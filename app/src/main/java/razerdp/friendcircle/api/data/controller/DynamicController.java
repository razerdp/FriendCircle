package razerdp.friendcircle.api.data.controller;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import razerdp.friendcircle.api.interfaces.BaseResponseListener;
import razerdp.friendcircle.api.network.base.BaseResponse;
import razerdp.friendcircle.api.network.request.DynamicAddPraiseRequest;
import razerdp.friendcircle.api.network.request.RequestType;
import razerdp.friendcircle.utils.ToastUtils;

/**
 * Created by 大灯泡 on 2016/3/8.
 * 事件控制类
 */
public class DynamicController implements BaseResponseListener, BaseDynamicController {
    private static final String TAG = "DynamicController";
    private CallBack mCallBack;
    private Activity mContext;
    //=============================================================request
    private DynamicAddPraiseRequest mDynamicAddPraiseRequest;

    public DynamicController(Activity context, @NonNull CallBack callBack) {
        mContext = context;
        mCallBack = callBack;
    }

    //=============================================================request
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
            switch (response.getRequestType()) {
                case RequestType.ADD_PRAISE:
                    if (mCallBack != null) mCallBack.onResultCallBack(response);
                    break;
                case RequestType.CANCEL_PRAISE:
                    if (mCallBack != null) mCallBack.onResultCallBack(response);
                    break;
                default:
                    break;
            }
        }
        else {
            ToastUtils.ToastMessage(mContext, response.getErrorMsg());
        }
    }

    public void destroyController() {
        mDynamicAddPraiseRequest = null;
        mCallBack = null;
    }

    @Override
    public void addPraise(long userid, long dynamicid, Object obj, @RequestType.DynamicRequestType int requesttype) {
        if (mDynamicAddPraiseRequest == null) {
            mDynamicAddPraiseRequest = new DynamicAddPraiseRequest();
            mDynamicAddPraiseRequest.setOnResponseListener(this);
            mDynamicAddPraiseRequest.setRequestType(requesttype);
        }
        mDynamicAddPraiseRequest.userid = userid;
        mDynamicAddPraiseRequest.dynamicid = dynamicid;
        mDynamicAddPraiseRequest.execute();
    }

    public interface CallBack {
        void onResultCallBack(BaseResponse response);
    }
}
