# FriendCircle
## 一起来撸个朋友圈吧(重构版)

欢迎来到本项目，这个项目是一个尝试性项目，目的在于从无到有撸出一个微信朋友圈。</br>

#### 在更新了Gradle 3.0后，可能会出现dexMerge错误，定位知道是bmob的sdk内含gson与gson2.8的冲突
#### 事实上暂时来说我不知道哪里引用了gson2.8（打印dependences Tree也找不到），所以如果出现这个情况，请删除bmob包下的gson-2.6.2.jar
>具体方法：
>  - 切换到Project标签
>  - 定位到External Libraries
>  - 找到cn.bmob.android:bmob-sdk-xxx，展开
>  - 找到gson-2.6.2.jar，右键delete即可】


##### 因为采取组件化，所以可能push上来的是组件的build，如果您要build整个app，请把gradle.properties下的  isModule=true 切换为false并sync gradle

##### 【简略更新日志】
 - 2017/10/24
    + 现在可以发布动态了哦~
    + 增加删除动态功能
    + 调整动态布局，看起来更和谐

 - 2017/10/12
    + 重新构建了选择图片的代码
    + 初步完成发布动态（仅完成选择图片）

 - 2017/09/07
    + 增加了权限兼容（到7.0）

 - ...more
    
 - 更多日志请看 → [更新日志](https://github.com/razerdp/FriendCircle/blob/master/UPDATE_LOG.md)

---

**本项目的一切实现思路以及逻辑都有记录，它们都有在我的简书文集记载：自4月份起停止更新，如果您感兴趣，可以加群一起探讨（590777418）**

[代码实现逻辑（简书合集）](http://www.jianshu.com/notebooks/3224048/latest)


#### 【最新进度：发布动态】
##### 2017-10-24

发布页面完成（图片选择）

![](https://github.com/razerdp/FriendCirclePreview/blob/master/img/device-2017-10-24.gif)

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

![](https://github.com/razerdp/FriendCircle/blob/master/qqgroup.png)



如果您愿意捐助一下项目，可以通过微信捐助哟~

![](https://github.com/razerdp/FriendCircle/blob/master/wechat.jpg)



## LICENSE：
***
[GPL3.0](https://github.com/razerdp/FriendCircle/blob/master/LICENSE)
