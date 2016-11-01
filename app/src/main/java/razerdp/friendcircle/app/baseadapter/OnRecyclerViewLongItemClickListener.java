package razerdp.friendcircle.app.baseadapter;

import android.view.View;

/**
 * Created by 大灯泡 on 2016/11/1.
 *
 */

public interface OnRecyclerViewLongItemClickListener<T> {
    boolean onItemLongClick(View v, int position, T data);
}
