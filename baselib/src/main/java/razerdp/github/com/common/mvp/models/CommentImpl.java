package razerdp.github.com.common.mvp.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import razerdp.github.com.common.mvp.callback.OnCommentChangeCallback;
import razerdp.github.com.common.mvp.models.entity.CommentInfo;
import razerdp.github.com.common.request.AddCommentRequest;
import razerdp.github.com.common.request.DeleteCommentRequest;
import razerdp.github.com.common.request.SimpleResponseListener;

/**
 * Created by 大灯泡 on 2016/12/7.
 * <p>
 * 评论Model
 */

public class CommentImpl implements IComment {
    @Override
    public void addComment(@NonNull String momentid,
                           @NonNull String authorid,
                           @Nullable String replyUserId,
                           @NonNull String content,
                           @NonNull final OnCommentChangeCallback onCommentChangeCallback) {
        if (onCommentChangeCallback == null) return;
        AddCommentRequest addCommentRequest = new AddCommentRequest();
        addCommentRequest.setContent(content);
        addCommentRequest.setMomentsInfoId(momentid);
        addCommentRequest.setAuthorId(authorid);
        addCommentRequest.setReplyUserId(replyUserId);
        addCommentRequest.setOnResponseListener(new SimpleResponseListener<CommentInfo>() {
            @Override
            public void onSuccess(CommentInfo response, int requestType) {
                onCommentChangeCallback.onAddComment(response);
            }
        });
        addCommentRequest.execute();
    }

    @Override
    public void deleteComment(@NonNull String commentid, @NonNull final OnCommentChangeCallback onCommentChangeCallback) {
        if (onCommentChangeCallback == null) return;
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest();
        deleteCommentRequest.setCommentid(commentid);
        deleteCommentRequest.setOnResponseListener(new SimpleResponseListener<String>() {
            @Override
            public void onSuccess(String response, int requestType) {
                onCommentChangeCallback.onDeleteComment(response);
            }
        });
        deleteCommentRequest.execute();

    }
}
