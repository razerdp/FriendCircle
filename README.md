# FriendCircle
## 一起来撸个朋友圈吧(重构版)

欢迎来到本项目，这个项目是一个尝试性项目，目的在于从无到有撸出一个微信朋友圈。</br>

ps:所有最新进度基本都会先push到[main-dev](https://github.com/razerdp/FriendCircle/tree/main-dev)分支哦

##### 因为采取组件化（目前正在开发的组件：photoselect），所以可能push上来的是组件的build，如果您要build整个app，请把gradle.properties下的  isModule=true 切换为false并sync gradle

##### 【简略更新日志】
 - 2017/09/06
    + 切换许可为GPLV3.0，开始恢复维护
    + 修复了点击评论有时候无法定位到item的问题
    + 增加了权限帮助类、拍照帮助类等

 - 2017/06/08
    +  因为有点忙，打算8月份开始再继续维护。

 - 2017/04/13
    + 修复了LocalPhotoManager扫描媒体库偶尔会出现不全的问题（原来我竟然在回调后没有return，从而导致一直扫描下去orz...这是个大问题额
    + 修复了选择图片页面在进入预览里取消选择后，返回来再取消选择时数量不准确的问题 **(详见PhotoSelectAdapter#onUnSelectPhoto)**
    + 其实我偷偷的加了一个长按这个popup文字可以复制的功能（虽然没鸟用）

 - 2017/04/10
    + 修复了Bmob后台查询记录只有100条的截断问题
    + 增加查询缓存
    
 - 更多日志请看 → [更新日志](https://github.com/razerdp/FriendCircle/blob/master/UPDATE_LOG.md)

---

**本项目的一切实现思路以及逻辑都有记录，它们都有在我的简书文集记载：自4月份起停止更新，如果您感兴趣，可以加群一起探讨（590777418）**

[代码实现逻辑（简书合集）](http://www.jianshu.com/notebooks/3224048/latest)


#### 【最新进度图片预览并选择】
##### 2017-04-05

图片选择模块完成！

![](https://github.com/razerdp/FriendCirclePreview/blob/master/img/2017_04_05photo_select.gif)

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

如果您感兴趣，欢迎加群一起讨论，本群新创建，暂时没有什么特别的要求哈，大家都是因为兴趣-V-

![](https://github.com/razerdp/FriendCircle/blob/main-dev/qqgroup.png)



如果您愿意捐助一下项目，可以通过微信捐助哟~

![](https://github.com/razerdp/FriendCircle/blob/main-dev/wechat.jpg)



## LICENSE：
***
[GPL3.0](https://github.com/razerdp/FriendCircle/blob/master/LICENSE)
