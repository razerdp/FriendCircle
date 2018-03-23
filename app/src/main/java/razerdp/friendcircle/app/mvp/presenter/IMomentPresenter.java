package razerdp.friendcircle.app.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.razerdp.github.com.common.entity.CommentInfo;
import com.razerdp.github.com.common.entity.LikesInfo;
import com.razerdp.github.com.common.entity.MomentsInfo;

import java.util.List;

import razerdp.friendcircle.app.mvp.view.IMomentView;
import razerdp.github.com.lib.mvp.IBasePresenter;


/**
 * Created by 大灯泡 on 2016/12/7.
 */

public interface IMomentPresenter extends IBasePresenter<IMomentView> {


    void addLike(int viewHolderPos, String momentid, List<LikesInfo> currentLikeList);

    void unLike(int viewHolderPos, String likesid, List<LikesInfo> currentLikeList);

    void addComment(int viewHolderPos, String momentid, String replyUserid, String commentContent, List<CommentInfo> currentCommentList);

    void deleteComment(int viewHolderPos, String commentid, List<CommentInfo> currentCommentList);

    void deleteMoments(Context context,@NonNull MomentsInfo momentsInfo);

}
