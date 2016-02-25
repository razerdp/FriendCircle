package razerdp.friendcircle.ui;

import android.app.Activity;
import razerdp.friendcircle.api.adapter.CircleBaseAdapter;
import razerdp.friendcircle.api.data.model.MomentsInfo;

/**
 * Created by 大灯泡 on 2016/2/16.
 * 朋友圈适配器
 */
public class FriendCircleAdapter extends CircleBaseAdapter<MomentsInfo> {

    public FriendCircleAdapter(Activity context, Builder<MomentsInfo> mBuilder) {
        super(context, mBuilder);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).dynamicInfo.dynamicType;
    }
}
