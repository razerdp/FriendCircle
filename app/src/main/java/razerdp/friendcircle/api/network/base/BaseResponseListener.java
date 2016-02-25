package razerdp.friendcircle.api.network.base;

/**
 * Created by 大灯泡 on 2016/2/9.
 * 网络请求回掉接口封装
 */
public interface BaseResponseListener {
    void onStart(BaseResponse response);
    void onStop(BaseResponse response);
    void onFailure(BaseResponse response);
    void onSuccess(BaseResponse response);

}
