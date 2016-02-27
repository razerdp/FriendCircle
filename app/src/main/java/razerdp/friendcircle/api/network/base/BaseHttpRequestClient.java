package razerdp.friendcircle.api.network.base;

import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import razerdp.friendcircle.api.interfaces.BaseResponseListener;

/**
 * Created by 大灯泡 on 2016/2/9.
 * 针对volley进行二次封装，外部实现本抽象并且只能调用实现类api
 */
public abstract class BaseHttpRequestClient implements Response.Listener<BaseResponse>, Response.ErrorListener {
    private static final String TAG = "BaseHttpRequestClient";
    //接口回调，外部引入
    private BaseResponseListener mBaseResponseListener;

    private int requestType;
    private String jsonStr;
    private boolean showDialog;

    //volley的请求对象
    private BaseRequest mBaseRequest = null;

    public BaseHttpRequestClient() {
        Log.d(TAG, "新建了一个请求");
    }

    //=============================================================抽象方法
    public abstract String setUrl();

    public void postValue(Map<String, String> keyValue) {}

    public abstract void parseResponse(BaseResponse response, JSONObject json, int start, boolean hasMore)
            throws JSONException;
    //=============================================================对外暴露方法

    /** get方法 */
    public void execute(boolean showDialog) {
        setShowDialog(showDialog);
        mBaseRequest = new BaseRequest(Request.Method.GET, setUrl(), this, this);
        //配置
        mBaseRequest.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 3, 1.0f));
        mBaseRequest.setShouldCache(false);
        mBaseRequest.setTag(this.getClass().getSimpleName());
        postStart();
        VolleyManager.INSTANCE.addQueue(mBaseRequest);
    }

    public void execute() {
        this.execute(false);
    }

    public void post(boolean showDialog) {
        Map<String, String> postMap = new HashMap<>();
        postValue(postMap);

        setShowDialog(showDialog);
        mBaseRequest = new BaseRequest(Request.Method.POST, setUrl(), postMap, this, this);
        //配置
        mBaseRequest.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 3, 1.0f));
        mBaseRequest.setShouldCache(false);
        mBaseRequest.setTag(this.getClass().getSimpleName());
        postStart();
        VolleyManager.INSTANCE.addQueue(mBaseRequest);
    }

    public void post() {
        this.post(true);
    }

    //=============================================================状态回调

    /** Volley回调 */
    @Override
    public void onResponse(BaseResponse response) {
        postSuccess(response);
    }

    /** Volley回调 */
    @Override
    public void onErrorResponse(VolleyError error) {
        postFailure();
    }

    private void postStart() {
        BaseResponse response = new BaseResponse();
        response.setRequestType(requestType);
        response.setShowDialog(showDialog);
        if (mBaseResponseListener != null) mBaseResponseListener.onStart(response);
    }

    private void postStop() {
        BaseResponse response = new BaseResponse();
        response.setRequestType(requestType);
        response.setShowDialog(showDialog);
        if (mBaseResponseListener != null) mBaseResponseListener.onStop(response);
    }

    private void postFailure() {
        BaseResponse response = new BaseResponse();
        response.setRequestType(requestType);
        response.setShowDialog(showDialog);
        if (mBaseResponseListener != null) mBaseResponseListener.onFailure(response);
    }

    private void postSuccess(BaseResponse response) {

        response.setRequestType(requestType);
        response.setShowDialog(showDialog);
        if (mBaseResponseListener != null) mBaseResponseListener.onSuccess(response);
    }

    //=============================================================请求操作
    public void cancel() {
        if (mBaseRequest != null) {
            mBaseRequest.cancel();
            postStop();
        }
        VolleyManager.INSTANCE.stopRequest(this.getClass().getSimpleName());
    }

    //=============================================================Setter/Getter

    public BaseResponseListener getOnResponseListener() {
        return mBaseResponseListener;
    }

    public void setOnResponseListener(BaseResponseListener baseResponseListener) {
        mBaseResponseListener = baseResponseListener;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }
}
