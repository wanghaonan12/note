# Claude 进阶使用_plugin

[TOC]

## 什么是  plugin

**Plugin（插件）**可以理解成一个“打包好的能力扩展包”，它把 **Skill、Command、Agent、Hook、MCP 配置**等能力组合在一起，让 Claude Code 可以像安装软件一样加载一套完整工作流。

## 安装 plugin

> plugin 的安装和 skill 一样，但是他需要启用才可以

进入 [ui-ux-pro-max](https://github.com/nextlevelbuilder/ui-ux-pro-max-skill/blob/main/README.zh.md)可以找到安装命令

![image-20260704175037244](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260704175037244.png)

这是之前安装时使用的照片文件，控制变量作为新的项目，启动终端执行安装命令。

![image-20260704172153571](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260704172153571.png)

```
# 进入到 claude
claude

# 运行命令 添加插件市场
/plugin marketplace add nextlevelbuilder/ui-ux-pro-max-skill

# 安装插件
/plugin install ui-ux-pro-max@ui-ux-pro-max-skill
```

![image-20260704175241372](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260704175241372.png)

之后就会返现项目中多了一个文件夹，这就是我们安装的 plugin，并在这项目中启用了

![image-20260704180203402](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260704180203402.png)

```json
{
  "enabledPlugins": {
    "ui-ux-pro-max@ui-ux-pro-max-skill": true
  }
}
```



## 使用 Plugin

> 还是使用之前提示词

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

和之前的 skill 一样使用

![image-20260707150931667](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260707150931667.png)