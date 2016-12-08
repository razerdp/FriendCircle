package razerdp.friendcircle.mvp.view;

import java.util.List;

import razerdp.friendcircle.mvp.model.entity.UserInfo;
import razerdp.friendcircle.widget.commentwidget.CommentWidget;

/**
 * Created by 大灯泡 on 2016/12/7.
 */

public interface IMomentView extends IBaseView{


    void onLikeChange(int itemPos, List<UserInfo> likeUserList);


    void showCommentBox();

}
