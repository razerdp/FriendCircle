package razerdp.friendcircle.app.config;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by 大灯泡 on 2016/3/6.
 * 各种设定值，常量
 */
public class CommonValue {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NOT_PRAISE,HAS_PRAISE})
    public @interface PraiseState{}
    // 点赞状态
    public static final int NOT_PRAISE = 0;
    public static final int HAS_PRAISE = 1;

    // 评论类型
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ COMMENT_FOR_DYNAMIC, COMMENT_FOR_USER })
    public @interface CommentType {}
    //针对动态的评论
    public static final int COMMENT_FOR_DYNAMIC = 3;
    //针对评论的评论（回复）
    public static final int COMMENT_FOR_USER = 4;
}