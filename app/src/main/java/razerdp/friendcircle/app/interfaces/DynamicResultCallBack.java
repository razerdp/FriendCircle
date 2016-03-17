package razerdp.friendcircle.app.interfaces;

import razerdp.friendcircle.app.https.base.BaseResponse;

/**
 * Created by 大灯泡 on 2016/3/17.
 * 动态操作请求结果回调
 */
public interface DynamicResultCallBack {
    void onResultCallBack(BaseResponse response);
}
