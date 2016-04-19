# 更新日志

##2016-04-19
评论功能与后台交互补充</br>

***

##2016-04-14
朋友圈图片浏览模块完成</br>
代码解析地址：</br>
http://www.jianshu.com/p/4c5b5d7dc856 </br>
图片预览：</br>
![](https://github.com/razerdp/FriendCircle/blob/main-dev/img/2016-04-14_friend_circle_photo_gallery.gif)

***

##2016-04-13
图片点击前景色</br>
代码解析地址：</br>
http://www.jianshu.com/p/8984efce40ae </br>
图片预览：</br>
![](https://github.com/razerdp/FriendCircle/blob/main-dev/img/2016-04-13_click_forceground_iv.gif)

***

##2016-04-04
评论模块对齐完成（针对回复评论）</br>
代码解析地址：</br>
http://www.jianshu.com/p/513e2eccd7a8 </br>
图片预览：</br>
![](https://github.com/razerdp/FriendCircle/blob/main-dev/img/2016-04-04_comment_align_input_box.gif)

***

##2016-03-26
修复点赞列表展示的背景selector</br>
代码解析地址：</br>
http://www.jianshu.com/p/69b9a6a83373 </br>

***
##2016-03-25
评论模块对齐完成（针对动态评论）</br>
代码解析地址：</br>
http://www.jianshu.com/p/8d24f9b7a63a </br>
图片预览：</br>
![](https://github.com/razerdp/FriendCircle/blob/main-dev/img/2016-03-25_show_input_box.gif)

***

##2016-03-18
修复加载更多时请求成功后没有重置状态的问题

***
##2016-03-17
更改为mvp架构

***

##2016-03-11
点赞模块完成</br>
代码解析地址：</br>
http://www.jianshu.com/p/deda1849a084 </br>
图片预览：</br>
![](https://github.com/razerdp/FriendCircle/blob/main-dev/img/2016-03-11_praise.gif)

***

##2016-03-08
评论弹出popup</br>
代码解析地址：</br>
http://www.jianshu.com/p/15a9fe8f917f </br>
图片预览：</br>
![](https://github.com/razerdp/FriendCircle/blob/main-dev/img/2016-03-08_comment_popup.gif)

***

##2016-03-01
内容页 - 网页分享

***
##2016-02-28
内容页 - 图文（GridView）</br>
代码解析地址：</br>
http://www.jianshu.com/p/3d0cc6882e1a </br>
图片预览：</br>
![](http://upload-images.jianshu.io/upload_images/684042-114feffc2a7be669.gif)

***
##2016-02-27
内容页 - 图文（单张）</br>
代码解析地址：</br>
http://www.jianshu.com/p/885538a261ea </br>
图片预览：</br>
![](https://github.com/razerdp/FriendCircle/blob/main-dev/img/single_image.gif)

***
##2016-02-26~27
评论区控件采用池，进一步优化。</br>
代码解析地址：</br>
http://www.jianshu.com/p/ff9788581fb0 </br>
图片预览：</br>
![](https://github.com/razerdp/FriendCircle/blob/main-dev/img/comment_text_pool.gif)

***
##2016-02-25
服务器就绪，暂时合并控件</br>
代码解析地址：</br>
http://www.jianshu.com/p/4cc3f9c8a713 </br>
图片预览：</br>
![](https://github.com/razerdp/FriendCircle/blob/main-dev/img/2016-02-25_test.gif)

***
##2016-02-23
评论列表控件
等待服务器就绪。现在暂时无法展示。

***
##2016-02-22
点赞列表控件</br>
代码解析地址：</br>
http://www.jianshu.com/p/26dd3aad965a </br>
图片预览：</br>
![](https://github.com/razerdp/FriendCircle/blob/master/img/praise_widget.gif)

***
##2016-02-20
点击展开全文控件实现</br>
代码解析地址：</br>
http://www.jianshu.com/p/80d7f34c5f08 </br>
图片预览：</br>
![](https://github.com/razerdp/FriendCircle/blob/master/img/click_to_show_more.gif)

***
##2016-02-19
布局上传，下一步实现文字更多的控件

***
##2016-02-16
大致adapter和baseitem的封装

***
##2016-02-15
关于icon使用margintop来更新是否会重复导致measure和layout的问题
在setAdapter后发现采用relativelayout的话在不断的改变margin时会导致多次测量（如果布局复杂，将会导致测量时间较长，在视觉上表现为掉帧），现改正布局根节点为FrameLayout，多次测量消失。

***
##2016-02-15
采取论坛评论区兄弟的建议，使用valueAnimator代替smoothChangeThread，源代码保留。

***
##2016-02-11
下拉刷新/上拉加载更多 listview定制完成，假数据测试成功，等待下一步执行。

***
##2016-02-10
实现了上拉加载更多</br>
代码解析地址：</br>
下篇：http://www.jianshu.com/p/68e13214cde4 </br>
图片预览：</br>
![](https://github.com/razerdp/FriendCircle/blob/master/img/2016-02-10%20%E5%8A%A0%E8%BD%BD%E6%9B%B4%E5%A4%9A.gif)

***
##2016-02-10
初步实现了朋友圈下拉刷新控件（上拉加载未实现）</br>
代码解析地址：</br>
上篇：http://www.jianshu.com/p/7fa237cfddbb </br>
中篇：http://www.jianshu.com/p/94e1e267b3b3 </br>
图片预览：</br>
![](https://github.com/razerdp/FriendCircle/blob/master/img/2016-02-10%20%E4%B8%8B%E6%8B%89%E5%88%B7%E6%96%B0.gif)

