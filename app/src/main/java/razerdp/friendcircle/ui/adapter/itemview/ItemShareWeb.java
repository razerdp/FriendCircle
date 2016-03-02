package razerdp.friendcircle.ui.adapter.itemview;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import razerdp.friendcircle.R;
import razerdp.friendcircle.api.adapter.view.BaseItemDelegate;
import razerdp.friendcircle.api.data.entity.MomentsInfo;
import razerdp.friendcircle.widget.SuperImageView;

/**
 * Created by 大灯泡 on 2016/2/25.
 * 分享
 * type=11
 */
public class ItemShareWeb extends BaseItemDelegate {
    private SuperImageView webThumb;
    private TextView webTitle;

    public ItemShareWeb(){}

    @Override
    public int getViewRes() {
        return R.layout.dynamic_item_with_share;
    }

    @Override
    public void onFindView(@NonNull View parent) {
        if (webThumb==null)webThumb= (SuperImageView) parent.findViewById(R.id.web_thumb);
        if (webTitle==null)webTitle= (TextView) parent.findViewById(R.id.web_title);
    }

    @Override
    protected void bindData(int position, @NonNull View v, @NonNull MomentsInfo data, int dynamicType) {
        webThumb.loadImageDefault(data.content.webImg);
        webTitle.setText(data.content.webTitle);
    }
}
