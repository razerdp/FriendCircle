package razerdp.friendcircle.api.data;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by 大灯泡 on 2016/3/6.
 * 各种设定值
 */
public class CommonValue {

    // 点赞状态
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NOT_PRAISE,HAS_PRAISE})
    public @interface PraiseState{}

    public static final int NOT_PRAISE = 0;
    public static final int HAS_PRAISE = 1;



}