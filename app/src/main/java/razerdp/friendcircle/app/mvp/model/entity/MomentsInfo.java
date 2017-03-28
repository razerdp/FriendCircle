package razerdp.friendcircle.app.mvp.model.entity;

import android.text.TextUtils;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;
import razerdp.friendcircle.app.manager.LocalHostManager;
import razerdp.friendcircle.config.MomentsType;
import razerdp.github.com.baselibrary.utils.ToolUtil;

/**
 * Created by 大灯泡 on 2016/10/27.
 * <p>
 * 朋友圈动态
 */

public class MomentsInfo extends BmobObject {


    public interface MomentsFields {
        String LIKES = "likes";
        String HOST = "hostinfo";
        String COMMENTS = "comments";
        String AUTHOR_USER = "author";
    }

    private UserInfo author;
    private UserInfo hostinfo;
    private BmobRelation likes;
    private List<LikesInfo> likesList;
    private List<CommentInfo> commentList;
    private MomentContent content;


    public MomentsInfo() {
    }

    public UserInfo getAuthor() {
        return author;
    }

    public void setAuthor(UserInfo author) {
        this.author = author;
    }

    public String getMomentid() {
        return getObjectId();
    }

    public UserInfo getHostinfo() {
        return hostinfo;
    }

    public void setHostinfo(UserInfo hostinfo) {
        this.hostinfo = hostinfo;
    }

    public void setLikesBmobRelation(BmobRelation likes) {
        this.likes = likes;
    }

    public List<LikesInfo> getLikesList() {
        return likesList;
    }

    public void setLikesList(List<LikesInfo> likesList) {
        this.likesList = likesList;
    }

    public List<CommentInfo> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentInfo> commentList) {
        this.commentList = commentList;
    }

    public MomentContent getContent() {
        return content;
    }

    public void setContent(MomentContent content) {
        this.content = content;
    }

    public void addComment(CommentInfo commentInfo) {
        if (commentInfo == null) return;
        if (this.commentList == null) {
            this.commentList = new ArrayList<>();
        }
        this.commentList.add(commentInfo);
    }

    public void addLikes(LikesInfo likesInfo) {
        if (likesInfo == null) return;
        if (this.likesList==null){
            this.likesList=new ArrayList<>();
        }
        this.likesList.add(likesInfo);
    }

    public String getLikesObjectid() {
        String momentid = getMomentid();
        String userid = LocalHostManager.INSTANCE.getUserid();
        if (!ToolUtil.isListEmpty(likesList)) {
            for (LikesInfo likesInfo : likesList) {
                if (TextUtils.equals(momentid, likesInfo.getMomentsid()) && TextUtils.equals(userid, likesInfo.getUserid())) {
                    return likesInfo.getObjectId();
                }
            }
        }
        return null;
    }

    public int getMomentType() {
        if (content == null) {
            KLog.e("朋友圈内容居然是空的？？？？？MDZZ！！！！");
            return MomentsType.EMPTY_CONTENT;
        }
        return content.getMomentType();
    }
}
