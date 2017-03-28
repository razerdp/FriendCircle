package razerdp.github.com.baseuilib.widget.imageview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
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
    private static final int CHECK_DRAWABLE_SIZE = UIHelper.dipToPx(20);
    private static final int CHECK_DRAWABLE_MARGIN = UIHelper.dipToPx(5);
    //对于每一个imageview来说，小勾勾的位置都是固定的，所以直接给静态，不用new太多对象
    private static Rect checkBounds = new Rect();
    private static Rect touchRectDelegate = new Rect();
    private boolean canSelect = true;

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
        checkDrawable.setCallback(this);

        setOnTouchListener(new OnTouchListener() {
            int x = 0;
            int y = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getX();
                        y = (int) event.getY();
                        return touchRectDelegate.contains(x, y);
                    case MotionEvent.ACTION_UP:
                        if (touchRectDelegate.contains(x, y) && canSelect) {
                            isSelected = checkDrawable.toggleSelected();
                            if (onSelectedChangeListener != null) {
                                KLog.i(TAG, "on touch up");
                                onSelectedChangeListener.onSelectChange(isSelected);
                            }
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
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setCanSelect(boolean canSelect) {
        if (this.canSelect == canSelect) return;
        this.canSelect = canSelect;
    }

    public boolean isCanSelect() {
        return canSelect;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        checkAndInitCheckDrawBounds();

        checkDrawable.setBounds(checkBounds);
        checkDrawable.setSelected(isSelected);
        checkDrawable.draw(canvas);
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable dr) {
        if (dr == checkDrawable) {
            invalidate();
        } else {
            super.invalidateDrawable(dr);
        }
    }

    private void checkAndInitCheckDrawBounds() {
        if (checkBounds.isEmpty() && getWidth() > 0 || checkBounds.left < 0) {
            int left = getWidth() - CHECK_DRAWABLE_SIZE - getPaddingRight() - CHECK_DRAWABLE_MARGIN;
            int top = getPaddingTop() + CHECK_DRAWABLE_MARGIN;
            int rigth = left + CHECK_DRAWABLE_SIZE;
            int bottom = top + CHECK_DRAWABLE_SIZE;
            checkBounds.set(left, top, rigth, bottom);
            touchRectDelegate.set(checkBounds);
            //扩展点击范围
            touchRectDelegate.inset(-5,-5);
        }
    }


    private OnSelectedChangeListener onSelectedChangeListener;

    public OnSelectedChangeListener getOnSelectedChangeListener() {
        return onSelectedChangeListener;
    }

    public void setOnSelectedChangeListener(OnSelectedChangeListener onSelectedChangeListener) {
        this.onSelectedChangeListener = onSelectedChangeListener;
    }

    public interface OnSelectedChangeListener {
        void onSelectChange(boolean isSelect);
    }

}
