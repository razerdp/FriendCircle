# FriendCircle
##一起来撸个朋友圈吧(重构版)

欢迎来到本项目，这个项目是一个尝试性项目，目的在于从无到有撸出一个微信朋友圈。</br>

#####【简略更新日志】
 - 2016/12/16
    + 图片浏览（进场动画）
    + 准备着手退出动画和优化进场动画
    + 准备着手增加指示器

 - 2016/12/14 增加了简单的注册系统，方便测试，评论定位，以及评论请求完善，可以随便评论了哦~

 - 2016/12/01 main-dev分支合并到主分支，所有代码将被覆盖。

 - 本项目将会从2016/10/26起全面重构，届时服务器将会采用Bmob，列表使用RecyclerView，并且去掉以前冗余的代码。

更多日志请看 → [更新日志](https://github.com/razerdp/FriendCircle/blob/master/UPDATE_LOG.md)

**本项目的一切实现思路以及逻辑都有记录，它们都有在我的简书文集记载：**

[代码实现逻辑（简书合集）](http://www.jianshu.com/notebooks/3224048/latest)

**每次更新的模块Gif预览都在另一个MD文件里，如果您对效果感兴趣，不妨去查看一下：**

[更新日志](https://github.com/razerdp/FriendCircle/blob/master/UPDATE_LOG.md)


直至目前为止，原版的最新进度(重构版未到该进度，但会努力跟上)：

##2016-04-21
朋友圈图片浏览指示器完成（评论后台交互完成）

代码解析地址：

http://www.jianshu.com/p/17c51bd5ba70

图片预览：

![](https://github.com/razerdp/FriendCirclePreview/blob/master/img/2016-04-21_dot_indicator.gif)

***

####如果您遇到popup的导入失败，请在工程的gradle（不是app的gradle哦）加上这句话：

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

本项目于2016/12/01 全面覆盖master分支

本项目于2016/10/26 全面重构

本项目于2016/03/17 更改为mvp架构



本项目在更新的同时也会同步发布经验心得（撸代码的过程），不过因为在下才学疏浅，有些地方肯定做的不好，所以衷心希望如果您有好的建议或优化方案，可以留下脚印。

##LICENSE：
***
The MIT License (MIT)

Copyright (c) [2016] [razerdp]

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


