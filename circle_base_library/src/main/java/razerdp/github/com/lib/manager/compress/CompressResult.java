package razerdp.github.com.lib.manager.compress;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 大灯泡 on 2018/10/26.
 */
public class CompressResult implements Parcelable {
    private String compressedFilePath;
    private float originWidth;
    private float originHeight;
    private float compressedWidth;
    private float compressedHeight;

    public CompressResult() {
    }

    protected CompressResult(Parcel in) {
        compressedFilePath = in.readString();
        originWidth = in.readFloat();
        originHeight = in.readFloat();
        compressedWidth = in.readFloat();
        compressedHeight = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(compressedFilePath);
        dest.writeFloat(originWidth);
        dest.writeFloat(originHeight);
        dest.writeFloat(compressedWidth);
        dest.writeFloat(compressedHeight);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CompressResult> CREATOR = new Creator<CompressResult>() {
        @Override
        public CompressResult createFromParcel(Parcel in) {
            return new CompressResult(in);
        }

        @Override
        public CompressResult[] newArray(int size) {
            return new CompressResult[size];
        }
    };

    public String getCompressedFilePath() {
        return compressedFilePath;
    }

    public CompressResult setCompressedFilePath(String compressedFilePath) {
        this.compressedFilePath = compressedFilePath;
        return this;
    }

    public float getOriginWidth() {
        return originWidth;
    }

    public CompressResult setOriginWidth(float originWidth) {
        this.originWidth = originWidth;
        return this;
    }

    public float getOriginHeight() {
        return originHeight;
    }

    public CompressResult setOriginHeight(float originHeight) {
        this.originHeight = originHeight;
        return this;
    }

    public float getCompressedWidth() {
        return compressedWidth;
    }

    public CompressResult setCompressedWidth(float compressedWidth) {
        this.compressedWidth = compressedWidth;
        return this;
    }

    public float getCompressedHeight() {
        return compressedHeight;
    }

    public CompressResult setCompressedHeight(float compressedHeight) {
        this.compressedHeight = compressedHeight;
        return this;
    }
}
