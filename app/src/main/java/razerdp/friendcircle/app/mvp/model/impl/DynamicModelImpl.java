package razerdp.friendcircle.app.mvp.model.impl;

import razerdp.friendcircle.app.https.base.BaseResponse;
import razerdp.friendcircle.app.https.request.DynamicAddPraiseRequest;
import razerdp.friendcircle.app.https.request.DynamicCancelPraiseRequest;
import razerdp.friendcircle.app.https.request.RequestType;
import razerdp.friendcircle.app.interfaces.BaseResponseListener;
import razerdp.friendcircle.app.interfaces.DynamicResultCallBack;
import razerdp.friendcircle.app.mvp.model.PraiseModel;

/**
 * Created by 大灯泡 on 2016/3/17.
 * mvp - model
 * 复杂逻辑的操作/请求/耗时等，处理后的数据提供
 *
 * 若过于复杂的操作，可能需要接口来松耦合
 */
public class DynamicModelImpl implements BaseResponseListener, PraiseModel {
    private DynamicResultCallBack callBack;

    public DynamicModelImpl(DynamicResultCallBack callBack) {
        this.callBack = callBack;
    }

    //=============================================================request
    private DynamicAddPraiseRequest mDynamicAddPraiseRequest;
    private DynamicCancelPraiseRequest mDynamicCancelPraiseRequest;

    //=============================================================public methods
    @Override
    public void addPraise(int currentDynamicPos, long userid, long dynamicid) {
        if (mDynamicAddPraiseRequest == null) {
            mDynamicAddPraiseRequest = new DynamicAddPraiseRequest();
            mDynamicAddPraiseRequest.setOnResponseListener(this);
            mDynamicAddPraiseRequest.setRequestType(RequestType.ADD_PRAISE);
        }
        mDynamicAddPraiseRequest.setCurrentDynamicPos(currentDynamicPos);
        mDynamicAddPraiseRequest.userid = userid;
        mDynamicAddPraiseRequest.dynamicid = dynamicid;
        mDynamicAddPraiseRequest.execute();
    }

    @Override
    public void cancelPraise(int currentDynamicPos, long userid, long dynamicid) {
        if (mDynamicCancelPraiseRequest == null) {
            mDynamicCancelPraiseRequest = new DynamicCancelPraiseRequest();
            mDynamicCancelPraiseRequest.setOnResponseListener(this);
            mDynamicCancelPraiseRequest.setRequestType(RequestType.CANCEL_PRAISE);
        }
        mDynamicCancelPraiseRequest.setCurrentDynamicPos(currentDynamicPos);
        mDynamicCancelPraiseRequest.userid = userid;
        mDynamicCancelPraiseRequest.dynamicid = dynamicid;
        mDynamicCancelPraiseRequest.execute();
    }

    //=============================================================request methods
    @Override
    public void onStart(BaseResponse response) {

    }

    @Override
    public void onStop(BaseResponse response) {

    }

    @Override
    public void onFailure(BaseResponse response) {

    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response.getStatus() == 200) {
            if (callBack != null) {
                callBack.onResultCallBack(response);
            }
        }
    }
}
