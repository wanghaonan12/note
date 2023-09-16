# mac安装homebrew使用国内镜像源 ,安装git

[homebrew | 清华大学开源软件镜像站](https://mirrors.tuna.tsinghua.edu.cn/help/homebrew/)

1. ## 配置环境变量

添加环境变量

```bash
#homebrew 环境变量
export HOMEBREW_API_DOMAIN="https://mirrors.tuna.tsinghua.edu.cn/homebrew-bottles/api"
export HOMEBREW_BOTTLE_DOMAIN="https://mirrors.tuna.tsinghua.edu.cn/homebrew-bottles"
export HOMEBREW_BREW_GIT_REMOTE="https://mirrors.tuna.tsinghua.edu.cn/git/homebrew/brew.git"
export HOMEBREW_CORE_GIT_REMOTE="https://mirrors.tuna.tsinghua.edu.cn/git/homebrew/homebrew-core.git"
export HOMEBREW_PIP_INDEX_URL="https://pypi.tuna.tsinghua.edu.cn/simple"
```



2. ## 安装脚本

```bash
/bin/zsh -c "$(curl -fsSL https://gitee.com/cunkai/HomebrewCN/raw/master/Homebrew.sh)"
```

> 无脑安装,选择题,不过中途会要安装一个软件,安装就是了

![image-20230916142241035](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230916142241035.png)

> 就是看需要安装需要的东西,反正我都安装了,具体装了啥也不知道,最后`git`也会被自动装上