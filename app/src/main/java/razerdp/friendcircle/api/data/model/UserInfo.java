package razerdp.friendcircle.api.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

/**
 * Created by 大灯泡 on 2016/2/23.
 * 用户的bean
 */
public class UserInfo implements Parcelable{
    public long userId;
    public String nick;
    public String avatar;

    public UserInfo() {
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.userId);
        dest.writeString(this.nick);
        dest.writeString(this.avatar);
    }

    protected UserInfo(Parcel in) {
        this.userId = in.readLong();
        this.nick = in.readString();
        this.avatar = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        public UserInfo createFromParcel(Parcel source) {return new UserInfo(source);}

        public UserInfo[] newArray(int size) {return new UserInfo[size];}
    };
}
