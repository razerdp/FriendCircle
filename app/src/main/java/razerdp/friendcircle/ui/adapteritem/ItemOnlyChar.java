package razerdp.friendcircle.ui.adapteritem;

import android.support.annotation.NonNull;
import android.view.View;
import razerdp.friendcircle.R;
import razerdp.friendcircle.app.adapter.base.viewholder.BaseItemDelegate;
import razerdp.friendcircle.app.config.DynamicType;
import razerdp.friendcircle.app.mvp.model.entity.MomentsInfo;

/**
 * Created by 大灯泡 on 2016/2/25.
 * 只有文字的朋友圈item
 * type=10{@link DynamicType}
 *
 */
public class ItemOnlyChar extends BaseItemDelegate {

    public ItemOnlyChar(){}

    @Override
    protected void bindData(int position, @NonNull View v, @NonNull MomentsInfo data, int dynamicType) {
    }

    @Override
    public int getViewRes() {
        return R.layout.dynamic_item_only_char;
    }

    @Override
    public void onFindView(@NonNull View parent) {

    }
}
