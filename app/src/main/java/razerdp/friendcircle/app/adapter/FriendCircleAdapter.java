package razerdp.friendcircle.app.adapter;

import android.app.Activity;

import com.waynell.videolist.visibility.items.ListItem;
import com.waynell.videolist.visibility.scroll.ItemsProvider;

import razerdp.friendcircle.app.adapter.base.CircleBaseAdapter;
import razerdp.friendcircle.app.mvp.model.entity.MomentsInfo;
import razerdp.friendcircle.widget.ptrwidget.FriendCirclePtrListView;

/**
 * Created by 大灯泡 on 2016/2/16.
 * 朋友圈适配器
 */
public class FriendCircleAdapter extends CircleBaseAdapter<MomentsInfo> implements ItemsProvider {

    private FriendCirclePtrListView listView;

    public FriendCircleAdapter(Activity context, Builder<MomentsInfo> mBuilder) {
        super(context, mBuilder);
    }

    public FriendCircleAdapter(FriendCirclePtrListView listView, Activity context, Builder<MomentsInfo> mBuilder) {
        this(context, mBuilder);
        this.listView = listView;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).dynamicInfo.dynamicType;
    }

    // FIXME: 2016/7/15 关于ListItem的获取方式
    @Override
    public ListItem getListItem(int i) {
        return null;
    }

    @Override
    public int listItemSize() {
        return getCount();
    }
}
