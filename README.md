# FriendCircle
## 一起来撸个朋友圈吧(重构版)

欢迎来到本项目，这个项目是一个尝试性项目，目的在于从无到有撸出一个微信朋友圈。</br>

ps:所有最新进度基本都会先push到[main-dev](https://github.com/razerdp/FriendCircle/tree/main-dev)分支哦

##### 【简略更新日志】
 - 2017/03/23
    + 开始photoselect的初步搭建
    + baselib的本地图片扫面完成

 - 2017/03/22
    + 组件化大致完成，目前的结构将会改为如下结构：
        - app：即工程主入口
        - baselib:主要的library，公用工具，base方法等
        - baseuilib:主要是自定义控件存放，baseadapter等
        - network:封装bmob的网络依赖
        - photoview：本来可以放入baseuilib的，但人家有开源协议，所以要遵守哦，因此一整个module保留
        - photoselect:图片选择模块，这个模块是目前的工作进程，可以单独运行该模块，但要记得去`gradle.properties`把`isModule`设置为true哦

    + 下一步，将会是继续开发朋友圈啦啦啦~应该会是做这个图片选择模块吧，做完后就是到发布模块了

 - 2017/03/16
    + 啦啦啦~又是一个大重构了咯
    + 组件化，lib拆分

 - 2017/03/15
    + fix [#42](https://github.com/razerdp/FriendCircle/issues/42)
    + 修复RecyclerView没有滚动条的问题。
    + RecyclerView的HeaderViewAdapter单独抽出。

 - 更多日志请看 → [更新日志](https://github.com/razerdp/FriendCircle/blob/master/UPDATE_LOG.md)

---

**本项目的一切实现思路以及逻辑都有记录，它们都有在我的简书文集记载：**

[代码实现逻辑（简书合集）](http://www.jianshu.com/notebooks/3224048/latest)


## 2017-02-08
重构后的朋友圈图片浏览过渡动画完成！（代码解析可能要等上几天）

代码解析地址：

待写

图片预览：

![](https://github.com/razerdp/FriendCirclePreview/blob/master/img/2017-02-08%E5%9B%BE%E7%89%87%E9%80%80%E5%87%BA%E5%8A%A8%E7%94%BB.gif)

***

#### 如果您遇到popup的导入失败，请在工程的gradle（不是app的gradle哦）加上这句话：

```xml
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```


##### 关于作者我:
普普通通的安卓开发者一枚。

##### 关于项目：
本项目意在按照我的经验弄出一个微信朋友圈，也许无法完全实现，但高仿应该是没问题的，只是时间问题←_←。

本项目于2016/12/01 全面覆盖master分支

本项目于2016/10/26 全面重构

本项目于2016/03/17 更改为mvp架构



本项目在更新的同时也会同步发布经验心得（撸代码的过程），不过因为在下才学疏浅，有些地方肯定做的不好，所以衷心希望如果您有好的建议或优化方案，可以留下脚印。


如果您愿意捐助一下项目，可以通过微信捐助哟~

![](https://github.com/razerdp/FriendCircle/blob/main-dev/wechat.jpg)



## LICENSE：
***
[MIT](https://github.com/razerdp/FriendCircle/blob/master/LICENSE)
