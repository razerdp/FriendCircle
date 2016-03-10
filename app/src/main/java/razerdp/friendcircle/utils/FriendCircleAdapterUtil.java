package razerdp.friendcircle.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import java.util.List;
import razerdp.friendcircle.app.adapter.base.CircleBaseAdapter;
import razerdp.friendcircle.app.config.DynamicType;
import razerdp.friendcircle.app.controller.BaseDynamicController;
import razerdp.friendcircle.app.data.entity.MomentsInfo;
import razerdp.friendcircle.app.adapter.FriendCircleAdapter;
import razerdp.friendcircle.ui.adapteritem.ItemOnlyChar;
import razerdp.friendcircle.ui.adapteritem.ItemShareWeb;
import razerdp.friendcircle.ui.adapteritem.ItemWithImg;
import razerdp.friendcircle.ui.adapteritem.ItemWithImgSingle;

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
