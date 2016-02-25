package razerdp.friendcircle.api.network.base;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import razerdp.friendcircle.api.FriendCircleApp;

/**
 * Created by 大灯泡 on 2016/2/9.
 * 针对volley的操作封装(单例)
 */
public enum VolleyManager {
    INSTANCE;

    //volley队列
    private RequestQueue mRequestQueue=null;

    public void initQueue(int maxCacheByte){
        mRequestQueue= Volley.newRequestQueue(FriendCircleApp.appContext,maxCacheByte);
    }
    public void addQueue(Request request){
        mRequestQueue.add(request);
    }
    public void stopRequest(Object tag) {
        if (tag != null)
            mRequestQueue.cancelAll(tag);
    }

}
