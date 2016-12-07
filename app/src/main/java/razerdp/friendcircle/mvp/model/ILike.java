package razerdp.friendcircle.mvp.model;

import razerdp.friendcircle.mvp.callback.OnLikeChangeCallback;

/**
 * Created by 大灯泡 on 2016/12/6.
 */

public interface ILike {


    /**
     * 添加点赞
     */
    void addLike(String momentid, OnLikeChangeCallback onLikeChangeCallback);

    /**
     * 移除点赞
     */
    void unLike();
}
