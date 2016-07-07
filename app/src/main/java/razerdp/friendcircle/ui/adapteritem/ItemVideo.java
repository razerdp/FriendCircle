package razerdp.friendcircle.ui.adapteritem;

import android.support.annotation.NonNull;
import android.view.View;

import com.waynell.videolist.visibility.items.ListItem;

import razerdp.friendcircle.R;
import razerdp.friendcircle.app.adapter.base.viewholder.BaseItemDelegate;
import razerdp.friendcircle.app.config.DynamicType;
import razerdp.friendcircle.app.mvp.model.entity.MomentsInfo;
import razerdp.friendcircle.widget.CircleVideoLayout;

/**
 * Created by 大灯泡 on 2016/7/7.
 * 小视频Item
 * type=12{@link DynamicType}
 */
public class ItemVideo extends BaseItemDelegate implements ListItem {
    private CircleVideoLayout videoLayout;

    @Override
    public int getViewRes() {
        return R.layout.dynamic_item_video;
    }

    @Override
    public void onFindView(@NonNull View parent) {
        if (videoLayout == null)
            videoLayout = (CircleVideoLayout) parent.findViewById(R.id.video_layout);

    }

    @Override
    protected void bindData(int position, @NonNull View v, @NonNull MomentsInfo data, int dynamicType) {
        videoLayout.loadVideo(data.content.videoCoverUrl,data.content.videoUrl);
    }

    @Override
    public void setActive(View view, int i) {
        videoLayout.playVideo();
    }

    @Override
    public void deactivate(View view, int i) {
        videoLayout.stopVideo();
    }
}
