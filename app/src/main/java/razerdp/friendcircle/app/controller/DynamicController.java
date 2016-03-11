package razerdp.friendcircle.app.controller;

import android.app.Activity;
import android.support.annotation.NonNull;
import razerdp.friendcircle.app.data.entity.MomentsInfo;
import razerdp.friendcircle.app.interfaces.BaseResponseListener;
import razerdp.friendcircle.app.https.base.BaseResponse;
import razerdp.friendcircle.app.https.request.DynamicAddPraiseRequest;
import razerdp.friendcircle.app.https.request.DynamicCancelPraiseRequest;
import razerdp.friendcircle.app.https.request.RequestType;
import razerdp.friendcircle.utils.ToastUtils;

/**
 * Created by 大灯泡 on 2016/3/8.
 * 事件控制器
 * 本控制器用于BaseItemDelegate的事件处理
 * 事件处理完成通过callback回调给activity，避免BaseItem与activity耦合度过高
 */
public class DynamicController implements BaseResponseListener, BaseDynamicController {
    private static final String TAG = "DynamicController";
    private CallBack mCallBack;
    private Activity mContext;
    //=============================================================request
    private DynamicAddPraiseRequest mDynamicAddPraiseRequest;
    private DynamicCancelPraiseRequest mDynamicCancelPraiseRequest;

    public DynamicController(Activity context, @NonNull CallBack callBack) {
        mContext = context;
        mCallBack = callBack;
    }

    //=============================================================request callback
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
            if (mCallBack != null) mCallBack.onResultCallBack(response);
        }
        else {
            ToastUtils.ToastMessage(mContext, response.getErrorMsg());
        }
    }

    //=============================================================controller methods
    @Override
    public void addPraise(long userid, long dynamicid, MomentsInfo info,
                          @RequestType.DynamicRequestType int requesttype) {
        if (mDynamicAddPraiseRequest == null) {
            mDynamicAddPraiseRequest = new DynamicAddPraiseRequest(info);
            mDynamicAddPraiseRequest.setOnResponseListener(this);
            mDynamicAddPraiseRequest.setRequestType(requesttype);
        }
        mDynamicAddPraiseRequest.setInfo(info);
        mDynamicAddPraiseRequest.userid = userid;
        mDynamicAddPraiseRequest.dynamicid = dynamicid;
        mDynamicAddPraiseRequest.execute();
    }

    @Override
    public void cancelPraise(long userid, long dynamicid, MomentsInfo info,
                             @RequestType.DynamicRequestType int requesttype) {
        if (mDynamicCancelPraiseRequest == null) {
            mDynamicCancelPraiseRequest = new DynamicCancelPraiseRequest(info);
            mDynamicCancelPraiseRequest.setOnResponseListener(this);
            mDynamicCancelPraiseRequest.setRequestType(requesttype);
        }
        mDynamicCancelPraiseRequest.setInfo(info);
        mDynamicCancelPraiseRequest.userid = userid;
        mDynamicCancelPraiseRequest.dynamicid = dynamicid;
        mDynamicCancelPraiseRequest.execute();
    }
    //=============================================================destroy
    public void destroyController() {
        mDynamicAddPraiseRequest = null;
        mCallBack = null;
    }
    public interface CallBack {
        void onResultCallBack(BaseResponse response);
    }
}
