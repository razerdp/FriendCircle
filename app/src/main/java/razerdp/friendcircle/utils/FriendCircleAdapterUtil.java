package razerdp.friendcircle.utils;

import android.app.Activity;
import java.util.List;
import razerdp.friendcircle.api.adapter.CircleBaseAdapter;
import razerdp.friendcircle.api.data.model.MomentsInfo;
import razerdp.friendcircle.ui.FriendCircleAdapter;

/**
 * Created by 大灯泡 on 2016/2/25.
 * 朋友圈adapter工具类
 */
public class FriendCircleAdapterUtil {

    public static FriendCircleAdapter getAdapter(Activity context, List<MomentsInfo> datas){
        FriendCircleAdapter.Builder<MomentsInfo> builder=new CircleBaseAdapter.Builder<>(datas).addType().build();
        return new FriendCircleAdapter(context,builder);
    }
}
