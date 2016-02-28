package razerdp.friendcircle.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by 大灯泡 on 2016/2/27.
 * listview嵌套的gridview
 */
public class NoScrollGridView extends GridView{
    private static final String TAG = "NoScrollGridView";

    public NoScrollGridView(Context context) {
        this(context,null);
    }

    public NoScrollGridView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NoScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;

        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        } else {
            heightSpec = heightMeasureSpec;
        }
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
