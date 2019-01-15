FriendCircle
---

### 一起来撸个朋友圈吧(重构版)

[**apk下载**](http://bmob-cdn-14711.b0.upaiyun.com/2018/08/07/54bf965c40a3c9eb800fe3023704f890.apk)


欢迎来到本项目，这个项目是一个尝试性项目，目的在于从无到有撸出一个微信朋友圈。</br>

>**来自项目作者（我）的一句话：**
>
> - 本项目后台采取`Bmob`作为后端支持，但因为这是一个开源的项目，并非商业盈利项目，因此使用的方案仅仅是免费版本
>
> **但是免费版本意味着不稳定以及限制，目前就有着每个月流量20G的限制。**
>
> 自从开发了发布朋友圈之后，很多开发者或者感兴趣的朋友不断的上传图片，上传重复的动态，甚至有着一条动态发布数十条之多。
>
> 这急剧的消耗着我后端的流量，要知道你的一条动态可不是只给你测试的啊，而是很多人一起共用的
>
> 一直以来我也有维护，虽然不是很经常，但是这个项目至少还远远没到心中的效果，所以会不断的优化/重构下去，只是服务器的压力我真的吃不消
>
> 所以拜托诸位，如果有闲钱，可以去打赏一下，我去买流量（0.3RMB/GB），如果不想打赏也没问题，但请不要不断的去发布动态，消耗不必要的流量，毕竟20G真的，很少很少，这个月消耗完了，就要等下个月了。
>
> 最后，如果您遇到图片加载失败，请不要担心，纯粹就是没流量而已。

【简略更新日志】
---

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
  
* 2018/3/29
  * 修复点击名字问题，优化spanEx（#65）https://github.com/razerdp/FriendCircle/issues/65
  * 继续编写可收缩/展开的评论layout

* 2018/3/23
  * 修复评论的问题。
  
* 2018/2/24
  * 删除朋友圈时也删除对应文件
  * 修复单张图片上传时无法压缩的问题
  * Glide升级到4.6.1

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
