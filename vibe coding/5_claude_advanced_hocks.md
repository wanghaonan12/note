# Claude 进阶使用_hooks

[TOC]

## 什么是 hooks

`hooks` 的英文翻译是 `勾子`，他就是在程序运行到指定节点会触发的一个节点，你可以在这个节点做一些事情，就是这样。比如：

你先想象一下，你家门口装了一个门铃。正常情况下，你要有人按门铃，你才知道外面有人。但是如果装了一个人体感应器呢？只要有人走到门口，它就自动亮灯、录像、发通知，你根本不用去按任何按钮。Claude 的 Hooks，本质上就是这种"感应器"。

## hooks 都有哪些

> 作者在[官方文档](https://code.claude.com/docs/zh-CN/hooks)选了几个比较常用的，简单说明一下想要更多更具体的一定要去看官方文档。

| Hook（触发时机）     | 什么时候触发              | 常见用途             | 举个例子                                                     |
| -------------------- | ------------------------- | -------------------- | ------------------------------------------------------------ |
| **PreToolUse**       | Claude 准备调用工具之前   | 拦截、检查、限制操作 | 在执行 `rm -rf`、修改生产环境文件之前先确认；禁止编辑某些目录 |
| **PostToolUse**      | Claude 调用工具完成之后   | 自动执行后续任务     | 修改代码后自动运行 `eslint`、`prettier`、单元测试            |
| **UserPromptSubmit** | 用户刚发送消息时          | 对用户输入做预处理   | 自动补充项目规范、团队约定、代码风格到 Prompt 中             |
| **Stop**             | Claude 完成回答准备结束时 | 收尾工作             | 自动生成工作总结、保存日志、记录本次修改内容                 |
| **Notification**     | Claude 需要通知用户时     | 自定义通知方式       | 任务完成后发送系统通知、播放提示音、推送到 Slack             |

## hooks 怎么配置

> 文档中已经说明了只要在 	`setting.json` 中配置就可以了，并给出了案例

下面这段意思是 claude 准备使用命令 `rm *`的时候会执行项目目录下`.claude/hooks/block-rm.sh`的脚本

```json
{
  "hooks": {
    "PreToolUse": [
      {
        "matcher": "Bash",
        "hooks": [
          {
            "type": "command",
            "if": "Bash(rm *)",
            "command": "${CLAUDE_PROJECT_DIR}/.claude/hooks/block-rm.sh",
            "args": []
          }
        ]
      }
    ]
  }
}
```

我们写一个简单的，就是在 claude 停止工作的时候给一个提示，把下面的代码放进项目的`setting.json`，注意这个提示的命令是 mac 的原生命令`windows`不可用

```json
{
  "hooks": {
    "Stop": [
      {
        "hooks": [
          {
            "type": "command",
            "command": "osascript -e 'beep' -e 'display dialog \"Claude 已完成任务！\" buttons {\"确定\"} default button \"确定\"'"
          }
        ]
      }
    ]
  }
}
```

效果展示！！！！当然了，也可以在需要授权的时候让他进行提示

![image-20260705225344286](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260705225344286.png)