package razerdp.friendcircle.app.mvp.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.razerdp.github.com.common.entity.CommentInfo;
import com.razerdp.github.com.common.entity.LikesInfo;
import com.razerdp.github.com.common.entity.MomentsInfo;

import java.util.List;

import razerdp.github.com.lib.mvp.IBaseView;
import razerdp.github.com.ui.widget.commentwidget.CommentWidget;


/**
 * Created by 大灯泡 on 2016/12/7.
 */

public interface IMomentView extends IBaseView {

    void onLikeChange(int itemPos, List<LikesInfo> likeUserList);

    void onCommentChange(int itemPos, List<CommentInfo> commentInfoList);

    /**
     * 因为recyclerview通过位置找到itemview有可能会找不到对应的View，所以这次直接传值
     *
     * @param viewHolderRootView
     * @param itemPos
     * @param momentid
     * @param commentWidget
     */
    void showCommentBox(@Nullable View viewHolderRootView, int itemPos, String momentid, CommentWidget commentWidget);

    void onDeleteMomentsInfo(@NonNull MomentsInfo momentsInfo);

}
