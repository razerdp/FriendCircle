package razerdp.friendcircle.api.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import razerdp.friendcircle.widget.commentwidget.CommentWidget;

/**
 * Created by 大灯泡 on 2016/2/23.
 * 评论用的bean
 * @see CommentWidget
 */
public class CommentInfo implements Parcelable {
    public UserInfo userA;
    public UserInfo userB;

    public String commentText;
    public long commentId;
    public long createTime;
    public int canDelete;

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.userA, 0);
        dest.writeParcelable(this.userB, 0);
        dest.writeString(this.commentText);
        dest.writeLong(this.commentId);
        dest.writeLong(this.createTime);
        dest.writeInt(this.canDelete);
    }

    public CommentInfo() {}

    protected CommentInfo(Parcel in) {
        this.userA = in.readParcelable(UserInfo.class.getClassLoader());
        this.userB = in.readParcelable(UserInfo.class.getClassLoader());
        this.commentText = in.readString();
        this.commentId = in.readLong();
        this.createTime = in.readLong();
        this.canDelete = in.readInt();
    }

    public static final Parcelable.Creator<CommentInfo> CREATOR = new Parcelable.Creator<CommentInfo>() {
        public CommentInfo createFromParcel(Parcel source) {return new CommentInfo(source);}

        public CommentInfo[] newArray(int size) {return new CommentInfo[size];}
    };
}
