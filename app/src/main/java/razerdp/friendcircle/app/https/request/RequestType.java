package razerdp.friendcircle.app.https.request;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.PublicKey;

/**
 * Created by 大灯泡 on 2016/3/9.
 * 请求类型识别码
 */
public class RequestType {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ADD_PRAISE,CANCEL_PRAISE})
    public @interface PraiseRequestType {}
    // 点赞
    public static final int ADD_PRAISE=0x10;
    public static final int CANCEL_PRAISE=0x11;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ADD_COMMENT,DEL_COMMENT})
    public @interface CommentRequestType{}
    // 评论
    public static final int ADD_COMMENT=0X12;
    public static final int DEL_COMMENT=0x13;


}
