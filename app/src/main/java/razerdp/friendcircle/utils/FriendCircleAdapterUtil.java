package razerdp.friendcircle.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import java.util.List;
import razerdp.friendcircle.api.adapter.CircleBaseAdapter;
import razerdp.friendcircle.api.data.DynamicType;
import razerdp.friendcircle.api.data.controller.BaseDynamicController;
import razerdp.friendcircle.api.data.entity.MomentsInfo;
import razerdp.friendcircle.ui.adapter.FriendCircleAdapter;
import razerdp.friendcircle.ui.adapter.itemview.ItemOnlyChar;
import razerdp.friendcircle.ui.adapter.itemview.ItemShareWeb;
import razerdp.friendcircle.ui.adapter.itemview.ItemWithImg;
import razerdp.friendcircle.ui.adapter.itemview.ItemWithImgSingle;

/**
 * Created by 大灯泡 on 2016/2/25.
 * 朋友圈adapter工具类
 */
public class FriendCircleAdapterUtil {

    public static FriendCircleAdapter getAdapter(Activity context, List<MomentsInfo> datas) {
        FriendCircleAdapter.Builder<MomentsInfo> builder = new CircleBaseAdapter.Builder<>(datas).addType(DynamicType.TYPE_ONLY_CHAR, ItemOnlyChar.class)
                                                                                                 .addType(DynamicType.TYPE_WITH_IMG,ItemWithImg.class)
                                                                                                 .addType(DynamicType.TYPE_SHARE_WEB,ItemShareWeb.class)
                                                                                                 .addType(DynamicType.TYPE_IMG_SINGLE,ItemWithImgSingle.class)
                                                                                                 .build();
        return new FriendCircleAdapter(context, builder);
    }
    public static FriendCircleAdapter getAdapter(Activity context, List<MomentsInfo> datas,@NonNull BaseDynamicController
                                                 controller) {
        FriendCircleAdapter.Builder<MomentsInfo> builder = new CircleBaseAdapter.Builder<>(datas).setController(controller)
                                                                                                 .addType(DynamicType.TYPE_ONLY_CHAR, ItemOnlyChar.class)
                                                                                                 .addType(DynamicType.TYPE_WITH_IMG,ItemWithImg.class)
                                                                                                 .addType(DynamicType.TYPE_SHARE_WEB,ItemShareWeb.class)
                                                                                                 .addType(DynamicType.TYPE_IMG_SINGLE,ItemWithImgSingle.class)
                                                                                                 .build();
        return new FriendCircleAdapter(context, builder);
    }
}
