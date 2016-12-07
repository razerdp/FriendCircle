package razerdp.friendcircle.mvp.callback;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import razerdp.friendcircle.config.Define;

/**
 * Created by 大灯泡 on 2016/12/7.
 * <p>
 * 点赞callback
 */

public interface OnLikeChangeCallback {

    void onLikeChange(@Define.LikeState int likeStae);

}
