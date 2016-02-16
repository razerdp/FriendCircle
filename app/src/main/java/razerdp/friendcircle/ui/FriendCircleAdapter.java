package razerdp.friendcircle.ui;

import android.app.Activity;
import razerdp.friendcircle.api.adapter.CircleBaseAdapter;

/**
 * Created by 大灯泡 on 2016/2/16.
 * 朋友圈适配器
 */
public class FriendCircleAdapter<T> extends CircleBaseAdapter {

    public FriendCircleAdapter(Activity context, Builder mBuilder) {
        super(context, mBuilder);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
}
