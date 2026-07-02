package com.whn.processor;

import com.google.auto.service.AutoService;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.Set;

/**
 * @author whn
 * @time 2025/7/10
 * @description ToStringProcessor
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.whn.processor.AutoToString")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class ToStringProcessor extends AbstractProcessor {

    private JavacTrees trees;      // 提供 AST 抽象语法树的访问入口
    private TreeMaker treeMaker;   // 构造新的 AST 节点的工具类
    private Names names;           // 创建标识符（变量名、方法名等）

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        ProcessingEnvironment realEnv = jbUnwrap(ProcessingEnvironment.class, processingEnv);
        this.trees = JavacTrees.instance(realEnv);
        this.treeMaker = TreeMaker.instance(((JavacProcessingEnvironment) realEnv).getContext());
        this.names = Names.instance(((JavacProcessingEnvironment) realEnv).getContext());
    }

    @SuppressWarnings("unchecked")
    private static <T> T jbUnwrap(Class<? extends T> iface, T wrapper) {
        T unwrapped = null;
        try {
            final Class<?> apiWrappers = wrapper.getClass().getClassLoader().loadClass("org.jetbrains.jps.javac.APIWrappers");
            final java.lang.reflect.Method unwrapMethod = apiWrappers.getDeclaredMethod("unwrap", Class.class, Object.class);
            unwrapped = iface.cast(unwrapMethod.invoke(null, iface, wrapper));
        } catch (Throwable ignored) {}
        return unwrapped != null ? unwrapped : wrapper;
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(AutoToString.class)) {
            if (!(element instanceof TypeElement)) continue;

            // 获取语法树表示的类定义（ClassDecl）
            JCTree.JCClassDecl classDecl = (JCTree.JCClassDecl) trees.getTree(element);

            java.util.List<VariableElement> fields = element.getEnclosedElements().stream()
                    .filter(e -> e instanceof VariableElement)
                    .map(e -> (VariableElement) e)
                    .toList();

            // 初始字符串，例如：User [
            JCTree.JCExpression expr = treeMaker.Literal(element.getSimpleName() + " [");

            for (int i = 0; i < fields.size(); i++) {
                VariableElement field = fields.get(i);
                String fieldName = field.getSimpleName().toString();

                // 拼接字段名，例如：+ "name="
                expr = treeMaker.Binary(JCTree.Tag.PLUS, expr, treeMaker.Literal(fieldName + "="));

                // 拼接字段值，例如：+ name
                expr = treeMaker.Binary(JCTree.Tag.PLUS, expr, treeMaker.Ident(names.fromString(fieldName)));

                if (i < fields.size() - 1) {
                    // 拼接逗号，例如：+ ", "
                    expr = treeMaker.Binary(JCTree.Tag.PLUS, expr, treeMaker.Literal(", "));
                }
            }

// 末尾补上 "]"
            expr = treeMaker.Binary(JCTree.Tag.PLUS, expr, treeMaker.Literal("]"));

// 构造 return 语句
            JCTree.JCStatement returnStatement = treeMaker.Return(expr);

            // 创建方法体 block {...}
            JCTree.JCBlock body = treeMaker.Block(0, List.of(returnStatement));
            // 创建方法声明 public String toString() { ... }
            JCTree.JCMethodDecl method = treeMaker.MethodDef(
                    treeMaker.Modifiers(Flags.PUBLIC),
                    names.fromString("toString"),
                    treeMaker.Ident(names.fromString("String")),
                    List.nil(), List.nil(), List.nil(),
                    body, null
            );

            // 将生成的方法插入类中
            classDecl.defs = classDecl.defs.append(method);
        }
        return true;
    }
}
