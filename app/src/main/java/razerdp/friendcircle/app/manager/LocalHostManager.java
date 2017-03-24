package razerdp.friendcircle.app.manager;

import razerdp.friendcircle.app.mvp.model.entity.UserInfo;
import razerdp.github.com.baselibrary.helper.AppSetting;

import static razerdp.github.com.baselibrary.helper.AppSetting.HOST_AVATAR;
import static razerdp.github.com.baselibrary.helper.AppSetting.HOST_COVER;
import static razerdp.github.com.baselibrary.helper.AppSetting.HOST_ID;
import static razerdp.github.com.baselibrary.helper.AppSetting.HOST_NAME;
import static razerdp.github.com.baselibrary.helper.AppSetting.HOST_NICK;

/**
 * Created by 大灯泡 on 2016/10/28.
 * <p>
 * 本地用户管理
 */

public enum LocalHostManager {

    INSTANCE;

    private UserInfo localHostInfo = new UserInfo();

    public boolean init() {
        localHostInfo.setUsername(AppSetting.loadStringPreferenceByKey(HOST_NAME, "razerdp"));
        localHostInfo.setAvatar(AppSetting.loadStringPreferenceByKey(HOST_AVATAR,
                                                                     "http://upload.jianshu.io/users/upload_avatars/684042/bd1b2f796e3a.jpg?imageMogr/thumbnail/90x90/quality/100"));
        localHostInfo.setNick(AppSetting.loadStringPreferenceByKey(HOST_NICK, "羽翼君"));
        localHostInfo.setObjectId(AppSetting.loadStringPreferenceByKey(HOST_ID, "MMbKLCCU"));
        localHostInfo.setCover(AppSetting.loadStringPreferenceByKey(HOST_COVER, "http://d.hiphotos.baidu.com/zhidao/pic/item/bf096b63f6246b601ffeb44be9f81a4c510fa218.jpg"));
        return true;
    }

    public String getUsername() {
        return localHostInfo.getUsername();
    }

    public void setUsername(String username) {
        localHostInfo.setUsername(username);
    }

    public String getUserid() {
        return localHostInfo.getObjectId();
    }

    public String getNick() {
        return localHostInfo.getNick();
    }

    public void setNick(String nick) {
        localHostInfo.setNick(nick);
    }

    public String getAvatar() {
        return localHostInfo.getAvatar();
    }

    public void setAvatar(String avatar) {
        localHostInfo.setAvatar(avatar);
    }

    public void updateLocalHost(UserInfo userInfo) {
        if (userInfo != null) {
            AppSetting.saveStringPreferenceByKey(HOST_NAME, userInfo.getUsername());
            //头像和壁纸暂时强制使用开发者头像
            AppSetting.saveStringPreferenceByKey(HOST_AVATAR, "http://upload.jianshu.io/users/upload_avatars/684042/bd1b2f796e3a.jpg?imageMogr/thumbnail/90x90/quality/100");
            AppSetting.saveStringPreferenceByKey(HOST_COVER, "http://d.hiphotos.baidu.com/zhidao/pic/item/bf096b63f6246b601ffeb44be9f81a4c510fa218.jpg");
            AppSetting.saveStringPreferenceByKey(HOST_NICK, userInfo.getNick());
            AppSetting.saveStringPreferenceByKey(HOST_ID, userInfo.getUserid());

            this.localHostInfo.setNick(userInfo.getNick());
            this.localHostInfo.setObjectId(userInfo.getUserid());
            this.localHostInfo.setCover("http://d.hiphotos.baidu.com/zhidao/pic/item/bf096b63f6246b601ffeb44be9f81a4c510fa218.jpg");
            this.localHostInfo.setAvatar("http://upload.jianshu.io/users/upload_avatars/684042/bd1b2f796e3a.jpg?imageMogr/thumbnail/90x90/quality/100");
            this.localHostInfo.setUsername(userInfo.getUsername());
        }
    }

    public UserInfo getLocalHostUser() {
        return localHostInfo;
    }

    public UserInfo getDeveloperHostUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("razerdp");
        userInfo.setAvatar("http://upload.jianshu.io/users/upload_avatars/684042/bd1b2f796e3a.jpg?imageMogr/thumbnail/90x90/quality/100");
        userInfo.setNick("羽翼君");
        userInfo.setObjectId("MMbKLCCU");
        userInfo.setCover("http://d.hiphotos.baidu.com/zhidao/pic/item/bf096b63f6246b601ffeb44be9f81a4c510fa218.jpg");
        return userInfo;
    }


}
