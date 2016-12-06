package razerdp.friendcircle.mvp.model;

/**
 * Created by 大灯泡 on 2016/12/6.
 */

public interface LikeModel {


    /**
     * 添加点赞
     */
    void addLike(String momentid,String userid);

    /**
     * 移除点赞
     */
    void removeLike();
}
