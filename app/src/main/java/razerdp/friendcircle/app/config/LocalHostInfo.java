package razerdp.friendcircle.app.config;

/**
 * Created by 大灯泡 on 2016/3/10.
 * 当前账号信息
 */
public enum LocalHostInfo {
    INSTANCE;

    private long hostId;
    private String hostNick;
    private String hostAvatar;

    public long getHostId() {
        return hostId;
    }

    public void setHostId(long hostId) {
        this.hostId = hostId;
    }

    public String getHostNick() {
        return hostNick;
    }

    public void setHostNick(String hostNick) {
        this.hostNick = hostNick;
    }

    public String getHostAvatar() {
        return hostAvatar;
    }

    public void setHostAvatar(String hostAvatar) {
        this.hostAvatar = hostAvatar;
    }
}
