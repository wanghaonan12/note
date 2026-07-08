# Claude 进阶使用_agent teams

[TOC]

## 什么是  agent teams

[官网说明](https://code.claude.com/docs/zh-CN/agent-teams)**agent teams 就是多个 Claude Code 实例协作，就像一个真实的软件研发团队；而 Subagents 更像一个老板带几个临时助手。** 在多个 Claude Code 实例模式中，每个 Claude 都是一个独立的“员工”，拥有自己的 context window（工作记忆），例如架构师负责设计系统，后端负责接口开发，前端负责页面开发，数据库负责数据设计，他们之间可以直接交流、讨论问题，就像公司的研发团队一样，项目负责人只需要协调任务、整合结果。这种模式适合开发大型项目，例如 SaaS、电商、微服务系统，因为复杂任务可以被不同角色同时推进。而 Subagents 模式则不同，它更像一个人带几个流水线助手，主 Claude 是老板，Subagent 是临时外包人员，老板把任务分出去，助手完成后只能把结果汇报给老板，助手之间不能直接沟通，你也不能跳过老板直接找某个助手交流。因此 Subagents 更适合简单任务拆分，比如分析一个文件、写一个工具函数、检查一段代码，而多个 Claude Code 实例更接近真实的软件团队协作，可以共同完成复杂工程。简单来说：**Subagents 是“一个大脑派几个小手干活”，多个 Claude 实例是“多个独立大脑组成一个团队一起工作”。**

![image-20260708141313824](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260708141313824.png)

## 使用 agent teams

> 直接给出提示词让 claude 自己进行工作，说明需要的团队，团队的任务等，剩下的交给 `claude`

````
帮我创建一个专家团队，拥有 5 个系统架构师，让他们互相交流，试图推翻彼此的理论，就像一场科学辩论。每个 agent 都要调用 /brainstorming 向我询问项目的具体要求，使用的技术框架，帮我设计一个后端框架，然后将最终的定下俩的系统架构整理成文件输出。
````

claude 开始自己的工作创建专家团队，会在项目的 ``.claude/agents`目录下创建角色

![image-20260708143253142](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260708143253142.png)

后面又让他加了三个专家 哈哈哈哈！

![image-20260708171334841](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260708171334841.png)
