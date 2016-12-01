package razerdp.friendcircle.widget.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import razerdp.friendcircle.R;

/**
 * Created by 大灯泡 on 2016/4/11.
 * 朋友圈的imageview，包含点击动作
 */
public class ForceClickImageView extends ImageView {
    //前景层
    private Drawable mForegroundDrawable;
    private Rect mCachedBounds = new Rect();

    public ForceClickImageView(Context context) {
        this(context, null);
    }

    public ForceClickImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ForceClickImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     */
    private void init(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ForceClickImageView);
        mForegroundDrawable = a.getDrawable(R.styleable.ForceClickImageView_foregroundColor);
        if (mForegroundDrawable instanceof ColorDrawable) {
            int foreGroundColor = a.getColor(R.styleable.ForceClickImageView_foregroundColor, 0x55c6c6c6);
            mForegroundDrawable = new StateListDrawable();
            ColorDrawable forceDrawable = new ColorDrawable(foreGroundColor);
            ColorDrawable normalDrawable = new ColorDrawable(Color.TRANSPARENT);
            ((StateListDrawable) mForegroundDrawable).addState(new int[] { android.R.attr.state_focused },
                    forceDrawable);
            ((StateListDrawable) mForegroundDrawable).addState(new int[] { android.R.attr.state_pressed },
                    forceDrawable);
            ((StateListDrawable) mForegroundDrawable).addState(new int[] { android.R.attr.state_enabled },
                    normalDrawable);
            ((StateListDrawable) mForegroundDrawable).addState(new int[] {}, normalDrawable);
        }
        if (mForegroundDrawable != null) mForegroundDrawable.setCallback(this);
        a.recycle();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (mForegroundDrawable != null && mForegroundDrawable.isStateful()) {
            mForegroundDrawable.setState(getDrawableState());
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mForegroundDrawable != null) {
            mForegroundDrawable.setBounds(mCachedBounds);
            mForegroundDrawable.draw(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mForegroundDrawable != null) mCachedBounds.set(0, 0, w, h);
    }
}
