package razerdp.github.com.model;

import java.io.Serializable;

/**
 * Created by 大灯泡 on 2017/3/29.
 * <p>
 * 相册使用的bean
 */

public class AlbumInfo implements Serializable {
    private String albumName;
    private int photoCounts;
    private String firstPhoto;

    public AlbumInfo() {
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getPhotoCounts() {
        return photoCounts;
    }

    public void setPhotoCounts(int photoCounts) {
        this.photoCounts = photoCounts;
    }

    public String getFirstPhoto() {
        return firstPhoto;
    }

    public void setFirstPhoto(String firstPhoto) {
        this.firstPhoto = firstPhoto;
    }
}
