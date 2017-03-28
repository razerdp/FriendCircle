package razerdp.friendcircle.app.mvp.model.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by 大灯泡 on 2017/3/28.
 */

public class LikesInfo extends BmobObject {
    private UserInfo userid;
    private MomentsInfo momentsid;

    public interface LikesField {
        String USERID = "userid";
        String MOMENTID = "momentsid";
    }

    public String getUserid() {
        return userid == null ? null : userid.getUserid();
    }

    public void setUserid(String userid) {
        if (this.userid != null) {
            this.userid.setObjectId(userid);
        } else {
            this.userid = new UserInfo();
            this.userid.setObjectId(userid);
        }
    }

    public UserInfo getUserInfo() {
        return userid;
    }

    public void setUserInfo(UserInfo userid) {
        this.userid = userid;
    }

    public void setMomentsInfo(MomentsInfo momentsid) {
        this.momentsid = momentsid;
    }

    public String getMomentsid() {
        return momentsid == null ? null : momentsid.getMomentid();
    }

    public void setMomentsid(String momentsid) {
        if (this.momentsid != null) {
            this.momentsid.setObjectId(momentsid);
        } else {
            this.momentsid = new MomentsInfo();
            this.momentsid.setObjectId(momentsid);
        }
    }
}
