package razerdp.friendcircle.app.mvp.model.entity;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by 大灯泡 on 2016/10/27.
 */

public class MomentsInfo extends BmobObject {
    private UserInfo author;
    private long momentid;
    private UserInfo hostinfo;
    private List<UserInfo> likes;


    public MomentsInfo() {
    }

    public UserInfo getAuthor() {
        return author;
    }

    public void setAuthor(UserInfo author) {
        this.author = author;
    }

    public long getMomentid() {
        return momentid;
    }

    public void setMomentid(long momentid) {
        this.momentid = momentid;
    }

    public UserInfo getHostinfo() {
        return hostinfo;
    }

    public void setHostinfo(UserInfo hostinfo) {
        this.hostinfo = hostinfo;
    }

    public List<UserInfo> getLikes() {
        return likes;
    }

    public void setLikes(List<UserInfo> likes) {
        this.likes = likes;
    }
}
