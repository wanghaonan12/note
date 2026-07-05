# Claude 进阶使用_MCP

[TOC]

## 什么是 MCP

>  [官方解释](https://code.claude.com/docs/zh-CN/managed-mcp)MCP 是 Anthropic 推出的一个标准化协议，让 AI 工具可以连接外部服务和数据源。你可以把 MCP 理解为给 AI 装"插件"或"扩展能力"。

上面的解释可能有些朋友还是不太理解，举个例子：

​	claude 是一个了解各个方面知识和技能的新同事，有一天你让他去财务那里，取一份上个季度的财务数据进行分析做成报表给你。结果他告诉你不知道谁是财务，而且财务也不认识他并不会把材料给他。这个时候你给他做了一个工牌，这个工牌上有去财务的地址，财务虽然不认识他，但是知道这个工牌啊，就把这个资料给他了。然后呢你又让他去人事那里整理同事的资料，又给了他一个新的工牌上面是去人事的地址。他每次需要其他部门帮助的时候就是用不同的工牌。

就是这样的一个流程

## 安装 MCP 服务

本次我们使用 git hub 进行演示，具体需要什么样的 mcp 服务需要自己去查询搜索，这没有一个市场可供挑选。下面是[官方给出添加 MCP 的基本语法](https://code.claude.com/docs/zh-CN/mcp)

```shell
# 基本语法
claude mcp add --transport http <name> <url>

# 真实示例：连接到 Notion
claude mcp add --transport http notion https://mcp.notion.com/mcp

# 带有 Bearer 令牌的示例
claude mcp add --transport http secure-api https://api.example.com/mcp \
  --header "Authorization: Bearer your-token"
  
  # 列出所有配置的服务器
claude mcp list

# 获取特定服务器的详细信息
claude mcp get github

# 删除服务器
claude mcp remove github

# （在 Claude Code 中）检查服务器状态
/mcp
```

在[git hub mcp](https://github.com/github/github-mcp-server)的 README.md 中可以看到具体的安装步骤，这是各个不同平台软件的安装跳转接口，不懂得可以翻译一下。

![image-20260705212004134](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260705212004134.png)





![image-20260705212131286](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260705212131286.png)

跳转之后就可以看到这里的安装步骤，需要获取[Personal Access Token](https://github.com/settings/personal-access-tokens/new)，进入页面后填写一下信息![image-20260705212850443](/Users/richwang/Library/Application Support/typora-user-images/image-20260705212850443.png)

点击创建之后会得到一个秘钥，**注意不要泄露！！！！！！**

![image-20260705212948707](/Users/richwang/Library/Application Support/typora-user-images/image-20260705212948707.png)

之后在终端中执行，这里的`YOUR_GITHUB_PAT` 替换成刚刚如上图的秘钥。

```shell
claude mcp add-json github '{"type":"http","url":"https://api.githubcopilot.com/mcp","headers":{"Authorization":"Bearer YOUR_GITHUB_PAT"}}'
```

![image-20260705214011969](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260705214011969.png)

进入 claude 之后 输入`/mcp`变展示出来刚才添加的 mcp 服务了。剩下的命令就不需要我一点一点示范了吧，都有解释的，并不是我懒😜😜😜😜

![image-20260705213848638](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260705213848638.png)

## 使用 MCP 服务

>  直接使用语言便可以控制他使用 mcp 服务如下

![image-20260705214344112](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260705214344112.png)

在刚才的设置中我仅仅给他了公共仓库的 查看权限 所以他说他看不见私有仓库的东西

![image-20260705214438817](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260705214438817.png)

到这里就结束啦，就是这些。