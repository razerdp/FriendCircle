# 更新日志

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

