package razerdp.friendcircle.api.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import razerdp.friendcircle.widget.praisewidget.PraiseWidget;

/**
 * Created by 大灯泡 on 2016/2/21.
 * 点赞用的bean
 * @see PraiseWidget
 */
public class PraiseInfo implements Parcelable {
    public UserInfo praiseUserInfo;

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {dest.writeParcelable(this.praiseUserInfo, 0);}

    public PraiseInfo() {}

    protected PraiseInfo(Parcel in) {this.praiseUserInfo = in.readParcelable(UserInfo.class.getClassLoader());}

    public static final Creator<PraiseInfo> CREATOR = new Creator<PraiseInfo>() {
        public PraiseInfo createFromParcel(Parcel source) {return new PraiseInfo(source);}

        public PraiseInfo[] newArray(int size) {return new PraiseInfo[size];}
    };
}
