package razerdp.friendcircle.app.net;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.BmobCallback2;

/**
 * Created by 大灯泡 on 2016/10/27.
 */

public interface OnResponseListener<T> {
    void onStart(int requestType);

    void onSuccess(T response, int requestType);

    void onError(BmobException e, int requestType);
}
