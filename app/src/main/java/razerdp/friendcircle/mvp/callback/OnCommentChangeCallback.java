package razerdp.friendcircle.mvp.callback;

import razerdp.friendcircle.mvp.model.entity.CommentInfo;

/**
 * Created by 大灯泡 on 2016/12/9.
 * <p>
 * 评论Callback
 */

public interface OnCommentChangeCallback {

    void onAddComment(CommentInfo response);

    void onRemoveComment(CommentInfo response);

}
