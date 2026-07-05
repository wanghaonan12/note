# Claude 进阶使用_1

[TOC]

> 本章节内容并不影响到你对于 `claude` 的使用，只是介绍讲解一些枯燥的知识点和配置，和一些使用小技巧，其中有一些知识点如`skill`细节会在后面的章节介绍。
>
> 介绍中如果有一些内容不明白的可以 AI 询问一下，最后再看官网，毕竟官方文档都不是很好懂啊，但是这个`claude`的写的还不错。

## setting.json 配置

>  温馨提示：`~/`表示用户目录下，进入后需要展示隐藏文件才可以在资源管理器或是访达中看到。
>
> mac 用户快捷键为（command + shift + 。）

Claude Code 有多层配置体系，从全局到项目级，层层覆盖。

| 配置类型                                          | 文件路径 / 来源                          | 作用范围               | 主要用途                                                     | 优先级     |
| ------------------------------------------------- | ---------------------------------------- | ---------------------- | ------------------------------------------------------------ | ---------- |
| **全局配置（Global Settings）**                   | `~/.claude/settings.json`                | 所有 Claude 项目       | 配置通用行为，如模型、权限、工具、默认设置等                 | ⭐ 最低     |
| **项目级配置（Project Settings）**                | `项目根目录/.claude/settings.json`       | 当前项目               | 覆盖全局配置，为当前项目设置专属行为                         | ⭐⭐ 中      |
| **本地项目配置（Local Project Settings）**        | `项目根目录/.claude/settings.local.json` | 当前项目（仅当前用户） | 保存个人本地配置，不提交到 Git，例如个人权限、实验性设置     | ⭐⭐⭐ 较高   |
| **命令行参数（CLI Options）**                     | 启动 Claude Code 时指定                  | 当前会话               | 临时覆盖部分配置，例如模型、权限模式等                       | ⭐⭐⭐⭐ 高    |
| **托管策略（Managed Settings / Managed Policy）** | 企业管理后台或系统级托管配置             | 整个组织或设备         | 企业统一管理安全策略、权限、MCP、Hooks、网络访问等，用户无法修改 | ⭐⭐⭐⭐⭐ 最高 |

**常用的配置**

> 下面只是一些常用到的配置，作者已经配置在了全局的 setting.json 如： `Bash(npm *)`、`Bash(git *)`、`Bash(node *)`、`Bash(rm -rf *)`这些配置
>
> 如果需要更多的配置满足使用要求请看[官网配置说明](https://code.claude.com/docs/zh-CN/settings)

```json
{
  "permissions": { // 允许 Claude Code 执行的操作（不再需要每次确认）
    "allow": [
      "Edit(/docs/**)", // 允许编辑项目 docs 目录下的文件
      "Read(~/.zshrc)", // 允许读取主目录的 .zshrc
      "Edit(//tmp/scratch.txt)", // 允许编辑绝对路径的临时文件
      "Read(src/**)", // 允许读取当前目录 src 子目录的文件
      "Read", // 读取文件 没有位置限制
      "Write", // 写入文件 没有位置限制
      "Edit", // 编辑文件 没有位置限制
      "Bash(npm *)", // 执行 npm 命令
      "Bash(git *)", // 执行 git 命令
      "Bash(node *)" // 执行 node 命令
    ],
    "deny": [ // 禁止执行危险的删除命令
      "Bash(rm -rf *)"
    ]
  },
  // 默认使用的模型
  "model": "sonnet",
  // 自动紧凑阈值（上下文使用超过此比例时自动压缩）
  "autoCompactThreshold": 80
}
```

当然在进入 claude cli  中也可以直接使用 `/config`进入配置界面，当然这里可以配置的不是很多

![image-20260702225125052](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260702225125052.png)

也可以通过向 `/config` 传递 `key=value` 来更改单个选项而无需打开界面，例如 `/config verbose=true`，这种方法也仅限于进入配置页面展示的这些参数，如果没有的设置后不会出现在 setting.json 中

```shell
# 会话关闭时保留的天数 
/config language=中文
```

## claudel.md 文件

详细文档请阅读[官方文件](https://code.claude.com/docs/zh-CN/memory#how-claude-md-files-load)

CLAUDE.md 拥层级（由顶向下叠加生效，规则冲突时优先级高的生效）

| 层级                     | 文件路径                                                     | 作用范围           | 主要用途                         | 优先级     |
| ------------------------ | ------------------------------------------------------------ | ------------------ | -------------------------------- | ---------- |
| 全局级                   | `~/.claude/CLAUDE.md`                                        | 所有项目           | 个人偏好、默认语言、编码习惯     | ⭐ 最低     |
| 项目级                   | `./CLAUDE.md` 或 `./.claude/CLAUDE.md`                       | 当前项目           | 项目架构、技术栈、团队规范       | ⭐⭐ 中      |
| 本地项目级               | `CLAUDE.local.md`                                            | 当前项目（仅自己） | 个人测试配置、实验规则           | ⭐⭐⭐ 中高   |
| 子目录级                 | `子目录/CLAUDE.md`                                           | 当前目录及子目录   | 模块级规则、局部约定             | ⭐⭐⭐⭐ 高    |
| 托管策略（Organization） | **macOS：**`/Library/Application Support/ClaudeCode/CLAUDE.md`**Linux / WSL：**`/etc/claude-code/CLAUDE.md`**Windows：**`C:\Program Files\ClaudeCode\CLAUDE.md` | 整个组织           | 公司编码规范、安全要求、合规策略 | ⭐⭐⭐⭐⭐ 最高 |

**两个官方推荐的创建姿势：**

- **`/init` 创建项目级**：在项目根目录下运行 `claude` 后输入 `/init`，cc 会自动扫描项目并生成一份 CLAUDE.md 初稿，你再调整。官方建议：**项目有一定规模再 `/init` 效果更好**（太空它扫不出什么东西）。

- **`/memory` 编辑全局级**：在 cc 会话里输入 `/memory` 选择“全局 CLAUDE.md”，会用默认编辑器打开该文件供你修改。**修改全局后需重启 cc 才生效。**

  ![image-20260703110017766](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260703110017766.png)

- **实践建议：**

  1. **保持更新**：项目级 CLAUDE.md 应该是动态的——项目加了功能、踩了坑，就同步更新
  2. **足够具体**：技术栈写明具体版本号，目录结构要与实际一致
  3. **写明禁忌**：把"不要做什么"也写清楚（如"不要修改数据库迁移文件"）
  4. **适度简洁**：不要写成论文，AI需要的是关键信息而非赘述
  5. **只放"顶层不变原则"**：随着实践你会发现，CLAUDE.md 不该塞太多。卡帕西发布的「claude.skills」几百行通用规则就能拿 10 万+ Star——写点 “顶层、不变、须严守" 的东西就够了。[编程规约 skill](https://github.com/multica-ai/andrej-karpathy-skills)


## .claudeignore

类似于 `.gitignore`，用来告诉 Claude Code 哪些文件/目录不需要关注：

```
# .claudeignore 示例
node_modules/      # 依赖包目录（太大了，AI不需要看）
.next/            # Next.js 构建产物
dist/            # 编译输出
*.log            # 日志文件
.env             # 环境变量（包含敏感信息）
```

文件位于项目根目录下

![image-20260703142638983](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260703142638983.png)

## claude cli 常用命令

> 在终端里输入的常用指令 

```shell
# 最基本的启动方式（在当前目录启动）
$ claude

# 指定项目目录启动
$ claude --project-dir /path/to/your/project

# 使用指定模型启动
$ claude --model sonnet

# 单次执行模式（执行完就退出，适合脚本调用）
$ claude -p "请列出当前目录下所有的 JavaScript 文件"
```

## claude 斜杠命令

### 系统斜杠命令

> 进入中断后使用的指令

| 命令       | 作用                                        | 使用场景                             |
| ---------- | ------------------------------------------- | ------------------------------------ |
| `/help`    | 显示帮助信息                                | 忘记命令时查看                       |
| `/model`   | 查看/切换当前模型（高/中/低档）             | 需要换用更强/更快的模型时            |
| `/compact` | 压缩当前对话的上下文                        | 对话太长，AI开始“遗忘”早期内容时     |
| `/clear`   | 完全清空当前对话                            | 开始全新的任务时                     |
| `/context` | 详细查看上下文占比（各 MCP/Skill 各占多少） | 优化 token、诊断哪里挨上下文         |
| `/memory`  | 查看/编辑 CLAUDE.md 与自动记忆              | 管理项目/全局记忆、开启 Auto Memory  |
| `/status`  | 查看会话状态                                | 确认模型、Token 消耗                 |
| `/cost`    | 查看当前会话费用                            | 监控花了多少钱                       |
| `/review`  | 对当前项目进行代码审查                      | 完成功能后检查质量                   |
| `/init`    | 自动生成项目的 CLAUDE.md                    | 进入新项目后的第一件事               |
| `/plan`    | 切入 Plan Mode（只读规划模式）              | 复杂任务起手（详见 4.9 节）          |
| `/rewind`  | 回滚 cc 之前的修改                          | “后悔药”，下面重点讲                 |
| `/resume`  | 选择历史会话恢复                            | 上次话题还没聊完                     |
| `/btw`     | “顺便问一句”，不污染主上下文                | 主任务进行中想问个无关问题           |
| `/exit`    | 退出 claude code 交互式终端                 | 工作完成，或开新的会话终端清除上下文 |

**扩展管理命令：**

| 命令            | 作用                                               | 使用场景                     |
| --------------- | -------------------------------------------------- | ---------------------------- |
| `/skill <名称>` | 直接调用某个 Skill                                 | 手动触发，不要等 AI 自己决定 |
| `/agent`        | 创建、查看、调用子代理（SubAgent）                 | 手工创建专项 SubAgent        |
| `/plugin`       | 插件管理界面（discover / installed）               | 发现、安装、卸载插件         |
| `/login`        | 使用 Claude 官方订阅会员登录                       | 有 Claude Pro/Max 会员时首选 |
| `/simplify`     | 派 3 个子 Agent 从代码质量/性能/复用性三个角度优化 | 快速全面优化已有代码（git）  |

**最常用的三个命令详解：**

**`/compact` —— 上下文压缩（必须掌握）**

这是解决”用久了 AI 变笨”的核心武器。用 cc 一段时间会发现回答变慢、质量下降——这是因为你聊的每句话、它读的每个文件、它执行的每个操作的结果，都在挤占上下文空间。模型上下文虽然有 200K，但实际有效比例只有 60%-80%，且会随上下文增多能力下降。脑子里塞多了东西，它就容易把握不住重点。

`/compact` 命令会帮你”整理桌面” —— 把前面的对话压缩成摘要，腾出空间。

```
> /compact

AI: 上下文已压缩。当前对话摘要：
   - 我们正在开发一个书签管理器项目
   - 已完成：数据库设计、API端点
   - 当前正在：前端页面开发
```

**配套命令：`/context` —— 监控上下文余量**

在 `/compact` 之前，先用 `/context` 看看当前状况：它会详细展示上下文占比，包括各个 MCP、Skill 各占用了多少 token，让你知道是什么在”吃掉”上下文。

```
> /context

上下文使用情况：
  已使用: 142,000 / 200,000 tokens (71%)
  ├── 对话历史: 89,000 tokens
  ├── CLAUDE.md: 2,100 tokens
  ├── Skills: 12,500 tokens
  └── MCP 工具: 4,800 tokens
```

> 提示： **我的习惯**：看到上下文高于 60% 了，就 `/compact` 一下。别等到接近满载、cc 自动压缩才动手——那时候它已经开始”遗忘”了。也可以让 cc 帮你打开常驻显示，重启终端后底部就会一直显示上下文余量。

**`/compact` vs `/clear` —— 什么时候用哪个？**

| 命令       | 效果                         | 适用时机                             |
| ---------- | ---------------------------- | ------------------------------------ |
| `/compact` | 压缩历史为摘要，保留关键决策 | 同一任务对话过长、但还要继续做       |
| `/clear`   | 彻底清空，等于重开           | 一个独立任务彻底结束，要开始全新任务 |

>  **心法**：宁可”多 `/clear` 几次重新介绍背景”，也不要”一直聊一直聊”。每个 `/clear` 都是给 AI 一次重新聚焦的机会。

**`/rewind` —— “后悔药”（双击 ESC 快捷启动）**

当你让 cc 改了一些代码、过后发现不满意（或者项目被改坏了），cc 自带一个回滚机制：**在对话里输入 `/rewind`，或者直接双击 `ESC`**，就会进入回滚界面：

```
[Rewind] 选择回滚方式：
  1. 仅回滚对话        → 文件保留，只清除后面几轮对话
  2. 回滚对话 与 文件编辑 → 推荐！全部返回某个节点
  3. 仅回滚文件        → 保留对话，只还原文件
```

> 注意： **底线提醒**：`/rewind` 只能撤销 **cc 自己编辑过的文件**。它跑过的终端命令（安装依赖、下载文件、修改数据库）**撤不了**。真正靠谱的“后悔药”还是 Git（参见 4.5 节“Git 集成最佳实践”）。

**`/memory` —— 记忆管理**

Claude Code 有一个跨会话的“长期记忆”系统。它会自动记住你的偏好和项目信息，下次启动时依然记得。`/memory` 进去后可以编辑全局 / 项目 CLAUDE.md、开启自动记忆。具体记忆体系见 **4.2 节 记忆系统**。

**`/review` —— 代码审查（git）**

完成功能开发后，让 AI 审查你的代码质量：

```
> /review

AI: 正在审查项目代码...

审查结果：
 代码结构清晰
注意： api/bookmarks.ts 第15行：缺少输入验证
注意： components/BookmarkList.tsx：建议添加 loading 状态
 发现潜在安全问题：SQL 查询未使用参数化查询
```

### 自定义斜杠命令

> 你可以创建自己的斜杠命令，将常用操作封装成快捷方式。在项目根目录创建 `.claude/commands/` 目录，然后添加 Markdown 文件：

如新建一个 deploy.md

```shell
# 部署检查清单

请执行以下部署前检查：
1. 运行所有测试：npm test
2. 检查是否有 lint 错误：npm run lint
3. 确认 .env.example 已更新（如果添加了新的环境变量）
4. 构建项目：npm run build
5. 报告所有检查结果
```

整你提示已经出来了

![image-20260703161622997](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260703161622997.png)

> 没错和你想的一样这里定义的是项目级别的，可以在`~/.claude/commands/`目录下定义系统级的命令

## Claude 权限模式

在 claude 中有一些权限模式，vs code 中会展示以下的样式，但是在官网中并没有这些的说明但是也不难看出这些模式的权限范围;

1. Ask before edits(编辑前询问)：Claude 在每次编辑前会请求确认  
2. Edit automaticallu(自动编辑)：Claude 将编辑您选中的文本或整个文件  
3. Plan mode(计划模式)：Claude 会先分析代码并提出修改方案，再进行编辑  
4. Auto mode(自动模式)：Claude 将为每项任务自动选择最佳权限模式

![image-20260703144410507](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260703144410507.png)

但是中包含了下面的一些，可能在什么其他地方没有找到，有兴趣的朋友可以看一看[官网权限模式说明](https://code.claude.com/docs/zh-CN/permission-modes#vs-code)

| 模式                                                         | 无需询问即可运行的操作                                       | 最适合                   |
| :----------------------------------------------------------- | :----------------------------------------------------------- | :----------------------- |
| `default`                                                    | 仅读取                                                       | 入门、敏感工作           |
| [`acceptEdits`](https://code.claude.com/docs/zh-CN/permission-modes#auto-approve-file-edits-with-acceptedits-mode) | 读取、文件编辑和常见文件系统命令（`mkdir`、`touch`、`mv`、`cp` 等） | 迭代您正在审查的代码     |
| [`plan`](https://code.claude.com/docs/zh-CN/permission-modes#analyze-before-you-edit-with-plan-mode) | 仅读取                                                       | 在更改代码库前进行探索   |
| [`auto`](https://code.claude.com/docs/zh-CN/permission-modes#eliminate-prompts-with-auto-mode) | 所有操作，带后台安全检查                                     | 长时间任务、减少提示疲劳 |
| [`dontAsk`](https://code.claude.com/docs/zh-CN/permission-modes#allow-only-pre-approved-tools-with-dontask-mode) | 仅预先批准的工具                                             | 锁定的 CI 和脚本         |
| [`bypassPermissions`](https://code.claude.com/docs/zh-CN/permission-modes#skip-all-checks-with-bypasspermissions-mode) | 所有操作                                                     | 仅隔离容器和 VM          |

## claude 使用技巧

### 超长提示词场景

> 当我们的要求过于复杂，提示词也特别长的时候，可以将需求整理成一份PRD（产品需求文档），
>
> 在交互式 Claude Code 会话中`@文件名`的形式告诉 claude。

![image-20260703151917955](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260703151917955.png)

### 超长 Claude.md 场景

> 某些东西不适合全部塞进 CLAUDE.md（太长、太专门），但 claude code 需要的时候必须能查到。可以在 claude.md 以条件获取的方式加入：

**涉及到品牌视觉如：颜色、字体、间距等请参严格照规范文档** → `docs/brand-visual.md`

**涉及到产品文本风格样式：语调、术语表等严格遵循样式约定文档** → `docs/copywriting-style.md`

### 交互式终端中运行命令

> 有的时候需要临时试用一下终端命令 但当前 claude code 还是运行需要开新的终端很麻烦，
>
>  推荐：在 cc 会话里以 ! 开头，进入 Bash 模式跑命令

```shell
 !pwd
 !node --version
```

![image-20260703153324057](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260703153324057.png)

## git 版本管理工具

> Git 并不是 Claude Code 的必需品，没有安装 Git，Claude Code 依然可以创建文件、修改代码、运行命令等日常操作。但安装 Git 后，Claude Code 能够更高效地分析代码变更、更方便地帮助管理版本，也能在出现问题时快速恢复项目。
>
> 简单来说就是游戏里的存档点，只要你保存了，可以在任何时候恢复到之前的状态。
>
> Git 可以理解成一个专门帮你管理代码历史记录的工具。很多人刚开始写项目时，会不断复制文件夹，比如“项目”“项目新版”“项目最终版”“项目最终版2”，时间久了自己都分不清哪个才是最新、哪个还能正常运行。Git 就是用来解决这个问题的。它会记录项目的每一次修改，就像给代码建立了一本完整的历史日志，不需要反复复制项目，也不用担心改错后找不到之前的版本。
>
> 本说明中 git 的介绍并不是很详细对于一些小白不是很友好，在下很贴心的找了一篇较为详细的教程供各位阅读参考[知乎大神的笔记](https://zhuanlan.zhihu.com/p/625189086)

1. git 安装[git 官网说明](https://git-scm.com/install/mac)

```
brew install git
```

下面是 Git 的常用命令，不用掌握，只需要配置好后使用 claude code 就可以控制，当然如之前所说，不适用也不影响 claude code 的使用。

| 命令                                    | 作用                       | 示例                                                     |
| --------------------------------------- | -------------------------- | -------------------------------------------------------- |
| `git --version`                         | 查看 Git 是否安装成功      | `git --version`                                          |
| `git config --global user.name "姓名"`  | 设置用户名                 | `git config --global user.name "Tom"`                    |
| `git config --global user.email "邮箱"` | 设置邮箱                   | `git config --global user.email "tom@example.com"`       |
| `git config --list`                     | 查看 Git 配置              | `git config --list`                                      |
| `git init`                              | 初始化 Git 仓库            | `git init`                                               |
| `git clone 仓库地址`                    | 克隆远程仓库               | `git clone https://github.com/user/demo.git`             |
| `git status`                            | 查看当前仓库状态           | `git status`                                             |
| `git add 文件名`                        | 添加指定文件到暂存区       | `git add app.py`                                         |
| `git add .`                             | 添加所有修改到暂存区       | `git add .`                                              |
| `git commit -m "说明"`                  | 提交一个版本               | `git commit -m "完成登录功能"`                           |
| `git log`                               | 查看提交历史               | `git log`                                                |
| `git log --oneline`                     | 简洁查看提交历史           | `git log --oneline`                                      |
| `git diff`                              | 查看未提交的代码变化       | `git diff`                                               |
| `git diff --staged`                     | 查看已暂存的变化           | `git diff --staged`                                      |
| `git restore 文件名`                    | 恢复未提交的文件           | `git restore app.py`                                     |
| `git restore .`                         | 恢复所有未提交修改         | `git restore .`                                          |
| `git rm 文件名`                         | 删除文件并提交删除记录     | `git rm test.py`                                         |
| `git mv 原文件 新文件`                  | 重命名文件                 | `git mv old.py new.py`                                   |
| `git branch`                            | 查看分支                   | `git branch`                                             |
| `git branch 分支名`                     | 创建新分支                 | `git branch dev`                                         |
| `git checkout 分支名`                   | 切换分支（旧写法）         | `git checkout dev`                                       |
| `git switch 分支名`                     | 切换分支（推荐）           | `git switch dev`                                         |
| `git switch -c 分支名`                  | 创建并切换到新分支         | `git switch -c feature/login`                            |
| `git merge 分支名`                      | 合并分支                   | `git merge dev`                                          |
| `git branch -d 分支名`                  | 删除分支                   | `git branch -d dev`                                      |
| `git remote -v`                         | 查看远程仓库               | `git remote -v`                                          |
| `git remote add origin 地址`            | 添加远程仓库               | `git remote add origin https://github.com/user/demo.git` |
| `git push`                              | 推送代码到远程仓库         | `git push`                                               |
| `git push -u origin main`               | 首次推送并建立关联         | `git push -u origin main`                                |
| `git pull`                              | 拉取并合并远程代码         | `git pull`                                               |
| `git fetch`                             | 获取远程更新，不自动合并   | `git fetch`                                              |
| `git reset --soft HEAD~1`               | 撤销最近一次提交，保留代码 | `git reset --soft HEAD~1`                                |
| `git reset --hard HEAD~1`               | 回退到上一个版本（慎用）   | `git reset --hard HEAD~1`                                |
| `git reset --hard commit_id`            | 回退到指定版本             | `git reset --hard a1b2c3d`                               |
| `git stash`                             | 临时保存当前修改           | `git stash`                                              |
| `git stash pop`                         | 恢复临时保存的修改         | `git stash pop`                                          |
| `git stash list`                        | 查看临时保存记录           | `git stash list`                                         |
| `git tag`                               | 查看标签                   | `git tag`                                                |
| `git tag v1.0.0`                        | 创建标签                   | `git tag v1.0.0`                                         |

2. **注册 git hub**

[git hub 官网](https://github.com/)

3. 配置 git
 ```
   git config --global user.name "你注册的用户名"
   git config --global user.email "你注册的邮箱"
 ```

5. 