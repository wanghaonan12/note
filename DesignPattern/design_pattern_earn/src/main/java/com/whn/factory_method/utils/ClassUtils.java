package com.whn.factory_method.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @Author: WangHn
 * @Date: 2024/5/28 16:22
 * @Description: 超实用的工具类
 */
public class ClassUtils {
    private ClassUtils(){
        throw new IllegalStateException("Utility class");
    }
    /**
     * 给一个接口，返回这个接口的所有实现类
     *
     * @param c
     * @return
     */
    public static <T> List<Class<? extends T>> getAllClassByInterface(Class<? extends T> c) {
        //返回结果
        List<Class<? extends T>> returnClassList = new ArrayList<>();
        //如果不是一个接口，则不做处理
        if (c.isInterface()) {
            //获得当前的包名
            String packageName = c.getPackage().getName();
            try {
                //获得当前包下以及子包下的所有类
                List<Class<? extends T>> allClass = getClasses(packageName);
                //判断是否是同一个接口
                for (Class<? extends T> aClass : allClass) {
                    //判断是不是一个接口 && 本身不加进去
                    if (c.isAssignableFrom(aClass) && !c.equals(aClass)) {
                        returnClassList.add(aClass);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return returnClassList;
    }

    /**
     * 从一个包中查找出所有的类，在jar包中不能查找
     *
     * @param packageName
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private static <T> List<Class<? extends T>> getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class<? extends T>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }


    private static <T> List<Class<? extends T>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<? extends T>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add((Class<? extends T>) Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
