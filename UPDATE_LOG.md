# 更新日志
## 2017/04/13
 -  修复了LocalPhotoManager扫描媒体库偶尔会出现不全的问题（原来我竟然在回调后没有return，从而导致一直扫描下去orz...这是个大问题额
 - 修复了选择图片页面在进入预览里取消选择后，返回来再取消选择时数量不准确的问题 **(详见PhotoSelectAdapter#onUnSelectPhoto)**
 - 其实我偷偷的加了一个长按这个popup文字可以复制的功能（虽然没鸟用）

## 2017/04/10
  - 修复了Bmob后台查询记录只有100条的截断问题
  - 增加查询缓存

## 017/04/05
  - 修复ARouter失效的问题
  - 图片预览页面实现
  - 选择后退出的保存即页面的更新

![](https://github.com/razerdp/FriendCirclePreview/blob/master/img/2017_04_05photo_select.gif)

## 2017/03/30
  - 修复LocalPhotoManager监听媒体库更新时插入过多数据的问题。
  - 优化gridphotofragment和图片浏览的逻辑
  - 增加图片预览activity，功能慢慢完善

## 2017-03-28
  - 图片选择模块完成！包含了选择的动画，交互按照iOS的哦~
  
![](https://github.com/razerdp/FriendCirclePreview/blob/master/img/2017-03-28%20%E5%9B%BE%E7%89%87%E9%80%89%E6%8B%A9.gif)


## 2017/03/24
  - 扫描系统媒体库Manager完成
  - 图片选择页面的扫描进度完成
  - 增加媒体库图片更新的监听
  - 重构Bmob的点赞表等，现在查询所有动态已经是不再有那么多请求了，fix #38

## 2017/03/23
  - 开始photoselect的初步搭建
  - baselib的本地图片扫面完成

## 2017/03/22
  - 组件化大致完成，目前的结构将会改为如下结构：
    + app：即工程主入口
    + baselib:主要的library，公用工具，base方法等
    + baseuilib:主要是自定义控件存放，baseadapter等
    + network:封装bmob的网络依赖
    + photoview：本来可以放入baseuilib的，但人家有开源协议，所以要遵守哦，因此一整个module保留
    + photoselect:图片选择模块，这个模块是目前的工作进程，可以单独运行该模块，但要记得去`gradle.properties`把`isModule`设置为true哦

  - 下一步，将会是继续开发朋友圈啦啦啦~应该会是做这个图片选择模块吧，做完后就是到发布模块了


## 2017/03/16
  - 组件化：
    + 公共工具类移动到baselib
    + ui组件类移动到baseuilib
    + 图片加载库封装移动到imagelib
    + photoview库不变
    + 采取全局Gradle配置，详见（config.gradle）

## 2017/03/15
  - fix [#42](https://github.com/razerdp/FriendCircle/issues/42)
  - 修复RecyclerView没有滚动条的问题。
  - RecyclerView的HeaderViewAdapter单独抽出。


## 2017/03/08
  - 解决浏览大图失败无法返回退出的问题[#41](https://github.com/razerdp/FriendCircle/issues/41)

## 2017/03/02
  - 增加popup

## 2017/03/01
  - UI微调
  - 发布朋友圈动态的activity预添加
  - TitleBarView稍作调整

## 2017/02/28
  - 优化刷新icon的逻辑
  - 双击回顶部刷新

## 2017/02/21
  - 标题栏整合到控件中
  - 预留发朋友圈的开关
  - 双击回到顶部

## 2017/02/13
  - 修复单图位置不准的问题

## 2017/02/09
  - 增加图片浏览的指示器

## 2017-02-08
16年底开发完《异次元通讯2》之后，就在想着如何解决这边朋友圈的问题了，很庆幸，17年回来第二个月就把这个难题解决了

诚然还是有着挺多的bug的，但起码第一步已经走出来了不是吗

图片的过渡动画这一个还原一做就做了两个月，虽然因为工作问题断断续续的，但也真的花费了不少时间

我不知道这个项目能否帮助到很多人，但对于我来说，每一次的重构，每一次的功能实现，都是一个进步

2017年，大家一起努力哦！

预览图：


![](https://github.com/razerdp/FriendCirclePreview/blob/master/img/2017-02-08%E5%9B%BE%E7%89%87%E9%80%80%E5%87%BA%E5%8A%A8%E7%94%BB.gif)


## 2016-12-23
图片的退场动画现在封装到GalleryPhotoView里面了，感谢bm-x photoview作者的开源项目：https://github.com/bm-x/PhotoView

本退场动画matrix思路参考的是他的项目，并进一步优化。

ps；退场归位动画还是存在无法完全对正的问题，这个问题应该会随着我对Matrix的逐渐深入而被解决，期望这一天来得快一点吧-V-

最后如果您感兴趣，我真的很欢迎您来项目一起讨论或者pull request


## 2016-12-16
 - 图片浏览（进场动画）
 - 准备着手增加指示器
 - TODO LIST:
    + 修复退场动画滑动过后动画执行不准确的问题
    + 修复退场动画结束之后，View覆盖到原来位置时图片出现的跳动问题

## 2016-12-15
 - 图片前景色
 - 准备着手图片浏览功能
 - 更新PhotoContents库

## 2016-12-14
 - 修复时间戳转换的问题，增加输入法消失的动画。
 - 评论框对齐优化算法完成。
 - 增加注册框框。

## 2016-12-13 初步完成评论定位，增加删除评论的方法和请求。
TODO LIST:
 - 优化评论定位。
 - 图片点击查看。

## 2016-12-09 实现评论功能，包括回复评论，UI方面暂时实现删除评论popup.
TODO LIST:
 - 删除评论请求。
 - 评论框自动定位。

## 2016-12-08 实现取消点赞的请求和UI方面的功能

## 2016-12-07 实现添加点赞的请求和UI方面的功能，待补取消点赞功能

## 2016-12-05 暂时实现popup，评论功能待补充.

## 2016-12-1 刷新的icon放弃使用offsetTopAndBottom，使用layout

## 2016-11-22 加载更多，同时修复wrapperAdapter的layourparams问题

## 2016-11-14 九宫格自定义控件实现

## 2016-11-3 朋友圈的adapter实现，viewholder的反射构造。。。果然我还是喜欢反射- -

## 2016-11-1 recyclerView的baseadapter和viewholder基类

## 2016-10-31 recyclerView添加头布局和尾布局方法

## 2016-10-30 recyclerview下拉刷新完成，icon联动实现

## 2016-10-28 增加查询朋友圈时间线的请求，增加添加数据的初始化类，添加了N条数据哦

## 2016-10-26 Bmob包导入，除了widget所有包删除，开始着手重构
