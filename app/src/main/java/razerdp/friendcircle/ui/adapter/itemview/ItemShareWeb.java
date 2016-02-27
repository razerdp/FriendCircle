package razerdp.friendcircle.ui.adapter.itemview;

import android.support.annotation.NonNull;
import android.view.View;
import razerdp.friendcircle.R;
import razerdp.friendcircle.api.adapter.view.BaseItemDelegate;
import razerdp.friendcircle.api.data.entity.MomentsInfo;

/**
 * Created by 大灯泡 on 2016/2/25.
 * 分享
 * type=11
 */
public class ItemShareWeb extends BaseItemDelegate {

    public ItemShareWeb(){}

    @Override
    protected void bindData(int position, @NonNull View v, @NonNull MomentsInfo data, int dynamicType) {

    }

    @Override
    public int getViewRes() {
        return R.layout.dynamic_item_with_share;
    }

    @Override
    public void onFindView(@NonNull View parent) {

    }
}
