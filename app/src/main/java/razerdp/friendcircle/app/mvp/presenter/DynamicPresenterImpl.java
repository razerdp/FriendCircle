package razerdp.friendcircle.app.mvp.presenter;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
import razerdp.friendcircle.app.config.CommonValue;
import razerdp.friendcircle.app.config.LocalHostInfo;
import razerdp.friendcircle.app.https.base.BaseResponse;
import razerdp.friendcircle.app.https.request.RequestType;
import razerdp.friendcircle.app.interfaces.DynamicResultCallBack;
import razerdp.friendcircle.app.mvp.model.entity.CommentInfo;
import razerdp.friendcircle.app.mvp.model.entity.DynamicInfo;
import razerdp.friendcircle.app.mvp.model.entity.UserInfo;
import razerdp.friendcircle.app.mvp.model.impl.DynamicModelImpl;
import razerdp.friendcircle.app.mvp.view.DynamicView;
import razerdp.friendcircle.widget.commentwidget.CommentWidget;

/**
 * Created by 大灯泡 on 2016/3/17.
 * mvp - 引导层
 * 用于接收view的操作命令分发到model层实现
 */
public class DynamicPresenterImpl implements DynamicResultCallBack {
    private DynamicModelImpl mModel;
    private DynamicView mView;

    public DynamicPresenterImpl(DynamicView view) {
        mView = view;
        mModel = new DynamicModelImpl(this);
    }

    // 点赞
    public void addPraise(int curDynamicPos, long dynamicId) {
        mModel.addPraise(curDynamicPos, LocalHostInfo.INSTANCE.getHostId(), dynamicId);
    }

    // 取消赞
    public void cancelPraise(int curDynamicPos, long dynamicId) {
        mModel.cancelPraise(curDynamicPos, LocalHostInfo.INSTANCE.getHostId(), dynamicId);
    }

    // 添加评论
    public void addComment(int currentDynamicPos, long dynamicid, long userid, long replyid, String content){
        mModel.addComment(currentDynamicPos,dynamicid,userid,replyid,content);
    }

    //=============================================================
    // 展示输入框
    public void showInputBox(int currentDynamicPos, CommentWidget commentWidget, DynamicInfo dynamicInfo){
        mView.showInputBox(currentDynamicPos,commentWidget,dynamicInfo);
    }


    // 跳转到图片展示
    public void shoPhoto(@NonNull ArrayList<String> photoAddress, @NonNull ArrayList<Rect> originViewBounds, int
            curSelectedPos){
        mView.showPhoto(photoAddress,originViewBounds,curSelectedPos);
    }

    @Override
    public void onResultCallBack(BaseResponse response) {
        if (mView != null) {
            final int curDynamicPos = (int) response.getData();
            switch (response.getRequestType()) {
                case RequestType.ADD_PRAISE:
                    List<UserInfo> praiseList = (List<UserInfo>) response.getDatas();
                    mView.refreshPraiseData(curDynamicPos, CommonValue.HAS_PRAISE, praiseList);
                    break;
                case RequestType.CANCEL_PRAISE:
                    List<UserInfo> praiseList2 = (List<UserInfo>) response.getDatas();
                    mView.refreshPraiseData(curDynamicPos, CommonValue.NOT_PRAISE, praiseList2);
                    break;
                case RequestType.ADD_COMMENT:
                    List<CommentInfo> commentInfos= (List<CommentInfo>) response.getDatas();
                    mView.refreshCommentData(curDynamicPos,commentInfos);
                    break;
            }
        }
    }
}
