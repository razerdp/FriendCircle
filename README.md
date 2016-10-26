# FriendCircle
##一起来撸个朋友圈吧

#【注意：】本项目将会从2016/10/26起全面重构，届时服务器将会采用Bmob，列表使用RecyclerView，并且去掉以前冗余的代码。Master分支下的代码暂时保持原样，相关操作将会同步到main-dev分支。

欢迎来到本项目，这个项目是一个尝试性项目，目的在于从无到有撸出一个微信朋友圈。</br>

**本项目的一切实现思路以及逻辑都有记录，它们都有在我的简书文集记载：**

[代码实现逻辑（简书合集）](http://www.jianshu.com/notebooks/3224048/latest)

**每次更新的模块Gif预览都在另一个MD文件里，如果您对效果感兴趣，不妨去查看一下：**

[更新日志](https://github.com/razerdp/FriendCircle/blob/main-dev/UPDATE_LOG.md)


直至目前为止，所做出的最新进度：

##2016-04-21
朋友圈图片浏览指示器完成（评论后台交互完成）

代码解析地址：

http://www.jianshu.com/p/17c51bd5ba70

图片预览：

![](https://github.com/razerdp/FriendCirclePreview/blob/master/img/2016-04-21_dot_indicator.gif)

***

###如果您有PR申请，请提交到dev分支，谢谢-V-

###如果您遇到popup的导入失败，请在工程的gradle（不是app的gradle哦）加上这句话：

```xml
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```


#####关于作者我:
普普通通的安卓开发者一枚。

#####关于项目：
本项目意在按照我的经验弄出一个微信朋友圈，也许无法完全实现，但高仿应该是没问题的，只是时间问题←_←。

本项目于2016/10/26 全面重构
本项目于2016/03/17 更改为mvp架构


本项目在更新的同时也会同步发布经验心得（撸代码的过程），不过因为在下才学疏浅，有些地方肯定做的不好，所以衷心希望如果您有好的建议或优化方案，可以留下脚印。

#####关于库：
因为不制造重复的轮子这句名言，本项目将会使用一些库，这些库基本都是github上比较流行，热度比较高的，比如下拉刷新框架使用的是android ultra pull to refresh(https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh) 等

当然，我们不能只用库，本项目也会有很多自定义控件（其实这些控件很早就上传到我的github了，只是木有人star....）

##LICENSE：
***
The MIT License (MIT)

Copyright (c) [2016] [razerdp]

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
