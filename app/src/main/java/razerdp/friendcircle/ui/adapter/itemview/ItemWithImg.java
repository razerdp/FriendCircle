package razerdp.friendcircle.ui.adapter.itemview;

import android.support.annotation.NonNull;
import android.view.View;
import razerdp.friendcircle.R;
import razerdp.friendcircle.api.adapter.view.BaseItemDelegate;
import razerdp.friendcircle.api.data.entity.MomentsInfo;

/**
 * Created by 大灯泡 on 2016/2/25.
 * 图文
 * type=13
 */
public class ItemWithImg extends BaseItemDelegate {

    public ItemWithImg(){}

    @Override
    protected void bindData(int position, @NonNull View v, @NonNull MomentsInfo data, int dynamicType) {

    }

    @Override
    public int getViewRes() {
        return R.layout.dynamic_item_with_img;
    }

    @Override
    public void onFindView(@NonNull View parent) {

    }
}
