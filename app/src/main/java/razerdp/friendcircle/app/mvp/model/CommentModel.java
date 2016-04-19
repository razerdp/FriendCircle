package razerdp.friendcircle.app.mvp.model;

/**
 * Created by 大灯泡 on 2016/4/19.
 * model - 评论接口
 */
public interface CommentModel {

    //添加评论
    void addComment(int currentDynamicPos,long dynamicid,long userid,long replyid,String content);
}
