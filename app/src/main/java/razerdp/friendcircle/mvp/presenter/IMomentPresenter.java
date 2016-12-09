package razerdp.friendcircle.mvp.presenter;

import java.util.List;

import razerdp.friendcircle.mvp.model.entity.CommentInfo;
import razerdp.friendcircle.mvp.model.entity.UserInfo;
import razerdp.friendcircle.mvp.view.IMomentView;

/**
 * Created by 大灯泡 on 2016/12/7.
 */

public interface IMomentPresenter extends IBasePresenter<IMomentView> {


    void addLike(int viewHolderPos, String momentid, List<UserInfo> currentLikeUserList);

    void unLike(int viewHolderPos, String momentid, List<UserInfo> currentLikeUserList);

    void addComment(int viewHolderPos, String momentid, String replyUserid, String commentContent, List<CommentInfo> currentCommentList);

    void removeComment(int viewHolderPos, String commentid, List<CommentInfo> currentCommentList);

}
