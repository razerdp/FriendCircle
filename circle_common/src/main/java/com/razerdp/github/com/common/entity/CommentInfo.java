package com.razerdp.github.com.common.entity;

import android.text.TextUtils;

import com.razerdp.github.com.common.manager.LocalHostManager;

import cn.bmob.v3.BmobObject;
import razerdp.github.com.ui.widget.commentwidget.IComment;

/**
 * Created by 大灯泡 on 2016/10/27.
 * <p>
 * 评论
 */

public class CommentInfo extends BmobObject implements IComment<CommentInfo> {

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
    public String getCommentCreatorName() {
        return author == null ? "" : author.getNick();
    }

    @Override
    public String getReplyerName() {
        return reply == null ? "" : reply.getNick();
    }

    @Override
    public String getCommentContent() {
        return content;
    }

    @Override
    public CommentInfo getData() {
        return this;
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
