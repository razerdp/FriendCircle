package razerdp.friendcircle.app.manager;

import razerdp.friendcircle.app.mvp.model.entity.UserInfo;
import razerdp.friendcircle.utils.EncryUtil;
import razerdp.friendcircle.utils.PreferenceHelper;

import static razerdp.friendcircle.utils.PreferenceHelper.Keys.*;

/**
 * Created by 大灯泡 on 2016/10/28.
 * <p>
 * 本地用户管理
 */

public enum LocalHostManager {

    INSTANCE;

    private UserInfo localHostInfo = new UserInfo();

    public boolean init() {
        if (!PreferenceHelper.INSTANCE.containsKey(HAS_LOGIN) || PreferenceHelper.INSTANCE.containsKey(HOST_ID))
            return false;
        localHostInfo.setUsername((String) PreferenceHelper.INSTANCE.getData(HOST_NAME, "razerdp"));
        localHostInfo.setAvatar((String) PreferenceHelper.INSTANCE.getData(HOST_AVATAR, "http://upload.jianshu.io/users/upload_avatars/684042/bd1b2f796e3a.jpg?imageMogr/thumbnail/90x90/quality/100"));
        localHostInfo.setNick((String) PreferenceHelper.INSTANCE.getData(HOST_NICK, "羽翼君"));
        localHostInfo.setObjectId((String) PreferenceHelper.INSTANCE.getData(HOST_ID, "MMbKLCCU"));
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


}
