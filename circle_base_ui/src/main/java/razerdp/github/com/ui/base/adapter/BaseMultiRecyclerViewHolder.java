package razerdp.github.com.ui.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 大灯泡 on 2018/4/10.
 */
public abstract class BaseMultiRecyclerViewHolder<T extends MultiType> extends BaseRecyclerViewHolder<T> {

    private BaseMultiRecyclerViewHolder(Context context, ViewGroup viewGroup, int layoutResId) {
        super(context, viewGroup, layoutResId);
    }

    private BaseMultiRecyclerViewHolder(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    public BaseMultiRecyclerViewHolder(View itemView, int viewType) {
        super(itemView, viewType);
    }

}
