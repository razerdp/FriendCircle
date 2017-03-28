package razerdp.friendcircle.app.mvp.view;

import java.util.List;

import razerdp.friendcircle.app.mvp.model.entity.CommentInfo;
import razerdp.friendcircle.app.mvp.model.entity.LikesInfo;
import razerdp.friendcircle.app.mvp.model.entity.UserInfo;
import razerdp.github.com.baselibrary.mvp.IBaseView;
import razerdp.friendcircle.ui.widget.commentwidget.CommentWidget;

/**
 * Created by 大灯泡 on 2016/12/7.
 */

public interface IMomentView extends IBaseView {


    void onLikeChange(int itemPos, List<LikesInfo> likeUserList);


    void onCommentChange(int itemPos, List<CommentInfo> commentInfoList);

    void showCommentBox(int itemPos, String momentid, CommentWidget commentWidget);

}
