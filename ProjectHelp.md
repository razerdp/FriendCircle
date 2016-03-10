#项目帮助文件

###欢迎来到本项目，这个项目是一个尝试性项目，目的在于从无到有撸出一个微信朋友圈。

####这个文件意在简单的描述一下本项目的结构和各个包的用途

----------


#####本项目总体结构如下：


 - app：存放全局性/抽象性等父类，通常是一些提供给子类的api，接口等
 - ui：存放界面相关类，acitivity/fragment等
 - utils：存放各个工具类
 - widget：存放各个控件，包括自定义控件或继承一些第三方库扩展后的控件


#####本项目具体结构如下：

 - **app**:
	+ `adapter`：存放封装后的adapter，包括朋友圈专用的adapter和一般AdapterView使用的adapter
		* adapter.viewholder：存放朋友圈专用的接口化的item（实质上就是viewholder）和一般AdapterView使用viewholder封装

	+ `config`：存放配置常量，基本账号信息，EventBus所使用的方法类等
		* config.ptrwidget：存放朋友圈下拉刷新控件的模式/状态配置

	+ `controller`：用于分离朋友圈BaseItemDelegate的交互事件处理，防止BaseItemDelegate代码量过多，同时将BaseItemDelegate与Activity解耦（类似于MVP）
	+ `data`：存放各种数据，如JSON解析的实体类或数据库（暂时不需要数据库）
	+ `https`：存放针对Volley封装的类以及抽象化的请求基类
		* https.base：网络访问的基类
		* https.request：继承基类实现的具体请求类

	+ `interfaces`：存放一些回掉接口

- **ui**：
	+ `activity`：存放activity
	+ `adapteritem`：存放朋友圈专用的viewholder（继承BaseItemDelegate）
- **utils**：略
- **widget**：
	+ `commentwidget`：朋友圈评论TextView控件
	+ `popup`：存放使用到的悬浮窗
		* popup.base：存放抽象出来的悬浮窗基类
	+ `praisewidget`：朋友圈点赞列表显示控件
	+ `ptrwidget`：朋友圈下拉刷新控件（包含滑动到底部加载更多）
	+ 其他：
		* CircleImageView：github上使用最多的圆形头像，此处继承了SuperImageView实现Glide封装
		* ClickShowMoreLayout：继承LinearLayout实现朋友圈点击查看全文的效果
		* CustomImageSpan：垂直居中的ImageSpan
		* NoScrollGridView：用于在ListView中嵌套并正常显示的GridView
		* SpannableStringBuilderAllVer：抽取API 21之后添加的一个方法，使之支持API 21前。
		* SuperImageView：简单封装了Glide的ImageView


本项目所有的实现步骤都在[简书](http://www.jianshu.com/notebooks/3224048/latest)有所记载，如果有什么疑问，可以先到简书查阅相关文章，如果依然有疑问，可以添加我的QQ:164701463一起讨论或者直接提交PR，在下十分欢迎哦-V-，因为我们是“一起撸个朋友圈”对吧。

最后，因为在下是16届毕业的学生，在写这个项目的时候，安卓的经验实际上只有一年，因为忙于毕业设计，同时经验不足，所以代码写得可能有点混乱，而且有些地方思考不周，所以在下十分诚恳的希望您能够提交一下建议或者PR，我会尽我努力将这个项目弄得更加的完美。

###谢谢您的阅读以及支持