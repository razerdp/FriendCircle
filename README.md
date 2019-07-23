FriendCircle
---

### 一起来撸个朋友圈吧(重构版)

[**apk下载**](http://bmob-cdn-14711.b0.upaiyun.com/2019/02/26/f1ffbe4240103da480d47cbbfe113d94.apk)


欢迎来到本项目，这个项目是一个尝试性项目，目的在于从无到有撸出一个微信朋友圈。</br>


### 本项目将会进行第三次重构：

重构内容如下：

  - 整个项目框架重构
  - 组件化更换
  - 控件更换

同时，跟以往不同的是，本项目的重构将会在B站直播。。。直播时间不定，一般晚上10点左右直播。

如果感兴趣可以来直播页看看~

[https://live.bilibili.com/724896](https://live.bilibili.com/724896)



【简略更新日志】
---

* 2019/07/23
  * 准备开始第三轮重构

* 2019/04/2
  * 修复评论对齐问题
  * 修复8.0或以上系统浏览大图崩溃的问题
  * 优化选图操作

* 2019/02/26
  * 修复因替换为沉浸式问题导致的评论对齐失效问题

* 2019/01/16
  * 沉浸式完成，titlebar渐变完成
  * 效果图：
  * ![](https://github.com/razerdp/Pics/blob/master/FirendCircle/demo.gif)

* 2019/01/15
  * UI按照新的朋友圈修改
  * 修改下拉刷新的icon逻辑，去掉ViewOffsetHelper
  * 按计划恢复功能

* 2018/9/27
  * fixed #79
  * 近期因为个人工作问题，暂时不更新，。。

* 2018/8/7
  * 修复多图发布无法移除图片问题  #62 (https://github.com/razerdp/FriendCircle/issues/62)
  * 适配Android O
  * 展开评论暂时没有完成，因此暂时禁用。
  

*  ...more

* 更多日志请看 → [更新日志](https://github.com/razerdp/FriendCircle/blob/master/UPDATE_LOG.md)

关于思路
---
**本项目的一切实现思路以及逻辑都有记录，它们都有在我的简书文集记载：自4月份起停止更新，如果您感兴趣，可以加群一起探讨（590777418）**

[代码实现逻辑（简书合集）](http://www.jianshu.com/notebooks/3224048/latest)


【最新进度：发布动态】
---

* 2017-10-24
 * 发布页面完成（图片选择）

![](https://github.com/razerdp/FriendCirclePreview/blob/master/img/device-2017-10-24.gif)

***

关于作者我
---

普普通通的安卓开发者一枚。

关于项目
---

本项目意在按照我的经验弄出一个微信朋友圈，也许无法完全实现，但高仿应该是没问题的，只是时间问题←_←。

**如果您需要bmob的后台数据查看，可以提交issue，留下用户名或者邮箱，我拉你进团队管理（无修改权限）**

**因为采取组件化，所以可能push上来的是组件的build，如果您要build整个app，请把gradle.properties下的  isModule=true 切换为false并sync gradle**


如果您感兴趣，欢迎加群一起讨论，本群新创建，暂时没有什么特别的要求哈，大家都是因为兴趣-V-

![](https://github.com/razerdp/FriendCircle/blob/master/qqgroup.png)



如果您愿意捐助一下项目，可以通过微信/支付宝捐助哟~

|微信         | 支付宝           | 
| ------------- |:-------------:| 
| ![](https://github.com/razerdp/FriendCircle/blob/master/wechat.png)      | ![](https://github.com/razerdp/FriendCircle/blob/master/alipay.png) |




LICENSE
---

[GPL3.0](https://github.com/razerdp/FriendCircle/blob/master/LICENSE)
