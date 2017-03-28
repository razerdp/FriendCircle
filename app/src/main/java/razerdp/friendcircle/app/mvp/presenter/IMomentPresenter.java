package razerdp.friendcircle.app.mvp.presenter;

import java.util.List;

import razerdp.friendcircle.app.mvp.model.entity.CommentInfo;
import razerdp.friendcircle.app.mvp.model.entity.LikesInfo;
import razerdp.friendcircle.app.mvp.model.entity.UserInfo;
import razerdp.friendcircle.app.mvp.view.IMomentView;
import razerdp.github.com.baselibrary.mvp.IBasePresenter;

/**
 * Created by 大灯泡 on 2016/12/7.
 */

public interface IMomentPresenter extends IBasePresenter<IMomentView> {


    void addLike(int viewHolderPos, String momentid, List<LikesInfo> currentLikeList);

    void unLike(int viewHolderPos, String momentid, List<LikesInfo> currentLikeList);

    void addComment(int viewHolderPos, String momentid, String replyUserid, String commentContent, List<CommentInfo> currentCommentList);

    void deleteComment(int viewHolderPos, String commentid, List<CommentInfo> currentCommentList);

}
