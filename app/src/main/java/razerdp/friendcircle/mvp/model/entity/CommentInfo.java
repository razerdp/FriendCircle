package razerdp.friendcircle.mvp.model.entity;

import android.text.TextUtils;

import cn.bmob.v3.BmobObject;
import razerdp.friendcircle.app.manager.LocalHostManager;

/**
 * Created by 大灯泡 on 2016/10/27.
 * <p>
 * 评论
 */

public class CommentInfo extends BmobObject {

    public interface CommentFields {
        String REPLY_USER = "reply";
        String MOMENT = "moment";
        String CONTENT = "content";
        String AUTHOR_USER = "author";
    }


    private MomentsInfo moment;
    private String content;
    private UserInfo author;
    private UserInfo reply;

    public MomentsInfo getMoment() {
        return moment;
    }

    public void setMoment(MomentsInfo moment) {
        this.moment = moment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentid() {
        return getObjectId();
    }

    public UserInfo getAuthor() {
        return author;
    }

    public void setAuthor(UserInfo author) {
        this.author = author;
    }

    public UserInfo getReply() {
        return reply;
    }

    public void setReply(UserInfo reply) {
        this.reply = reply;
    }

    public boolean canDelete() {
        return author != null && TextUtils.equals(author.getUserid(), LocalHostManager.INSTANCE.getUserid());
    }
}
