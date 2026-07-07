

# Claude 入门
[TOC]
## Claude简介

本教程目标不会代码的人也可以直接进行使用`claude`进行` vibe coding`，本章安装之后只要可以明确的说出自己的需求就可以进行使用了，后面的章节属于高级用法。

> **本教程皆以 mac 系统命令为例，windows 部分命令不可用**
>
> 教程中所有的终端命令 `#` 表示这一行为解释，没有标注的则为命令，可直接复制使用。
>
> **教程中会出现各种技术的官网有兴趣可以点进去看看，官网才是最权威的说明书，虽然不好懂，但是看久了就知道了**

### 常用终端命令

> 蛮重要的，不会也没关系 用的时候查一下

```shell
# 展示所在当前文件夹路径
pwd

# 展示当前文件夹下所有文件
ls -a

# 创建 dir_name 文件夹
mkdir dir_name

# 创建 file_name.md 文件
touch file_name.md

# 清除终端内容
clear

# 前往当前文件夹下的 dir_name 文件内
cd ./dir_name

# 删除当前目录下 dir_name 文件
rm -rf dir_name
```

### 废话阶段

我在写教程这天爆出 claude、codex 都有窃取国内用户的身份信息 ，这个信息只是标注你是否是国内用户，采用非常隐秘的手段，在字符编码上动的手脚导致肉眼根本看不出来。

今天他敢打你，明天就干打天下😂😂😂😂😂😂

## Claude 安装

这里使用客户端是在官网看到了新出了这个临时起意，不需要安装各种环境，就可以使用，但使用来进行 vibe coding 那些环境是必不可少的。不想麻烦只想体验一下的小伙伴可以直接使用这种，毕竟界面对于不习惯使用 终端的 小伙伴还是很有好的。

下面的两种安装方式可以选择一种，也可以都安装，作者这里两种都装了，不喜欢到时候卸载就是了。

**本教程主要针对 CLI 模式的 claude**

### 客户端安装

  [claude 的桌面版本官网地址](https://code.claude.com/docs/zh-CN/desktop-quickstart)我安装的是 mac 版本的

![image-20260701170825115](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260701170825115.png)

登录账号之后的界面（我也没用过）

![image-20260701172321142](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260701172321142.png)

### 安装 CLI（终端模式）

为此我把本地环境全都删了，从头开始，用心良苦快夸我 😏😏😏😏

1. 终端运行以下命令 安装 `nvm` ```[NVM 官网](https://www.nvmnode.com/guide/installation.html)

> `nvm` 是 `node` 版本管理工具，我们主要是为了安装 `node`，然后通过 `node` 进行安装 `claude`
   >
   > `node` 是前端代码开发的 `JavaScript`运行环境，安装之后进行 `vibe coding` 时就省去这一步骤了

```shell

# 安装命令 
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.3/install.sh | bash

# 刷新环境变量
source ~/.zshrc

# 验证安装 nvm
nvm --help
```

出现下面的 `Node Version Manger...` 的信息就已经安装完成了！！

![image-20260701162817857](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260701162817857.png)

2. 通过`nvm` 安装 `node` [node 官网](https://nodejs.org/zh-cn)

```shell
# 安装 24 版本的 node
nvm install 24

# 检查安装状态 出现 v24.18.0 就表示成功了，这里的版本号不固定，出来了就表示成功了
node --version
```

![image-20260701165332022](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260701165332022.png)

3. 使用 node 安装 claude

```shell
# 安装 claude
npm install -g @anthropic-ai/claude-code

# 验证安装是否成功
claude --version
```

![image-20260701230311717](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260701230311717.png)

> 这个时候就已经安装成功了，但是他需要官方的账号才可以使用，平民玩家玩不起啊。![image-20260702094132615](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260702094132615.png)
>
> 他的安装位置在用户目录下 运行以下命令可以看见，以后我们也会经常进来更改配置或是安装`skill`等操作

```shell
cd ~
ls -a
```

![image-20260701230650511](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260701230650511.png)

4. 申请模型 api 这里以 Deepseek 为例[Deepseek 官网](https://www.deepseek.com/)

![image-20260701173223713](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260701173223713.png)

登录进去创建自己的 api key
![image-20260701174206230](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260701174206230.png)
**一定要复制保存好 不能给别人 ，他就相当于你的银行卡号和密码的组合体，别人可以直接使用记得 token**
**一定要保存好，泄漏后及时删除即可**
![image-20260701174310482](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260701174310482.png)

缴费阶段，模型可不是白让你用的，国内的 token 还是蛮便宜的，相对于国外😏![image-20260701174555879](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260701174555879.png)

点击左侧的产品定价，就可以看到模型的价格了，我选用了 `deepseek-v4-flash`模型，我主要看中的就是他的价格😂，响应速度还是很可以正常使用完全没问题

![image-20260702093101693](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260702093101693.png)

5. 安装 cc switch

> cc switch 是用来切换 claude 模型的工具，不然还需要手动更改环境变量很麻烦，而且还可以管理其他工具的模型 如：codex、openclaw 等。

[cc switch 官网](https://www.ccswitch.io/zh/) 点击免费下载后会跳转到 git 仓库 选择系统对应的版本下载就可以了.

![image-20260701172922313](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260701172922313.png)

打开就是这样子的，我们接下来要去找模型和 api 去给他配置，然后启动claude

![image-20260702095029765](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260702095029765.png)

![image-20260702095055389](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260702095055389.png)

填写你的 api key

![image-20260702095121349](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260702095121349.png)

选择模型，点击保存就 OK 啦！然后点击启用你的 claude 就可以使用啦。

![image-20260702095237433](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260702095237433.png)

### claude 第一次使用

> 前往新建的一个 文件夹 输入 claude 会出

![image-20260702103806516](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260702103806516.png)

> 选择主题

![image-20260702095804640](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260702095804640.png)

安全通知，就是《我已仔细阅读并同意》的步骤，建议审查一下 claude 生成的代码，回车确认就完了。

![image-20260702100004776](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260702100004776.png)

推荐使用 optimal+enter 进行换行 

![image-20260702100144473](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260702100144473.png)

信任文件夹，确认（除了这个信任文件夹之外都只会在第一次使用 claude 时会出现，所以不用）

![image-20260702101025434](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260702101025434.png)

可以是用了

![image-20260702101110630](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260702101110630.png)

![image-20260702101445788](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260702101445788.png)

7. 快速创建一个小网页

> 我提前在文件夹中放了一个 `static` 文件夹，里面有一些图片

![image-20260702101645561](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260702101645561.png)

> 下面是使用 GPT 生成的一段网页生成的提示词

```md
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

> 中途会出现各种需要授权的，需要确认

![image-20260702102452086](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260702102452086.png)

>  生成效果（消耗了0.21 元的 token）

[效果展示](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/itab.link-1782962109773.mp4)

## Claude 卸载

> 我们这次是使用 npm 进行安装的 所以直接运行一下命令就好了，但是 nvm 和 node 的卸载 我不想写了，哈哈哈哈。

```
# 标准卸载命令
npm uninstall -g @anthropic-ai/claude-code

# 删除 Claude 主目录
rm -rf ~/.claude

# 删除本地 bin 中的可执行文件
rm -f ~/.local/bin/claude

# 删除可能的其他位置
rm -f /usr/local/bin/claude
```

## 其它工具(VS code)

> 不是必装软件，不想装可以跳过。
> 在接下来的教程中我会使用一段时间的 终端命令行的模式 ，但是由于实用性我更推荐使用 VS Code 配合插件的形式

[VS code 官网](https://code.visualstudio.com/) 在这里下载

> 下面是我打开教程中 [claude 第一次使用](#claude 第一次使用) 创建的项目

![image-20260702153545751](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260702153545751.png)

> 刚下载完成你们的软件中是没有 claude 区域的 需要下载插件搜索，然后 3 的位置有安装 点击下载，重启就可以开到 4 的未知有一个标志，就可以打开 claude 区域了。
>
> **注意插件的作者是 `Anthropic` 不要安装错了**[插件官方安装地址](https://code.claude.com/docs/zh-CN/vs-code)

![image-20260702154439830](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20260702154439830.png)
