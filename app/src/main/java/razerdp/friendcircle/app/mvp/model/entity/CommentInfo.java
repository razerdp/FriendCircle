package razerdp.friendcircle.app.mvp.model.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by 大灯泡 on 2016/10/27.
 *
 * 评论
 */

public class CommentInfo extends BmobObject {

    private MomentsInfo moment;
    private String content;
    private long commentid;
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

    public long getCommentid() {
        return commentid;
    }

    public void setCommentid(long commentid) {
        this.commentid = commentid;
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
}
