package razerdp.friendcircle.app.mvp.model.entity;

import cn.bmob.v3.BmobObject;
import razerdp.friendcircle.utils.EncryUtil;

/**
 * Created by 大灯泡 on 2016/10/27.
 */

public class UserInfo extends BmobObject {

    private String username;
    private String userid;
    private String password;
    private String nick;
    private String avatar;

    public UserInfo() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = EncryUtil.MD5(password);
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
