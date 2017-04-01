package razerdp.github.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import razerdp.github.com.baselibrary.manager.localphoto.LocalPhotoManager;

/**
 * Created by 大灯泡 on 2017/4/1.
 * <p>
 * 用于打开预览页面的实体类
 */

public class PhotoBrowserInfo implements Parcelable {
    private int curPos;
    private String currentAlbumName;
    private List<LocalPhotoManager.ImageInfo> selectedDatas;


    protected PhotoBrowserInfo(Parcel in) {
        curPos = in.readInt();
        currentAlbumName = in.readString();
        selectedDatas = in.createTypedArrayList(LocalPhotoManager.ImageInfo.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(curPos);
        dest.writeString(currentAlbumName);
        dest.writeTypedList(selectedDatas);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhotoBrowserInfo> CREATOR = new Creator<PhotoBrowserInfo>() {
        @Override
        public PhotoBrowserInfo createFromParcel(Parcel in) {
            return new PhotoBrowserInfo(in);
        }

        @Override
        public PhotoBrowserInfo[] newArray(int size) {
            return new PhotoBrowserInfo[size];
        }
    };

    public int getCurPos() {
        return curPos;
    }

    public void setCurPos(int curPos) {
        this.curPos = curPos;
    }

    public String getCurrentAlbumName() {
        return currentAlbumName;
    }

    public void setCurrentAlbumName(String currentAlbumName) {
        this.currentAlbumName = currentAlbumName;
    }

    public List<LocalPhotoManager.ImageInfo> getSelectedDatas() {
        return selectedDatas;
    }

    public void setSelectedDatas(List<LocalPhotoManager.ImageInfo> selectedDatas) {
        this.selectedDatas = selectedDatas;
    }

    private PhotoBrowserInfo(int curPos, String currentAlbumName, List<LocalPhotoManager.ImageInfo> selectedDatas) {
        this.curPos = curPos;
        this.currentAlbumName = currentAlbumName;
        this.selectedDatas = selectedDatas;
    }

    public static PhotoBrowserInfo create(int curPos, String currentAlbumName, List<LocalPhotoManager.ImageInfo> selectedDatas) {
        return new PhotoBrowserInfo(curPos, currentAlbumName, selectedDatas);
    }
}
