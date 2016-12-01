package razerdp.friendcircle.app.net.request;

import android.text.TextUtils;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import razerdp.friendcircle.mvp.model.entity.CommentInfo;
import razerdp.friendcircle.mvp.model.entity.MomentsInfo;
import razerdp.friendcircle.mvp.model.entity.UserInfo;
import razerdp.friendcircle.app.net.base.BaseRequestClient;
import razerdp.friendcircle.utils.StringUtil;

/**
 * Created by 大灯泡 on 2016/10/28.
 *
 * 添加评论
 */

public class AddCommentRequest extends BaseRequestClient<CommentInfo> {

    private String content;
    private String authorId;
    private String replyUserId;
    private String momentsInfoId;

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
    }

    public void setMomentsInfoId(String momentsInfoId) {
        this.momentsInfoId = momentsInfoId;
    }

    @Override
    protected void executeInternal(final int requestType, boolean showDialog) {
        if (TextUtils.isEmpty(authorId)) {
            onResponseError(new BmobException("创建用户为空"), getRequestType());
            return;
        }
        if (TextUtils.isEmpty(momentsInfoId)) {
            onResponseError(new BmobException("动态为空"), getRequestType());
            return;
        }
        CommentInfo commentInfo = new CommentInfo();
        commentInfo.setContent(content);

        UserInfo author = new UserInfo();
        author.setObjectId(authorId);
        commentInfo.setAuthor(author);

        if (StringUtil.noEmpty(replyUserId)) {
            UserInfo replyUser = new UserInfo();
            replyUser.setObjectId(replyUserId);
            commentInfo.setReply(replyUser);
        }

        MomentsInfo momentsInfo = new MomentsInfo();
        momentsInfo.setObjectId(momentsInfoId);
        commentInfo.setMoment(momentsInfo);

        commentInfo.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    BmobQuery<CommentInfo> commentQuery = new BmobQuery<CommentInfo>();
                    commentQuery.getObject(s, new QueryListener<CommentInfo>() {
                        @Override
                        public void done(CommentInfo commentInfo, BmobException e) {
                            if (e == null) {
                                onResponseSuccess(commentInfo, requestType);
                            } else {
                                onResponseError(e, requestType);
                            }
                        }
                    });
                } else {
                    onResponseError(e, requestType);
                }
            }
        });
    }
}
