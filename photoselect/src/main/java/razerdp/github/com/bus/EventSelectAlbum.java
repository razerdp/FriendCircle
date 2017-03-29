package razerdp.github.com.bus;

/**
 * Created by 大灯泡 on 2017/3/29.
 * <p>
 * 选择相册的event
 */

public class EventSelectAlbum {
    private String albumName;

    public EventSelectAlbum(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
}
