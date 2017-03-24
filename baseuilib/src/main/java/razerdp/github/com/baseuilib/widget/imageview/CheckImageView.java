package razerdp.github.com.baseuilib.widget.imageview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.nfc.Tag;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.socks.library.KLog;

import razerdp.github.com.baselibrary.utils.ui.UIHelper;

/**
 * Created by 大灯泡 on 2017/3/24.
 * <p>
 * 包含选择的Imageview
 */

public class CheckImageView extends android.support.v7.widget.AppCompatImageView {
    private static final String TAG = "CheckImageView";

    boolean isSelected;

    private CheckDrawable checkDrawable;
    private int checkDrawableSize = UIHelper.dipToPx(25);
    private static final int CHECK_DRAWABLE_MARGIN = UIHelper.dipToPx(5);
    private Rect checkBounds;

    public CheckImageView(Context context) {
        super(context);
        init(context);
    }

    public CheckImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CheckImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        checkDrawable = new CheckDrawable(context);
        checkBounds = new Rect();
        //drawable要draw需要关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE,null);

        setOnTouchListener(new OnTouchListener() {
            int x = 0;
            int y = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getX();
                        y = (int) event.getY();
                        return checkBounds.contains(x, y);
                    case MotionEvent.ACTION_UP:
                        if (checkBounds.contains(x, y)) {
                            isSelected = checkDrawable.toggleSelected();
                            KLog.i(TAG,"toggle");
                            invalidate();
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        checkAndInitCheckDrawBounds();

        KLog.i("draw","外部的draw");
        checkDrawable.setBounds(checkBounds);
        checkDrawable.setSelected(isSelected);
        checkDrawable.draw(canvas);
    }

    private void checkAndInitCheckDrawBounds() {
        if (checkDrawableSize > getWidth() / 3) {
            checkDrawableSize = getWidth() / 3;
        }
        if (checkBounds.isEmpty()) {
            int left = getWidth() - checkDrawableSize - getPaddingRight() - CHECK_DRAWABLE_MARGIN;
            int top = getPaddingTop() + CHECK_DRAWABLE_MARGIN;
            int rigth = left + checkDrawableSize;
            int bottom = top + checkDrawableSize;
            checkBounds.set(left, top, rigth, bottom);
        }
    }

}
