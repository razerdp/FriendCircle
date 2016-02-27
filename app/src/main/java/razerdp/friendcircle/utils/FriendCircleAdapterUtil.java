package razerdp.friendcircle.utils;

import android.app.Activity;
import java.util.List;
import razerdp.friendcircle.api.adapter.CircleBaseAdapter;
import razerdp.friendcircle.api.data.DynamicType;
import razerdp.friendcircle.api.data.entity.MomentsInfo;
import razerdp.friendcircle.ui.adapter.FriendCircleAdapter;
import razerdp.friendcircle.ui.adapter.itemview.ItemOnlyChar;
import razerdp.friendcircle.ui.adapter.itemview.ItemShareWeb;
import razerdp.friendcircle.ui.adapter.itemview.ItemWithImg;

/**
 * Created by 大灯泡 on 2016/2/25.
 * 朋友圈adapter工具类
 */
public class FriendCircleAdapterUtil {

    public static FriendCircleAdapter getAdapter(Activity context, List<MomentsInfo> datas) {
        FriendCircleAdapter.Builder<MomentsInfo> builder = new CircleBaseAdapter.Builder<>(datas).addType(DynamicType.TYPE_ONLY_CHAR, ItemOnlyChar.class)
                                                                                                 .addType(DynamicType.TYPE_WITH_IMG,ItemWithImg.class)
                                                                                                 .addType(DynamicType.TYPE_SHARE_WEB,ItemShareWeb.class)
                                                                                                 .build();
        return new FriendCircleAdapter(context, builder);
    }
}
