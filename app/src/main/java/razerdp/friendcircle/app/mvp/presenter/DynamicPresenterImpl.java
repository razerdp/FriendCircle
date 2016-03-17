package razerdp.friendcircle.app.mvp.presenter;

import java.util.List;
import razerdp.friendcircle.app.config.CommonValue;
import razerdp.friendcircle.app.config.LocalHostInfo;
import razerdp.friendcircle.app.https.base.BaseResponse;
import razerdp.friendcircle.app.https.request.RequestType;
import razerdp.friendcircle.app.interfaces.DynamicResultCallBack;
import razerdp.friendcircle.app.mvp.model.entity.UserInfo;
import razerdp.friendcircle.app.mvp.model.impl.DynamicModelImpl;
import razerdp.friendcircle.app.mvp.view.DynamicView;

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
            }
        }
    }
}
