package razerdp.friendcircle.app.mvp.model;

import cn.bmob.v3.exception.BmobException;
import razerdp.friendcircle.app.mvp.callback.OnLikeChangeCallback;
import razerdp.friendcircle.app.net.request.AddLikeRequest;
import razerdp.friendcircle.app.net.request.UnLikeRequest;
import razerdp.github.com.net.base.OnResponseListener;

/**
 * Created by 大灯泡 on 2016/12/7.
 * <p>
 * 点赞model
 */

public class LikeImpl implements ILike {

    @Override
    public void addLike(String momentid, final OnLikeChangeCallback onLikeChangeCallback) {
        if (onLikeChangeCallback == null) return;
        AddLikeRequest request = new AddLikeRequest(momentid);
        request.setOnResponseListener(new OnResponseListener<String>() {
            @Override
            public void onStart(int requestType) {

            }

            @Override
            public void onSuccess(String response, int requestType) {
                onLikeChangeCallback.onLike(response);
            }

            @Override
            public void onError(BmobException e, int requestType) {

            }
        });
        request.execute();
    }

    @Override
    public void unLike(String likesid, final OnLikeChangeCallback onLikeChangeCallback) {
        if (onLikeChangeCallback == null) return;
        UnLikeRequest request = new UnLikeRequest(likesid);
        request.setOnResponseListener(new OnResponseListener<Boolean>() {
            @Override
            public void onStart(int requestType) {

            }

            @Override
            public void onSuccess(Boolean response, int requestType) {
                if (response) {
                    onLikeChangeCallback.onUnLike();
                }
            }

            @Override
            public void onError(BmobException e, int requestType) {

            }
        });
        request.execute();
    }
}
