package razerdp.friendcircle.app.config;

/**
 * Created by 大灯泡 on 2016/2/16.
 * 朋友圈类型，跟服务器需要保持一致
 */
public class DynamicType {

    //纯文字动态值为：【10】
    //图文动态值为：【11】

    public static final int TYPE_ONLY_CHAR = 10;
    public static final int TYPE_WITH_IMG = 11;
    public static final int TYPE_VIDEO = 12;
    public static final int TYPE_SHARE_WEB = 13;
    public static final int TYPE_AD = 14;

    //仅仅只有一张图片（本地进行对应设置，非服务器下发）
    //weblink:http://www.jianshu.com/p/885538a261ea
    public static final int TYPE_IMG_SINGLE=9;
}
