package razerdp.github.com.common.mvp.models.entity;

import android.text.TextUtils;

import cn.bmob.v3.BmobObject;
import razerdp.github.com.common.manager.LocalHostManager;
import razerdp.github.com.common.mvp.models.IComment;

/**
 * Created by 大灯泡 on 2016/10/27.
 * <p>
 * 评论
 */

public class CommentInfo extends BmobObject implements icom{

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


    @Override
    public String toString() {
        return "CommentInfo{" +
                "moment=" + moment +
                ", content='" + content + '\'' +
                ", author=" + author.toString() + '\n' +
                ", reply=" + (reply == null ? "null" : reply.toString()) + '\n' +
                '}';
    }
}
