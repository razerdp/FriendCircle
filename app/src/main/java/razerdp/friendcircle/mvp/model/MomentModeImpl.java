package razerdp.friendcircle.mvp.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import cn.bmob.v3.exception.BmobException;
import razerdp.friendcircle.app.net.OnResponseListener;
import razerdp.friendcircle.app.net.request.AddLikeRequest;

/**
 * Created by 大灯泡 on 2016/12/6.
 */

public class MomentModeImpl implements LikeModel,CommentModel{
    @Override
    public void addComment(@NonNull String momentsId, @NonNull String content, @NonNull String authorId, @Nullable String replyUserId) {

    }

    @Override
    public void removeComment() {

    }

    @Override
    public void addLike(String momentid, String userid) {
        // TODO: 2016/12/6 补充点赞请求
        new AddLikeRequest(momentid,userid).setOnResponseListener(new OnResponseListener<Boolean>() {
            @Override
            public void onStart(int requestType) {

            }

            @Override
            public void onSuccess(Boolean response, int requestType) {

            }

            @Override
            public void onError(BmobException e, int requestType) {

            }
        });
    }

    @Override
    public void removeLike() {

    }
}
