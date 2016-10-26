package razerdp.friendcircle.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import razerdp.friendcircle.R;
import razerdp.friendcircle.utils.UIHelper;


/**
 * Created by 大灯泡 on 2016/6/23.
 * <p/>
 * 圆形饼图加载控件
 */
public class CircleProgressView extends View implements View.OnClickListener {
    private static final String TAG = "CircleProgressView";
    public static boolean DEBUG = true;

    //options
    private int circleSize;
    private int textSize;
    private int circleColor;
    private int textColor;
    private int strokeWidth;
    private int strokeColor;
    private int strokeMargin;

    private Paint circlePaint;
    private Paint strokePaint;
    private Paint textPaint;

    private RectF circleRect;

    private volatile int currentPresent;

    private boolean isFailed;

    private int defaultWidth;
    private int defaultHeight;

    private AlphaAnimation exitAnimation;
    private AlphaAnimation enterAnimation;

    private boolean isLoading;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme()
                .obtainStyledAttributes(attrs,
                        R.styleable.CircleProgressView,
                        0,
                        0);
        circleSize = a.getDimensionPixelSize(R.styleable.CircleProgressView_inner_circle_size,
                0);
        textSize = a.getDimensionPixelSize(R.styleable.CircleProgressView_inner_text_size, 0);
        circleColor = a.getColor(R.styleable.CircleProgressView_inner_circle_color, 0);
        textColor = a.getColor(R.styleable.CircleProgressView_inner_text_color, 0);
        strokeWidth = a.getDimensionPixelSize(R.styleable.CircleProgressView_stroke_width, 0);
        strokeColor = a.getColor(R.styleable.CircleProgressView_stroke_color, 0);
        strokeMargin = a.getDimensionPixelSize(R.styleable.CircleProgressView_stroke_margin,
                0);
        currentPresent = a.getInt(R.styleable.CircleProgressView_current_progress, 0);
        a.recycle();
        initDefaultValueWhenEmpty(context);
        buildAnimation();
        initPaint();
    }

    private void initDefaultValueWhenEmpty(Context context) {
        if (circleSize == 0) circleSize = UIHelper.dipToPx(30f);
        if (textSize == 0) textSize = 16;
        if (circleColor == 0) circleColor = 0xafffffff;
        if (textColor == 0) textColor = Color.WHITE;
        if (strokeWidth == 0) strokeWidth = UIHelper.dipToPx(2f);
        if (strokeColor == 0) strokeColor = 0xafffffff;
        if (strokeMargin == 0) strokeMargin = UIHelper.dipToPx(5f);
    }

    private void buildAnimation() {
        exitAnimation = new AlphaAnimation(1.0f, 0.0f);
        exitAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        exitAnimation.setDuration(500);
        //        exitAnimation.setFillAfter(true);
        exitAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                currentPresent = 100;
                postInvalidate();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                CircleProgressView.this.setVisibility(GONE);
                if (!isFailed) reset();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        enterAnimation = new AlphaAnimation(0.0f, 1.0f);
        enterAnimation.setDuration(500);
        enterAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        //        enterAnimation.setFillAfter(true);
        enterAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                CircleProgressView.this.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initPaint() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(circleColor);
        circlePaint.setStyle(Paint.Style.FILL);

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(strokeColor);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(strokeWidth);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (defaultWidth == 0) defaultWidth = w;
        if (defaultHeight == 0) defaultHeight = h;

        float horizontal_padding = (float) (getPaddingLeft() + getPaddingRight());
        float vertical_padding = (float) (getPaddingBottom() + getPaddingTop());

        float width = (float) w - horizontal_padding;
        float height = (float) h - vertical_padding;

        int strokeSpace = strokeMargin + strokeWidth + 1;

        if (circleRect == null) {
            circleRect = new RectF(
                    getPaddingLeft() + strokeSpace,
                    getPaddingTop() + strokeSpace,
                    getPaddingLeft() + width - strokeSpace,
                    getPaddingTop() + height - strokeSpace
            );
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (defaultWidth == 0) defaultWidth = getWidth();
        if (defaultHeight == 0) defaultHeight = getHeight();

        if (circleRect == null) {
            int strokeSpace = strokeMargin + strokeWidth + 1;
            circleRect = new RectF(
                    getPaddingLeft() + strokeSpace,
                    getPaddingTop() + strokeSpace,
                    getPaddingLeft() + getWidth() - strokeSpace,
                    getPaddingTop() + getHeight() - strokeSpace
            );
        }

        if (!isFailed) {
            //画饼图
            canvas.drawArc(circleRect, -90, currentPresent * 3.6f, true, circlePaint);
            //画线
            int radius = (int) Math.max(circleRect.width() / 2, circleRect.height() / 2);
            canvas.drawCircle(circleRect.centerX(),
                    circleRect.centerY(),
                    radius + strokeMargin,
                    strokePaint);

            //文字，保证文字居中
            Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
            int baseline = (int) (circleRect.top + (circleRect.bottom - circleRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top);
            textPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(currentPresent + "%", circleRect.centerX(), baseline, textPaint);
        } else {
            //文字，保证文字居中
            Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
            int baseline = (int) (circleRect.top + (circleRect.bottom - circleRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top);
            textPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("加载失败，点我重新加载", getWidth() / 2, baseline, textPaint);
        }
    }

    public synchronized void reset() {
        isFailed = false;
        lastThreadId = 0;
        currentPresent = 0;
        if (defaultWidth != 0 && defaultHeight != 0 && getLayoutParams().width != defaultWidth) {
            getLayoutParams().width = defaultWidth;
            getLayoutParams().height = defaultHeight;
            setLayoutParams(getLayoutParams());
        }
        textPaint.setTextSize(textSize);
        setOnClickListener(null);
        postInvalidate();
    }

    public synchronized void setStart() {
        if (isLoading) return;
        isLoading = true;
        post(new Runnable() {
            @Override
            public void run() {
                reset();
                if (getAnimation() != null) {
                    clearAnimation();
                }
                startAnimation(enterAnimation);
                Log.d(TAG, "start");
            }
        });

    }

    public synchronized void setFinish(boolean needAnima) {
        isLoading = false;
        if (needAnima) {
            post(new Runnable() {
                @Override
                public void run() {
                    if (getAnimation() != null) {
                        clearAnimation();
                    }
                    startAnimation(exitAnimation);
                }
            });
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    setVisibility(GONE);
                    reset();
                }
            });

        }
        Log.d(TAG, "finish");
    }

    public synchronized void setFailed() {
        post(new Runnable() {
            @Override
            public void run() {
                isFailed = true;
                isLoading = false;
                lastThreadId = 0;
                if (getAnimation() != null) clearAnimation();
                if (defaultWidth != 0) {
                    getLayoutParams().width = defaultWidth * 3 >= UIHelper.getScreenWidthPix(
                            getContext()) ? UIHelper.getScreenWidthPix(getContext()) : defaultWidth * 3;
                    setLayoutParams(getLayoutParams());
                }
                textPaint.setTextSize(30);
                setOnClickListener(CircleProgressView.this);
                postInvalidate();
                setAlpha(1.0f);
                setVisibility(VISIBLE);
                Log.d(TAG, "failed");
            }
        });

    }

    /**
     * =============================================================
     * setter/getter
     */
    public int getCircleSize() {
        return circleSize;
    }

    public void setCircleSize(int circleSize) {
        this.circleSize = circleSize;
        postInvalidate();
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        postInvalidate();
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
        postInvalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        postInvalidate();
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        postInvalidate();
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        postInvalidate();
    }

    public int getStrokeMargin() {
        return strokeMargin;
    }

    public void setStrokeMargin(int strokeMargin) {
        this.strokeMargin = strokeMargin;
        postInvalidate();
    }

    public Paint getCirclePaint() {
        return circlePaint;
    }

    public void setCirclePaint(Paint circlePaint) {
        this.circlePaint = circlePaint;
        postInvalidate();
    }

    public Paint getStrokePaint() {
        return strokePaint;
    }

    public void setStrokePaint(Paint strokePaint) {
        this.strokePaint = strokePaint;
        postInvalidate();
    }

    public int getCurrentPresent() {
        return currentPresent;
    }

    public boolean isLoading() {
        return isLoading;
    }

    private long lastThreadId = 0;

    public synchronized void setCurrentPresent(int currentPresent) {
        if (lastThreadId == 0) {
            lastThreadId = Thread.currentThread()
                    .getId();
        }
        long currentThreadId = Thread.currentThread()
                .getId();
        if (currentThreadId == lastThreadId) {
            if (currentPresent < 0) currentPresent = 0;
            if (currentPresent > 100) currentPresent = 100;
            this.currentPresent = currentPresent;
            postInvalidate();
        }
    }

    public boolean isFailed() {
        return isFailed;
    }

    @Override
    public void onClick(View v) {
        if (onFailedClickListener != null) {
            onFailedClickListener.onFailedClick(v);
        }
    }

    private OnFailedClickListener onFailedClickListener;

    public OnFailedClickListener getOnFailedClickListener() {
        return onFailedClickListener;
    }

    public void setOnFailedClickListener(OnFailedClickListener onFailedClickListener) {
        this.onFailedClickListener = onFailedClickListener;
    }

    public interface OnFailedClickListener {
        void onFailedClick(View v);
    }
}
