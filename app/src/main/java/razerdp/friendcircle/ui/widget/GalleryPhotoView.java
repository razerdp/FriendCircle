package razerdp.friendcircle.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.socks.library.KLog;

import razerdp.friendcircle.utils.PhotoBrowseUtil;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by 大灯泡 on 2016/12/20.
 * <p>
 * 用于图片浏览页面的photoview，主要负责展开动画和回退动画的计算
 */

public class GalleryPhotoView extends PhotoView {
    private static final int ANIMA_DURATION = 350;

    private ViewTransform viewTransfrom;
    private OnEnterAnimaEndListener onEnterAnimaEndListener;

    private boolean isPlayingEnterAnima = false;


    public GalleryPhotoView(Context context) {
        super(context);
    }

    public GalleryPhotoView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public GalleryPhotoView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        viewTransfrom = new ViewTransform();
    }


    public void playEnterAnima(final Rect from, @Nullable final OnEnterAnimaEndListener l) {
        this.onEnterAnimaEndListener = l;
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                playEnterAnimaInternal(from);
                getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    private void playEnterAnimaInternal(Rect from) {
        if (isPlayingEnterAnima || from == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!isAttachedToWindow()) return;
        }

        final Rect tFrom = new Rect(from);
        final Rect to = new Rect();
        final Point tGlobalOffset = new Point();

        getGlobalVisibleRect(to, tGlobalOffset);

        tFrom.offset(-tGlobalOffset.x, -tGlobalOffset.y);
        to.offset(-tGlobalOffset.x, -tGlobalOffset.y);

        float[] scaleRatios = calculateRatios(tFrom, to);

        setPivotX(0.5f);
        setPivotY(0.5f);

        final AnimatorSet enterSet = new AnimatorSet();
        enterSet.play(ObjectAnimator.ofFloat(this, View.X, tFrom.left, to.left))
                .with(ObjectAnimator.ofFloat(this, View.Y, tFrom.top, to.top))
                .with(ObjectAnimator.ofFloat(this, View.SCALE_X, scaleRatios[0], 1f))
                .with(ObjectAnimator.ofFloat(this, View.SCALE_Y, scaleRatios[1], 1f));

        enterSet.setDuration(ANIMA_DURATION);
        enterSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isPlayingEnterAnima = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isPlayingEnterAnima = false;
                if (onEnterAnimaEndListener != null) {
                    onEnterAnimaEndListener.onEnterAnimaEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isPlayingEnterAnima = false;

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                isPlayingEnterAnima = true;

            }
        });
        enterSet.start();


    }

    public Rect getMineRect() {
        Drawable drawable = getDrawable();
        Rect drawableRect = new Rect();
        if (drawable != null) {
            drawableRect = rectF2rect(getDisplayRect());
        } else {
            drawableRect.set(0, 0, getWidth(), getHeight());
        }

        return drawableRect;

    }

    private Rect rectF2rect(RectF rectf) {
        Rect rect = new Rect();
        if (rectf == null) {
            rect.setEmpty();
        } else {
            rect.left = (int) rectf.left;
            rect.top = (int) rectf.top;
            rect.right = (int) rectf.right;
            rect.bottom = (int) rectf.bottom;
        }
        return rect;
    }


    private float[] calculateRatios(Rect startBounds, Rect endBounds) {
        float[] result = new float[2];
        float widthRatio = startBounds.width() * 1.0f / endBounds.width() * 1.0f;
        float heightRatio = startBounds.height() * 1.0f / endBounds.height() * 1.0f;
        result[0] = widthRatio;
        result[1] = heightRatio;
        return result;
    }


    public OnEnterAnimaEndListener getOnEnterAnimaEndListener() {
        return onEnterAnimaEndListener;
    }

    public void setOnEnterAnimaEndListener(OnEnterAnimaEndListener onEnterAnimaEndListener) {
        this.onEnterAnimaEndListener = onEnterAnimaEndListener;
    }

    public interface OnEnterAnimaEndListener {
        void onEnterAnimaEnd();
    }

    private class ViewTransform implements Runnable {

        volatile boolean isRunning;

        Scroller translateScroller;
        Scroller scaleScroller;

        Interpolator defaultInterpolator = new DecelerateInterpolator();


        int scaleCenterX;
        int scaleCenterY;

        float currentScale;

        int dx;
        int dy;

        int preTranslateX;
        int preTranslateY;


        public ViewTransform() {
            isRunning = false;
            translateScroller = new Scroller(getContext(), defaultInterpolator);
            scaleScroller = new Scroller(getContext(), defaultInterpolator);
        }

        void animaScale(float from, float to, int centerX, int centerY) {
            this.scaleCenterX = centerX;
            this.scaleCenterY = centerY;
            scaleScroller.startScroll((int) (from * 10000), 0, (int) ((to - from) * 10000), 0, ANIMA_DURATION);
        }

        void animaTranslate(int dx, int dy) {
            preTranslateX = 0;
            preTranslateY = 0;
            translateScroller.startScroll(0, 0, dx, dy, ANIMA_DURATION);
        }

        @Override
        public void run() {

            boolean isAllFinish = true;

            if (scaleScroller.computeScrollOffset()) {
                currentScale = (float) scaleScroller.getCurrX() / 10000f;
                isAllFinish = false;
            }

            if (translateScroller.computeScrollOffset()) {
                dx = translateScroller.getCurrX() - preTranslateX;
                dy = translateScroller.getCurrY() - preTranslateY;

                preTranslateX = translateScroller.getCurrX();
                preTranslateY = translateScroller.getCurrY();

                isAllFinish = false;
            }

            if (!isAllFinish) {
                setMatrixValue();
                postExecuteSelf();
            } else {
                isRunning = false;
            }

        }

        private void setMatrixValue() {
            KLog.i(currentScale);
            postScale(currentScale, scaleCenterX, scaleCenterY);
            applyMatrix();
//            drage(dx, dy);
        }

        private void postExecuteSelf() {
            if (isRunning) post(this);
        }

        void stop() {
            removeCallbacks(this);
            scaleScroller.abortAnimation();
            translateScroller.abortAnimation();
            isRunning = false;
        }

        void start() {
            isRunning = true;
            postExecuteSelf();
        }
    }

}
