# Claude 进阶使用_workflow

[TOC]

## 什么是  workflow

[官方解释](https://code.claude.com/docs/zh-CN/workflows)动态工作流是让 AI 自己编写编排脚本，把一个大任务拆成若干子任务，分发给数十到数百个并行子 Agent 执行，最后汇总验证结果的能力。但是这动态工作流是由 agent 自动生成分解的流程，

通过刚才的解释感觉更像是分配了很多任务，然后调用`subagent`，像是重复了。工作流不仅要分配人格给 `subagent`还要分析要不要拆任务？拆成几个？谁负责？是否重新执行？是否需要验证？，而`subagent`仅仅只负责执行。

![image-20260708140134831](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260708140134831.png)

## 使用 workflow

给出了我们一个内置保存好的工作流，

```
/deep-research 你需要查询的问题
```

![image-20260708091611324](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260708091611324.png)

在工作的时候 主 agent 不会被影响，所有子 agent 都会以 background 的形式运行。输入`/workflows`可以查看工作的工作流,`enter`可以看到里面的工作详情。

```
/workflows
```

![image-20260708091826698](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260708091826698.png)

![image-20260708092123463](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260708092123463.png)

**工作流的操作**

| 键             | 操作                                                         |
| :------------- | :----------------------------------------------------------- |
| `↑` / `↓`      | 选择一个阶段或代理                                           |
| `Enter` 或 `→` | 深入选定的阶段，然后进入代理以读取其提示、最近的工具调用和结果 |
| `Esc`          | 返回一个级别                                                 |
| `j` / `k`      | 当代理详情溢出时在其中滚动                                   |
| `f`            | 按状态过滤选定阶段中的代理列表。再次按下以循环               |
| `p`            | 暂停或恢复运行                                               |
| `x`            | 停止选定的代理，或当焦点在运行上时停止整个工作流             |
| `r`            | 重启选定的运行中代理                                         |
| `s`            | [保存](https://code.claude.com/docs/zh-CN/workflows#save-the-workflow-for-reuse)运行的脚本作为命令 |

## 创建 workflow

[官网说明](https://code.claude.com/docs/zh-CN/workflows#ask-for-a-workflow-in-your-prompt)中使用`ultracode` 进行触发，但是在实验多次之后都不行，使用的 claude 版本是 `2.1.204`。最终还是在提示词中添加`使用工作流`进行触发。

![image-20260708111922560](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260708111922560.png)

![image-20260708111634152](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260708111634152.png)

### 保存 workflows

使用 `/workflows`进入到工作流之后点击`s`就可以保存工作流了，然后会有一个命名的环节，可以直接`enter` 确认使用 claude 取得名字，下次使用时可直接`/<名字>`加提示词就可以了。

![image-20260708112302649](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260708112302649.png)

保存的工作流默认是项目级的，保存在`.claude/workflows`中，不需要可以删除，如果想保存为，全局的可以手动复制到``~、.claude/workflows``目录下，也可以在保存时使用 `Tab`键进行切换。

![image-20260708112716939](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260708112716939.png)
