package razerdp.github.com.lib.common.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.Serializable;

import razerdp.github.com.lib.utils.StringUtil;

/**
 * Created by 大灯泡 on 2017/10/12.
 */
public class ImageInfo implements Serializable, Parcelable, Cloneable, Comparable<ImageInfo> {
    public final String imagePath;
    public final String thumbnailPath;
    public final String albumName;
    public final long time;
    public final int orientation;

    public ImageInfo(String imagePath, String thumbnailPath, String albumName, long time, int orientation) {
        this.imagePath = imagePath;
        this.thumbnailPath = thumbnailPath;
        this.albumName = albumName;
        this.time = time;
        this.orientation = orientation;
    }

    protected ImageInfo(Parcel in) {
        imagePath = in.readString();
        thumbnailPath = in.readString();
        albumName = in.readString();
        time = in.readLong();
        orientation = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imagePath);
        dest.writeString(thumbnailPath);
        dest.writeString(albumName);
        dest.writeLong(time);
        dest.writeInt(orientation);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageInfo> CREATOR = new Creator<ImageInfo>() {
        @Override
        public ImageInfo createFromParcel(Parcel in) {
            return new ImageInfo(in);
        }

        @Override
        public ImageInfo[] newArray(int size) {
            return new ImageInfo[size];
        }
    };

    public boolean checkValided() {
        return StringUtil.noEmpty(imagePath) || StringUtil.noEmpty(thumbnailPath);
    }

    @Override
    public ImageInfo clone() throws CloneNotSupportedException {
        //因为这里只有一些基础数据，所以就浅复制足够了
        return (ImageInfo) super.clone();
    }

    @Override
    public int compareTo(@NonNull ImageInfo o) {
        if (o == null) return -1;
        if (TextUtils.isEmpty(o.getImagePath())) return -1;
        if (TextUtils.equals(o.getImagePath(), getImagePath())) return 0;
        return -1;
    }

    //深复制，暂时不需要，另外利用流的方法的话，类需要实现Serializable接口
   /* public Object cloneDeepInternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        return ois.readObject();
    }*/

    @Override
    public String toString() {
        return "ImageInfo{" +
                "imagePath='" + imagePath + '\'' +
                ", thumbnailPath='" + thumbnailPath + '\'' +
                ", albumName='" + albumName + '\'' +
                ", time=" + time +
                ", orientation=" + orientation +
                '}';
    }

    public String getImagePath() {
        return TextUtils.isEmpty(imagePath) ? thumbnailPath : imagePath;
    }
}
