package razerdp.friendcircle.app.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by 大灯泡 on 2016/2/25.
 * 朋友圈当前帐号的实体，包含id,封面什么的。
 *
 * weblink:http://www.jianshu.com/p/94403e45fbef
 */
public class HostInfo implements Serializable {
    /**
     * hostid: 1001,
     * hostAvatar: "http://upload.jianshu.io/users/upload_avatars/684042/bd1b2f796e3a.jpg",
     * hostNick: "羽翼君",
     * hostWallPic: "http://www.pp3.cn/uploads/allimg/111118/10562Cb5-13.jpg"
     */
    public long hostid;
    public String hostAvatar;
    public String hostNick;
    public String hostWallPic;
}
