package razerdp.github.com.ui.widget.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * Created by 大灯泡 on 2017/02/16.
 * <p/>
 * 小红点消息提醒
 */
public class DotWidget extends View {
    private static final String TAG = "DotWidget";

    public static boolean DEBUG = true;

    private static final int DEFAULT_DOT_COLOR = Color.RED;
    private static final int DEFAULT_DOT_RADIUS = 10;
    private static final int DEFAULT_DOT_SIZE = 10;
    private static final int DEFAULT_DOT_TEXT_COLOR = Color.WHITE;

    private View mTargetView;
    private FrameLayout container;
    private Rect marginRect;

    /**
     * 显示模式
     * <p/>
     * GRAVITY_TOP_LEFT 顶部居左
     * <p/>
     * GRAVITY_TOP_RIGHT 顶部居右
     * <p/>
     * GRAVITY_BOTTOM_LEFT 底部居左
     * <p/>
     * GRAVITY_BOTTOM_RIGHT 底部居右
     * <p/>
     * GRAVITY_CENTER 居中
     * <p/>
     * GRAVITY_CENTER_VERTICAL 垂直居中
     * <p/>
     * GRAVITY_CENTER_HORIZONTAL 水平居中
     * <p/>
     * GRAVITY_LEF_CENTER 左边居中
     * <p/>
     * GRAVITY_RIGHT_CENTER 右边居中
     * <p/>
     */
    public enum DotGravity {
        GRAVITY_TOP_LEFT, GRAVITY_TOP_RIGHT, GRAVITY_BOTTOM_LEFT, GRAVITY_BOTTOM_RIGHT, GRAVITY_CENTER,
        GRAVITY_CENTER_VERTICAL, GRAVITY_CENTER_HORIZONTAL, GRAVITY_LEF_CENTER, GRAVITY_RIGHT_CENTER
    }

    private DotGravity gravity;

    public enum Mode {
        ROUND_RECT, CIRCLE
    }

    private Mode mode = Mode.CIRCLE;

    private int dotColor = DEFAULT_DOT_COLOR;
    private int dotSize = DEFAULT_DOT_SIZE;
    private int dotTextColor = DEFAULT_DOT_TEXT_COLOR;
    private int dotTextSize;
    private int dotRadius = DEFAULT_DOT_RADIUS;

    private ShapeDrawable dotBackground;
    private String dotText = "";
    private boolean isShowing;
    private AlphaAnimation fadeIn, fadeOut;
    private boolean needReDraw = false;

    private DotWidget(Context context) {
        this(context, null, android.R.attr.textViewStyle);
    }

    private DotWidget(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    private DotWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DotWidget(Context context, View target) {
        this(context);
        initView(target);
    }

    private void initView(View target) {
        if (target == null) return;

        mTargetView = target;
        gravity = DotGravity.GRAVITY_TOP_RIGHT;
        marginRect = new Rect();
        isShowing = false;

        buildDefaultAnima();

        addDot(target);
    }

    private void buildDefaultAnima() {
        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(450);
        fadeIn.setAnimationListener(animaListener);

        fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new DecelerateInterpolator());
        fadeOut.setDuration(450);
        fadeOut.setAnimationListener(animaListener);
    }

    private void addDot(View target) {
        ViewGroup.LayoutParams targetViewParams = target.getLayoutParams();
        ViewGroup.LayoutParams newTargetViewParams = new ViewGroup.LayoutParams(targetViewParams.width, targetViewParams.height);
        targetViewParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        targetViewParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        //new一个容器
        container = new FrameLayout(getContext());
        container.setClipToPadding(false);

        ViewGroup parent = (ViewGroup) target.getParent();
        int index = parent.indexOfChild(target);
        //去掉目标view
        parent.removeView(target);
        //添加容器
        parent.addView(container, index, targetViewParams);

        //容器添加目标view
        container.addView(target, newTargetViewParams);
        setVisibility(GONE);
        //容器添加本view（红点）
        container.addView(this);
        parent.invalidate();
    }

    //=============================================================control
    public void show() {
        show(false);
    }

    public void show(boolean needAnima) {
        show(needAnima, fadeIn);
    }

    public void show(Animation anima) {
        show(true, anima);
    }

    private void show(boolean needAnima, Animation animation) {
        if (isShowing()) return;
        if (getBackground() == null || needReDraw || dotBackground == null) {
            dotBackground = getDotBackground();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(dotBackground);
            } else {
                setBackgroundDrawable(dotBackground);
            }
        }

        initDotParams(dotSize);
        mTargetView.setVisibility(VISIBLE);
        if (needAnima) {
            clearAnimation();
            startAnimation(animation);
        } else {
            setAlpha(1);
            setVisibility(VISIBLE);
            isShowing = true;
        }
        needReDraw = false;
    }

    public void hide() {
        hide(false);
    }

    public void hide(boolean needAnima) {
        hide(needAnima, fadeOut);
    }

    public void hide(Animation anima) {
        hide(true, anima);
    }

    private void hide(boolean needAnima, Animation animation) {
        if (!isShowing()) return;
        if (needAnima) {
            clearAnimation();
            this.startAnimation(animation);
        } else {
            setAlpha(0);
            setVisibility(GONE);
            isShowing = false;
        }
    }

    public void toggle() {
        toggle(false);
    }

    public void toggle(boolean needAnima) {
        toggle(needAnima, fadeIn, fadeOut);
    }

    public void toggle(Animation enterAnima, Animation exitAnima) {
        toggle(fadeIn, fadeOut);
    }

    private void toggle(boolean needAnima, Animation enterAnima, Animation exitAnima) {
        if (isShowing) {
            hide(needAnima && (exitAnima != null), exitAnima);
        } else {
            show(needAnima && (enterAnima != null), enterAnima);
        }
    }

    public void setDotMargins(int left, int top, int right, int bottom) {
        int absLeft = Math.abs(dip2Pixels(left));
        int absTop = Math.abs(dip2Pixels(top));
        int absRight = Math.abs(dip2Pixels(right));
        int absBottom = Math.abs(dip2Pixels(bottom));

        switch (gravity) {
            case GRAVITY_TOP_LEFT:
                applyDotMargin(absRight, absTop, 0, 0);
                break;
            case GRAVITY_LEF_CENTER:
                applyDotMargin(absRight, 0, 0, 0);
                break;
            case GRAVITY_BOTTOM_LEFT:
                applyDotMargin(absRight, 0, 0, absBottom);
                break;
            case GRAVITY_TOP_RIGHT:
                applyDotMargin(0, absTop, absLeft, 0);
                break;
            case GRAVITY_RIGHT_CENTER:
                applyDotMargin(0, 0, absLeft, 0);
                break;
            case GRAVITY_BOTTOM_RIGHT:
                applyDotMargin(0, 0, absLeft, absBottom);
                break;
        }
    }

    /**
     * 更新dot和容器的大小
     * <p/>
     * 原理如下：
     * 当dot在左边，那么源控件就是相对于dot的右边，因此想移动dot一般而言都是设置dotMarginRight，然而容器container已经限制死了大小
     * 因此需要将container进行扩展，使用padding，同时clipToPadding设置为false，使dot可以正常显示。而dot因为其gravity，因此只需要marginLeft为负值即可
     */
    private void applyDotMargin(int left, int top, int right, int bottom) {
        marginRect.left = -left;
        marginRect.top = -top;
        marginRect.right = -right;
        marginRect.bottom = -bottom;
        container.setPadding(left, top, right, bottom);

        if (DEBUG) {
            Log.d(TAG, "applyDotMargin: \n" + marginRect.toString());
        }
    }

    //=============================================================getter/setter

    public View getTargetView() {
        return mTargetView;
    }

    public void setTargetView(View mTargetView) {
        this.mTargetView = mTargetView;
    }

    public Rect getDotMargin() {
        Rect rect = new Rect();
        rect.left = px2Dip(Math.abs(marginRect.right));
        rect.right = px2Dip(Math.abs(marginRect.left));
        rect.top = px2Dip(Math.abs(marginRect.top));
        rect.bottom = px2Dip(Math.abs(marginRect.bottom));
        if (DEBUG) {
            Log.d(TAG, "getDotMargin: \n" + rect.toString());
        }
        return rect;
    }

    public DotGravity getDotGravity() {
        return gravity;
    }

    public void setDotGravity(DotGravity gravity) {
        this.gravity = gravity;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        needReDraw = true;
    }

    public int getDotColor() {
        return dotColor;
    }

    public void setDotColor(int dotColor) {
        this.dotColor = dotColor;
        needReDraw = true;
    }

    public int getDotSize() {
        return dotSize;
    }

    public void setDotSize(int dotSize) {
        this.dotSize = dotSize;
    }

    public int getDotTextColor() {
        return dotTextColor;
    }

    public void setDotTextColor(int dotTextColor) {
        this.dotTextColor = dotTextColor;
        needReDraw = true;
    }

    public int getDotTextSize() {
        return dotTextSize;
    }

    public void setDotTextSize(int dotTextSize) {
        this.dotTextSize = dotTextSize;
        needReDraw = true;
    }

    public int getDotRadius() {
        return dotRadius;
    }

    public void setDotRadius(int dotRadius) {
        this.dotRadius = dotRadius;
        needReDraw = true;
    }

    public String getDotText() {
        return dotText;
    }

    public void setDotText(String dotText) {
        this.dotText = dotText;
        needReDraw = true;
    }

    public boolean isShowing() {
        return isShowing;
    }

    //=============================================================tools method

    private int dip2Pixels(float dip) {
        return (int) (dip * getResources().getDisplayMetrics().density + 0.5f);
    }

    private int px2Dip(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private ShapeDrawable getDotBackground() {
        ShapeDrawable drawable = null;
        switch (mode) {
            case ROUND_RECT:
                int radius = dip2Pixels(dotRadius);
                float[] outerRect = new float[] { radius, radius, radius, radius, radius, radius, radius, radius };

                RoundRectShape rr = new RoundRectShape(outerRect, null, null);
                drawable = new InnerShapeDrawableWithText(rr, dotText);
                drawable.getPaint().setColor(dotColor);
                break;
            case CIRCLE:
                OvalShape os = new OvalShape();
                drawable = new InnerShapeDrawableWithText(os, dotText);
                drawable.getPaint().setColor(dotColor);
                //                int paddingPixels = dip2Pixels(8);
                //                drawable.setPadding(paddingPixels, paddingPixels, paddingPixels,
                //                        paddingPixels);
                break;
        }

        return drawable;
    }

    private void initDotParams(int dotSize) {
        int dotHeightAndWidth = dip2Pixels(dotSize);

        FrameLayout.LayoutParams dotParams = new FrameLayout.LayoutParams(dotHeightAndWidth, dotHeightAndWidth);

        switch (gravity) {
            case GRAVITY_TOP_LEFT:
                dotParams.gravity = Gravity.LEFT | Gravity.TOP;
                break;
            case GRAVITY_TOP_RIGHT:
                dotParams.gravity = Gravity.RIGHT | Gravity.TOP;
                break;
            case GRAVITY_BOTTOM_LEFT:
                dotParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
                break;
            case GRAVITY_BOTTOM_RIGHT:
                dotParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
                break;
            case GRAVITY_CENTER:
                dotParams.gravity = Gravity.CENTER;
                break;
            case GRAVITY_CENTER_VERTICAL:
                dotParams.gravity = Gravity.CENTER_VERTICAL;
                break;
            case GRAVITY_CENTER_HORIZONTAL:
                dotParams.gravity = Gravity.CENTER_HORIZONTAL;
                break;
            case GRAVITY_LEF_CENTER:
                dotParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                break;
            case GRAVITY_RIGHT_CENTER:
                dotParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                break;
            default:
                break;
        }

        dotParams.setMargins(marginRect.left, marginRect.top, marginRect.right, marginRect.bottom);

        this.setLayoutParams(dotParams);
    }

    class InnerShapeDrawableWithText extends ShapeDrawable {
        String text;
        Paint textPaint;

        public InnerShapeDrawableWithText(String text) {
            this.text = text;
            init();
        }

        public InnerShapeDrawableWithText(Shape s, String text) {
            super(s);
            this.text = text;
            init();
        }

        private void init() {
            textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setColor(dotTextColor);
            if (dotTextSize != 0) {
                textPaint.setTextSize(dotTextSize);
            }
        }

        @Override
        protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
            super.onDraw(shape, canvas, paint);
            if (!TextUtils.isEmpty(text)) {

                Rect r = getBounds();
                if (dotTextSize == 0) {
                    dotTextSize = (int) (r.width() * 0.5);
                    textPaint.setTextSize(dotTextSize);
                }
                //保证文字居中
                Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
                int baseline = r.top + (r.bottom - r.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
                textPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(text, r.centerX(), baseline, textPaint);
            }
        }
    }

    private Animation.AnimationListener animaListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            isShowing = !isShowing;
            DotWidget.this.setVisibility(isShowing ? VISIBLE : GONE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
}
