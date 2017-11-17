package razerdp.friendcircle.app.mvp.callback;

/**
 * Created by 大灯泡 on 2016/12/7.
 * <p>
 * 点赞callback
 */

public interface OnLikeChangeCallback {

    void onLike(String likeinfoid);

    void onUnLike();

}
