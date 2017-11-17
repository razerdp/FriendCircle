package razerdp.github.com.ui.widget.pullrecyclerview.wrapperadapter;

import android.view.View;

/**
 * Created by 大灯泡 on 2017/3/15.
 * <p>
 * 用于recyclerview的header和footer的view fix info
 */

public class FixedViewInfo {

    /**
     * 不完美解决方法：添加一个header，则从-2开始减1
     * header:-2~-98
     */
    public static final int ITEM_VIEW_TYPE_HEADER_START = -2;
    /**
     * 不完美解决方法：添加一个header，则从-99开始减1
     * footer:-99~-无穷
     */
    public static final int ITEM_VIEW_TYPE_FOOTER_START = -99;


    /**
     * The view to add to the list
     */
    public final View view;
    /**
     * 因为onCreateViewHolder不包含位置信息，所以itemViewType需要包含位置信息
     * <p>
     * 位置信息方法：将位置添加到高位
     */
    public final int itemViewType;

    public FixedViewInfo(View view, int itemViewType) {
        this.view = view;
        this.itemViewType = itemViewType;
    }
}
