package razerdp.friendcircle.config;

/**
 * Created by 大灯泡 on 2016/10/28.
 * <p>
 * 朋友圈类型
 */

public interface MomentsType {
    //空内容，容错用
    int EMPTY_CONTENT = 0;
    //纯文字
    int TEXT_ONLY = 1;
    //多图
    int MULTI_IMAGES = 2;
    //单图
    int SINGLE_IMAGE = 3;
    //网页
    int WEB = 4;
    // TODO: 2016/10/29  增加视频类型，广告类型等
}
