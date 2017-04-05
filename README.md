# FriendCircle
## 一起来撸个朋友圈吧(重构版)

欢迎来到本项目，这个项目是一个尝试性项目，目的在于从无到有撸出一个微信朋友圈。</br>

ps:所有最新进度基本都会先push到[main-dev](https://github.com/razerdp/FriendCircle/tree/main-dev)分支哦

##### 因为采取组件化（目前正在开发的组件：photoselect），所以可能push上来的是组件的build，如果您要build整个app，请把gradle.properties下的  isModule=true 切换为false并sync gradle

##### 【简略更新日志】
 - 2017/04/05
    + 修复ARouter失效的问题
    + 图片预览页面实现

 - 2017/03/30
    + 修复LocalPhotoManager监听媒体库更新时插入过多数据的问题。
    + 优化gridphotofragment和图片浏览的逻辑
    + 增加图片预览activity，功能慢慢完善

 - 2017/03/29
    + 图片选择模块（包括相册部分）完成
    + 图片选择模块封入fragment

 - 2017/03/28
    + 图片选择模块完成
    + 动画交互完成
    + 添加gif预览
    + 重构Bmob的点赞表等，现在查询所有动态已经是不再有那么多请求了，fix #38
    + 图片选择模块合并到app中

 - 2017/03/24
    + 扫描系统媒体库Manager完成
    + 图片选择页面的扫描进度完成
    + 增加媒体库图片更新的监听

 - 2017/03/23
    + 开始photoselect的初步搭建
    + baselib的本地图片扫面完成

 - 更多日志请看 → [更新日志](https://github.com/razerdp/FriendCircle/blob/master/UPDATE_LOG.md)

---

**本项目的一切实现思路以及逻辑都有记录，它们都有在我的简书文集记载：**

[代码实现逻辑（简书合集）](http://www.jianshu.com/notebooks/3224048/latest)


#### 【最新进度图片预览】
##### 2017-03-28

图片选择模块完成！包含了选择的动画，交互按照iOS的哦~

![](https://github.com/razerdp/FriendCirclePreview/blob/master/img/2017-03-28%20%E5%9B%BE%E7%89%87%E9%80%89%E6%8B%A9.gif)

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
