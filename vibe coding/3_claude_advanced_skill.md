# Claude 进阶使用_skill

[TOC]

## 什么是 Skill

claude code 是大模型的一个脚手架，以擎天柱为比喻。

claude code 就是擎天柱，一个超级机器人。

大模型 就是他的引擎 ，为他提供强大的动力来源。

token 则是油箱里的油，是能量来源，每个动作都会消耗一定的能量。  

 skill 则是他集装箱中武器，更好的发挥他的战力。

所以可以直接使用大模型，但是使用脚手架配合可以更好地发挥出他的能力，skill 也不是必须他只是能力的扩展。 

这里只是简单解释了一下我们常见的，关于 claude 中更多的名词属于请参考[官方术语表](https://code.claude.com/docs/zh-CN/glossary#claude-md)

### skill 的结构

很多人以为 Skill 就是一个 Markdown 文件，其实不是。**一个完整的 Skill 是一个目录**，可以包含多种类型的文件，就像一个"能力包"。

打个比方：如果把 Skill 比作一本食谱，那么：

- **SKILL.md** 就是食谱本身（菜名、步骤、注意事项）
- **scripts/** 就是配套的厨房小工具（削皮刀、量杯 —— 封装好的辅助脚本）
- **resources/** 就是附赠的食材包和调料配比表（模板、示例数据、配置）
- **references/** 就是食谱末尾的"参考书目"（营养学标准、食品安全规范 —— AI 可随时查阅的参考资料）

**skill 的目录结构**

```
skill-xxx/               # Skill 根目录（命名规范：小写+短横线）
├── SKILL.md              # 核心：技能描述文件（必选）
├── scripts/              # 辅助脚本目录（可选）
│   ├── helper.py          # Python 辅助脚本
│   └── utils.js           # JavaScript 工具函数
├── resources/            # 配套资源目录（可选）
│   ├── template/          # 模板文件（如代码模板、报告模板）
│   ├── examples/          # 示例文件（如输入/输出示例数据）
│   └── config/            # 配置文件（如规则定义、默认参数）
├── references/            # 参考文档目录（可选）
│   ├── best-practices.md    # 最佳实践文档
│   ├── api-docs.md         # API 参考文档
│   └── standards.md        # 行业/团队编码规范
└── requirements.txt        # 依赖声明（可选，列出脚本需要的第三方包）
```

> **提示**：Skill 的核心是 `SKILL.md`，其余文件均为辅助。如果你的 Skill 只需要一份指令说明，只放一个 `SKILL.md` 就够了。但当 Skill 涉及复杂逻辑（如数据处理、格式转换）时，配上 `scripts/`、`resources/` 和 `references/` 会大幅提升 Skill 的能力和可维护性。那 SKILL.md 的结构如下：

这是 Skill 的核心载体。它包含两部分：头部的**元数据（Frontmatter）**和正文的**具体指令**。

```
---
# 元数据（Frontmatter，YAML 格式）
name: react-component-generator   # 技能名称（唯一标识）
version: 1.0                  # 技能版本
description: 根据需求生成符合项目规范的 React 组件文件集  # 技能简介
trigger: ["创建组件", "新建React组件", "生成组件"]      # 触发关键词
tools: ["typescript", "react"]    # 依赖工具
author: your-name              # 技能作者
---

# React 组件生成器

## 执行步骤
1. 确认组件名称和功能需求
2. 在 src/components/{componentName}/ 目录下创建文件
3. 按照 resources/template/ 中的模板生成代码
4. 运行 scripts/validate.js 验证组件结构

## 输出规范
- 所有文件创建完成后，报告创建的文件列表
- 给出组件的使用示例代码

## 错误处理
- 如果目录已存在，提示用户确认是否覆盖
- 如果缺少依赖包，提示安装命令

## 示例
给一个完整的输入→输出示例。
```

至于其他的文件则是需要编程的脚本文件，需要一定的编程能力，当然借助于 ai 也可以实现。怎么使用什么使用使用则需要根据 skill 的要求定夺了。

## skill 的获取

>  至于去哪里找开源的 skill ，下面已经列出了一些 skill 的仓库和社区平台，但是大部分，都需要根据自己的需求来添加 skill ，所以都是去各种论坛直接去找，比如 UI 美化的，项目管理的等等，并不是 skill 越多越好，他会使用大量的 token。

1. skill 仓库

| 资源方                | 链接                                              | 说明                                                         | 推荐星 |
| --------------------- | ------------------------------------------------- | ------------------------------------------------------------ | ------ |
| Anthropic Skills      | https://github.com/anthropics/skills              | Anthropic 官方 Skill 仓库，包含前端开发、文档、调研、命令开发、PR、测试等多个官方 Skill，是学习 `SKILL.md` 编写规范的最佳参考。 ([GitHub](https://github.com/vercel-labs/skills?utm_source=chatgpt.com)) | ⭐⭐⭐⭐⭐  |
| Vercel Labs Skills    | https://github.com/vercel-labs/skills             | Vercel 官方 Agent Skills CLI 及 Skill 仓库，支持 Claude Code、Codex、Cursor 等多个 Agent，可通过 `npx skills add` 安装和管理 Skills。 ([GitHub](https://github.com/vercel-labs/skills?utm_source=chatgpt.com)) | ⭐⭐⭐⭐⭐  |
| antfu/skills          | https://github.com/antfu/skills                   | Anthony Fu 维护，聚焦 Vue、Nuxt、Vite、TypeScript 等前端开发场景，内容质量高。 | ⭐⭐⭐⭐⭐  |
| baoyu-skills          | https://github.com/JimLiu/baoyu-skills            | 中文社区 Skill 仓库，包含写作、PPT、图片生成、开发等多个实用 Skill。 | ⭐⭐⭐⭐☆  |
| Deep-Research-skills  | https://github.com/Weizhena/Deep-Research-skills  | 面向 Deep Research、市场调研、信息检索等场景，适合研究类工作流。 | ⭐⭐⭐⭐☆  |
| antigravity-kit       | https://github.com/vudovn/antigravity-kit         | 提供 Agent 模板和 Skills，适合快速搭建自动化工作流。         | ⭐⭐⭐⭐☆  |
| claude-seo            | https://github.com/AgriciDaniel/claude-seo        | SEO 分析与网站优化 Skill，适合内容运营和技术 SEO。           | ⭐⭐⭐⭐   |
| humanizer             | https://github.com/blader/humanizer               | AI 文本优化 Skill，用于调整文本表达、降低机械感。            | ⭐⭐⭐⭐   |
| fact-check-skill      | https://github.com/petar-nauka/fact-check-skill   | 用于事实核查、引用验证和内容可信度检查。                     | ⭐⭐⭐⭐   |
| awesome-claude-skills | https://github.com/BehiSecc/awesome-claude-skills | Claude Skills 导航合集，收录大量社区 Skill 仓库，方便发现优质资源。 | ⭐⭐⭐    |

2. skill 社区平台

| 平台                             | 链接                                              | 收录规模        | 说明                                                         | 推荐星 |
| -------------------------------- | ------------------------------------------------- | --------------- | ------------------------------------------------------------ | ------ |
| **skills.sh**                    | [https://skills.sh](https://skills.sh/)           | 18,000+ Skills  | 最早的 Agent Skill 注册平台之一，支持搜索、安装、管理 Skills，也是很多工具兼容的生态入口。 ([arXiv](https://arxiv.org/abs/2607.00911?utm_source=chatgpt.com)) | ⭐⭐⭐⭐⭐  |
| **SkillsMP**                     | https://skillsmp.com/zh                           | 200万+ SKILL.md | 当前收录规模最大的 Skill 市场，聚合 GitHub 公开 Skills，支持中文、搜索、分类、创作者、API 等功能。 ([SkillsMP](https://skillsmp.com/zh?utm_source=chatgpt.com)) | ⭐⭐⭐⭐⭐  |
| **AgentSkills.io**               | [https://agentskills.io](https://agentskills.io/) | 开放标准        | Agent Skills 官方规范，定义 `SKILL.md` 格式及跨 Agent 的兼容标准，由社区维护。 ([GitHub](https://github.com/agentskills?utm_source=chatgpt.com)) | ⭐⭐⭐⭐⭐  |
| **SkillDuck**                    | https://github.com/william-zheng-tw/skillduck     | 开源项目        | 基于 skills.sh 的桌面管理工具，可视化管理、搜索和安装本地 Skills。 ([Reddit](https://www.reddit.com/r/AI_Agents/comments/1rbgce9/i_built_an_opensource_desktop_gui_for/?utm_source=chatgpt.com)) | ⭐⭐⭐⭐☆  |
| **GitHub Topics · Agent Skills** | https://github.com/topics/agent-skills            | 数千个仓库      | GitHub 官方 Topic 页面，可发现社区维护的 Skill 仓库和示例项目。 | ⭐⭐⭐⭐☆  |
| **GitHub Topics · Claude Code**  | https://github.com/topics/claude-code             | 数千个仓库      | 聚合 Claude Code 相关 Skills、MCP Server、工具链和模板。     | ⭐⭐⭐⭐☆  |

## skill 的安装与使用

> 现在我们就从社区中寻找一个 skill 安装并使用他，还是用之前创建的 静态网页为例，虽然已经很好看，但是还是想使用 skill 对他进行美化一下。这次我们使用同样的提示词配合 skill 看看回事什么样的效果。
>
> 接下来采用两种不同的方式安装 skill，方便应对不同的使用场景。

这次我们使用两个 skill 分别是：

- [官方工具库中的 frontend-design](https://github.com/anthropics/skills/tree/main/skills/frontend-design): Anthropic 自己做的前端设计技能，负责实现落地
- [impeccable](https://github.com/pbakaus/impeccable): 特定领域的参考设计文件，以及精心挑选的反模式，这些反模式能够准确地告诉模型哪些内容不应该生成。最终效果：用户界面看起来是经过精心设计的，而不是自动生成的。

**安装前说明**

skill 的安装分为全局安装和项目级安装

| 对比项              | 项目级安装（Project Skill）            | 全局安装（Global Skill）                        |
| ------------------- | -------------------------------------- | ----------------------------------------------- |
| 安装位置            | 当前项目目录（如 `.claude/skills/`）   | 用户目录（如 `~/.claude/skills/`）              |
| 生效范围            | 仅当前项目                             | 当前用户所有 Claude 项目                        |
| 使用场景            | 项目专属工具、团队共享能力             | 个人常用工具、通用能力                          |
| 是否随 Git 仓库共享 | ✅ 可以（提交到仓库）                   | ❌ 不会（仅本机）                                |
| 团队成员获取        | Clone 项目即可使用                     | 每个人需要单独安装                              |
| 配置维护            | 跟随项目版本管理                       | 用户自行维护                                    |
| 更新方式            | 项目更新后同步更新                     | 每台机器单独更新                                |
| 是否影响其他项目    | ❌ 不影响                               | ✅ 所有项目都会可用                              |
| 推荐用途            | 项目规范、代码生成、部署流程、测试流程 | Git、Docker、Kubernetes、翻译、文档等通用 Skill |
| 优点                | 团队一致性高、可版本管理、易于协作     | 一次安装，到处可用，避免重复安装                |
| 缺点                | 多个项目可能存在重复 Skill             | 团队无法自动同步，容易出现版本不一致            |

### 下载安装 frontend-design

进入 [官方工具库](https://github.com/anthropics/skills)下载并解压

![image-20260704170051759](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260704170051759.png)

刚安装的 claude 在 `.claude` 目录下没有 skills 目录需要手动新建，进入用户目录下需要 按`command`+`shift` + `.` 展示隐藏文件才可以看见。

![image-20260704171051635](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260704171051635.png)

![image-20260704171059117](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260704171059117.png)

这是一个 skill 包里面包含其他能力的 skill 我们挑选自己需要的进行 copy。我们这里仅仅只选用`frontend-design`复制或拖动到刚才新建的目录中,这就安装完成了。

![image-20260704171236821](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260704171236821.png)

### 命令安装 impeccable

进入 [impeccable](https://github.com/pbakaus/impeccable)可以找到`npx`安装命令

![image-20260704180716552](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260704180716552.png)

直接在项目目录先运行，之前安装 claude 使用的项目，为了看出 skill 的效果我 copy 了一下删除了资源之外的所有文件。

![image-20260704181810052](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260704181810052.png)

运行命令

```
npx impeccable install
```

![image-20260704181733005](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260704181733005.png)

下面多出来的这个文件夹就是 我们安装到项目的一个 skill

![image-20260704182027554](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260704182027554.png)

### 使用案例

>  还是采用之前的提示词，我们看看有什么效果上的差别

```markdown
你是一位世界级 Creative Frontend Designer，请设计并实现一个具有 **Apple、Linear、Vercel、Awwwards** 水准的单页面 Landing Page。

项目 `static` 目录已包含图片，请自动分析并选择最合适的图片作为 Hero、背景、Gallery、局部视觉及 Parallax 元素，无需我手动指定。

### 设计风格

整体风格应具有：

* Premium、Elegant、Minimal、Editorial、Cinematic、Immersive
* 像一本高端杂志，而不是普通企业官网

避免：

* 廉价渐变
* 玻璃拟态泛滥
* 彩虹发光
* 夸张阴影
* Bootstrap/Admin Dashboard 风格
* AI 模板感

### 页面结构

自由设计页面节奏，可参考：

Hero → Story → Image Showcase → Gallery → Highlight → Ending

每个 Section 都要拥有不同的视觉节奏、留白和层次，营造电影般的视觉叙事（Visual Storytelling）。

### UI 设计

* 大胆留白，Editorial Typography
* 超大标题、舒适正文宽度
* 图片允许突破布局、局部裁切、全屏展示
* 配色根据图片自动提取，以黑、白、灰、暖白、低饱和色为主，仅少量 Accent Color
* 整体克制、优雅、高级

### 动效

动画应自然且服务于内容，不炫技。

包括：

* 页面进入：Fade、Blur、Translate、Stagger
* Scroll Reveal：不同 Section 使用不同 Reveal
* Hero：Slow Zoom、Parallax、文字逐字出现
* 图片 Hover：轻微 Scale、Brightness、Contrast
* Gallery：Parallax、多层滚动
* Section Transition：平滑过渡、Mask Reveal
* 微妙背景 Noise/Grain
* Ending 保持电影般的留白和收尾

### 技术要求

遵循当前项目技术栈（HTML/CSS/JS 或 React/Next/Vue）。

优先使用 GSAP、Framer Motion、Motion One、Intersection Observer 等实现高性能动画。

要求：

* 响应式（Desktop / Tablet / Mobile）
* Lazy Load
* GPU 加速
* 避免 Layout Thrashing
* 代码结构清晰、组件化、可维护

### 最终目标

整个网页应像国际品牌发布会官网或 Awwwards 获奖作品，而不是模板网站。打开页面后给人的第一感觉应该是：

**「这不是网页，而是一段具有电影感的视觉体验。」**

请直接输出完整、可运行的代码，并充分利用 `static` 目录中的所有图片完成设计。

```

这次用了 0.4 圆的 token 应该是好一点吧

[效果展示](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/itab.link-1783161313555.webm)

## 创建自己的 skill

之前我们下载的 skill 工具中有一个 `skill-creator`的他是帮助我们创建，把他放到全局的 skill 目录下

![image-20260704185507740](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260704185507740.png)

然后我们就可以直接让 claude 进行创建 skill 了，`/skill-creator` 表示主动指定使用这个 skill

> **温馨提示，本次教学创建的这个skill功能，使用 hooks 触发会更合适，只是举个例子**

```
帮我创建一个 skill 在每次使用其他的 skill 时触发，告诉我使用了哪一个 skill ，使用这个 skill 干什么 /skill-creator
```

![image-20260704185250767](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260704185250767.png)

这就创建好了，可以在他说的路径下查看到![image-20260704190943104](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260704190943104.png)

````markdown
---
name: skill-tracker
description: Skill 使用追踪器。当任何 skill 被触发使用时，立即向用户报告：用了哪个 skill、用来做什么。如果你发现对话中出现了 /skill-creator、/frontend-design、/code-review 等 skill 命令，或者 Claude 正在调用某个 skill，立刻告诉用户。不要因为用户没有明确提到"追踪"才触发——只要有任何 skill 被激活，就要报告。

---

# Skill 使用追踪器

## 功能

追踪并报告当前会话中所有被触发的 skill。

## 工作流程

每次检测到有 skill 被触发时（通过以下方式之一判断）：

1. **显式命令**：用户输入了 `/skill-name` 形式的命令
2. **Skill 工具调用**：你调用了 `Skill` 工具并指定了某个 skill
3. **上下文提示**：系统提示中提到了某个 skill 已被加载或正在使用

立即向用户输出以下格式的报告：

```
📡 Skill 已触发
━━━━━━━━━━━━━━━━━━━━━━━
• 使用的 skill：{skill 名称}
• 用途说明：{根据用户的请求和 skill 的行为，简要描述这个 skill 用来做什么}
• 触发方式：{命令 / 工具调用 / 自动触发}
━━━━━━━━━━━━━━━━━━━━━━━
```

## 示例

- 用户输入 `/code-review` → 报告：「📡 Skill 已触发 — 使用的 skill：code-review — 用途：审查代码变更的正确性和优化空间」
- 用户输入 `/skill-creator` → 报告：「📡 Skill 已触发 — 使用的 skill：skill-creator — 用途：创建和改进新的 skill」

## 注意事项

- 每次 skill 触发都要报告，不要合并或跳过
- 如果无法确定 skill 的用途，基于 skill 名称和上下文做出合理推断
- 报告要简洁，控制在 2-3 行以内
- 即使用户没有明确要求追踪，只要 skill 被触发就主动报告
````

## 常用 skill 推荐

> 根据自己的需求增加 skill ，并不是越多越好的

| Skill                       | 地址                                                         | 简介                                                         |
| --------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| **karpathy-guidelines**     | https://github.com/multica-ai/andrej-karpathy-skills/tree/main | 遵循 Andrej Karpathy 的编程理念，引导 AI **先思考、再编码**，减少错误、精准修改、保持代码简洁。 |
| **frontend-design**         | https://github.com/anthropics/skills/tree/main/skills/frontend-design | Anthropic 官方前端设计 Skill，帮助 AI **实现高质量页面、组件和响应式布局**，将设计方案落地。 |
| **skill-creator**           | https://github.com/anthropics/skills/tree/main/skills/skill-creator | Anthropic 官方 Skill，用于 **创建、组织和优化新的 Skill**，帮助 AI 快速构建可复用的能力模块。 |
| **remotion-best-practices** | https://github.com/remotion-dev/remotion                     | 提供 Remotion 视频开发最佳实践，指导 AI **构建视频场景、动画、时间轴以及视频渲染流程**。 |
| **superpowers-zh**          | https://github.com/jnMetaCode/superpowers-zh                 | AI 编程超能力中文增强版，集成 **20 个开发 Skill**，覆盖头脑风暴、规划、TDD、代码审查、调试等完整开发流程，支持 Claude Code、Cursor、Copilot CLI、Gemini CLI 等多种 AI 编程工具。([github.com](https://github.com/jnMetaCode/superpowers-zh?utm_source=chatgpt.com)) |
