package razerdp.github.com.ui.widget.common;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by 大灯泡 on 2016/8/10.
 *
 * loading
 */

public class LoadingView extends View {

    private static final String TAG = "LoadingView";
    private static final int DEFAULT_COLOR = 0xff24c7fc;
    private static final int DEFAULT_STROKE_WIDTH = 2;
    private static final long DEFAULT_DURATION = 1080;
    private static final float DYNAMIC_DEGREE_VALUE = (float) Math.asin(1);
    private boolean isAnimaed;
    private Paint strokePaint;
    private Paint circlePaint;
    private Rect bounds;
    private Interpolator defaultInterpolator = new LinearInterpolator();
    private ValueAnimator valueAnimator;
    private int bigCircleSize;
    private int smallCircleSize;
    private int sideMargin;
    private float currentDegree;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initPaint();
        isAnimaed = false;
        bounds = new Rect();
    }

    private void initPaint() {
        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(Color.TRANSPARENT);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(DEFAULT_COLOR);
        circlePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int desiredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int desiredHeight = MeasureSpec.getSize(heightMeasureSpec);
        bounds.set(getPaddingLeft(), getPaddingTop(), desiredWidth - getPaddingRight(), desiredHeight - getPaddingBottom());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bounds.set(getPaddingLeft(), getPaddingTop(), w - getPaddingRight(), h - getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bigCircleSize == 0 || bigCircleSize > bounds.width()) {
            bigCircleSize = bounds.width() >> 3;
        }
        if (smallCircleSize == 0 || smallCircleSize > bounds.width()) {
            smallCircleSize = bounds.width() >> 5;
        }
        if (sideMargin == 0) {
            sideMargin = bounds.width() >> 2;
        }

        int bigCircleX = bounds.right - bigCircleSize / 2 - DEFAULT_STROKE_WIDTH - sideMargin;
        int bigCircleY = bounds.top + bigCircleSize / 2 + DEFAULT_STROKE_WIDTH + sideMargin;

        int smallCircleX = bounds.left + bigCircleSize / 2 + DEFAULT_STROKE_WIDTH + sideMargin;
        int smallCircleY = bounds.bottom - bigCircleSize / 2 - DEFAULT_STROKE_WIDTH - sideMargin;

        float bigCircleDynamicSize =
            (float) (bigCircleSize - (bigCircleSize - smallCircleSize) * Math.abs(Math.sin(currentDegree * DYNAMIC_DEGREE_VALUE / 180f)));
        if (bigCircleDynamicSize < smallCircleSize) {
            bigCircleDynamicSize = smallCircleSize;
        }

        float smallCircleDynamicSize =
            (float) (smallCircleSize + (bigCircleSize - smallCircleSize) * Math.abs(Math.sin(currentDegree * DYNAMIC_DEGREE_VALUE / 180f)));
        if (smallCircleDynamicSize > bigCircleSize) {
            smallCircleDynamicSize = bigCircleSize;
        }

        canvas.save();
        canvas.rotate(currentDegree, bounds.centerX(), bounds.centerY());
        canvas.drawCircle(bigCircleX, bigCircleY, bigCircleDynamicSize, circlePaint);
        canvas.drawCircle(bigCircleX, bigCircleY, bigCircleDynamicSize + strokePaint.getStrokeWidth(), strokePaint);
        canvas.drawCircle(smallCircleX, smallCircleY, smallCircleDynamicSize, circlePaint);
        canvas.drawCircle(smallCircleX, smallCircleY, smallCircleDynamicSize + strokePaint.getStrokeWidth(), strokePaint);

        canvas.restore();
    }

    public void start() {
        start(defaultInterpolator, DEFAULT_DURATION);
    }

    /**
     * duration必须为180的倍数，否则会自动调整为180的倍数
     */
    public void start(@Nullable Interpolator interpolator, long duration) {
        if (isAnimaed) return;

        if (duration % 180 != 0) {
            duration = (long) (180 * Math.floor(duration / 180.0f));
        }

        isAnimaed = true;
        clearAnimation();
        if (valueAnimator == null) {
            valueAnimator = createAnimator(interpolator, duration);
        }
        valueAnimator.cancel();
        valueAnimator.start();
    }

    public void stop() {
        isAnimaed = false;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        currentDegree = 0;
        clearAnimation();
    }

    public int getCircleColor() {
        return circlePaint == null ? 0 : circlePaint.getColor();
    }

    public void setCircleColor(int color) {
        if (circlePaint != null) {
            circlePaint.setColor(color);
        }
    }

    public int getSmallCircleSize() {
        return smallCircleSize;
    }

    public void setSmallCircleSize(int smallCircleSize) {
        this.smallCircleSize = smallCircleSize;
    }

    public int getBigCircleSize() {
        return bigCircleSize;
    }

    public void setBigCircleSize(int bigCircleSize) {
        this.bigCircleSize = bigCircleSize;
    }

    public void setStrokeColor(int color) {
        this.strokePaint.setColor(color);
    }

    public void setStrokeWidth(int width) {
        this.strokePaint.setStrokeWidth(width);
    }

    private ValueAnimator createAnimator(Interpolator interpolator, long duration) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 180f);
        animator.setInterpolator(interpolator == null ? defaultInterpolator : interpolator);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setDuration(duration == 0 ? DEFAULT_DURATION : duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentDegree = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        return animator;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }
}
