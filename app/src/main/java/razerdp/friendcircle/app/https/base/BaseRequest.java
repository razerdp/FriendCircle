package razerdp.friendcircle.app.https.base;

import android.text.TextUtils;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by 大灯泡 on 2016/2/9.
 * 针对volley请求封装
 * 请求回来的数据在此处先行解析，然后解析后的JSON回调给Client({@link BaseHttpRequestClient})
 * 因为请求本身处于volley的请求队列，所以解析是异步的，不会阻塞UI线程
 */
public class BaseRequest extends Request<BaseResponse> {
    private static final String TAG = "BaseRequest";
    private BaseHttpRequestClient mClient;
    private Map<String, String> mPostValue;

    public BaseRequest(int method, String url, BaseHttpRequestClient client, Response.ErrorListener listener) {
        super(method, url, listener);
        this.mClient = client;
    }

    public BaseRequest(int method, String url, Map<String, String> postValue, BaseHttpRequestClient client, Response.ErrorListener listener) {
        super(method, url, listener);
        this.mClient = client;
        mPostValue = postValue;
    }

    @Override
    protected Response<BaseResponse> parseNetworkResponse(NetworkResponse response) {
        //以下是volley(stringrequest)源码
        String responseStr;
        try {
            responseStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            responseStr = new String(response.data);
        }
        BaseResponse baseResponse = new BaseResponse();
        int start = 0;
        boolean hasMore = false;
        //以下针对封装处理，将返回的string解析为json（或gson）
        try {
            JSONTokener jsonParser = new JSONTokener(responseStr);
            JSONObject json = null;
            json = (JSONObject) jsonParser.nextValue();
            if (json!=null) {
                baseResponse.setStatus(json.optInt("stateCode"));
                baseResponse.setErrorMsg(json.optString("errorStr"));
                start=json.optInt("start");
                hasMore=json.optInt("loadMore")==1;
            }
            json=json.optJSONObject("data");
            baseResponse.setJsonStr(responseStr);

            baseResponse.setStart(start);
            baseResponse.setHasMore(hasMore);
            mClient.parseResponse(baseResponse, json,start,hasMore);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, ">>>>>>json解析失败<<<<<<<<\n\n" + ">>>>>>>>>>>>>>>>>>>>>该json字符串为：" + responseStr + "\n" +
                    "+<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        }

        return Response.success(baseResponse, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(BaseResponse response) {
        mClient.onResponse(response);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (mPostValue != null) {
            for (Map.Entry<String, String> entry : mPostValue.entrySet()) {
                //使用post方式,保证value不为空
                if (TextUtils.isEmpty(entry.getValue())) {
                    entry.setValue("");
                }
            }
        }
        return mPostValue;
    }
}
