package razerdp.friendcircle.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.socks.library.KLog;

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
    private OnExitAnimaEndListener onExitAnimaEndListener;

    private boolean isPlayingEnterAnima = false;
    private boolean isPlayingExitAnima = false;

    private float[] scaleRatios;

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
                return false;
            }
        });
    }

    private void playEnterAnimaInternal(final Rect from) {
        if (isPlayingEnterAnima || from == null) return;

        final Rect tFrom = new Rect(from);
        final Rect to = new Rect();

        getGlobalVisibleRect(to);

        scaleRatios = calculateRatios(tFrom, to);

        setPivotX(from.centerX() / to.width());
        setPivotY(from.centerY() / to.height());

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


    public void playExitAnima(Rect to, @Nullable View alphaView, @Nullable final OnExitAnimaEndListener l) {
        this.onExitAnimaEndListener = l;
        playExitAnimaInternal(to, alphaView);
    }

    private void playExitAnimaInternal(final Rect to, @Nullable View alphaView) {
        if (isPlayingEnterAnima || to == null || mAttacher == null) return;

        final float scale = Math.max(scaleRatios[0], scaleRatios[1]);

        final Rect from = getMineRect();

        viewTransfrom.animaTranslate(from.centerX(), to.centerX(), from.centerY(), to.centerY());
        viewTransfrom.animaScale(getScale(), scale, to.centerX(), to.centerY());
        if (alphaView != null) {
            viewTransfrom.animaAlpha(alphaView, 1.0f, 0);
        }
        viewTransfrom.start(new OnAllFinishListener() {
            @Override
            public void onAllFinish() {
                if (onExitAnimaEndListener != null) {
                    onExitAnimaEndListener.onExitAnimaEnd();
                }
            }
        });


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

    public OnExitAnimaEndListener getOnExitAnimaEndListener() {
        return onExitAnimaEndListener;
    }

    public void setOnExitAnimaEndListener(OnExitAnimaEndListener onExitAnimaEndListener) {
        this.onExitAnimaEndListener = onExitAnimaEndListener;
    }

    public interface OnEnterAnimaEndListener {
        void onEnterAnimaEnd();
    }

    public interface OnExitAnimaEndListener {
        void onExitAnimaEnd();
    }

    interface OnAllFinishListener {
        void onAllFinish();
    }

    private class ViewTransform implements Runnable {

        View targetView;

        volatile boolean isRunning;

        Scroller translateScroller;
        Scroller scaleScroller;
        Scroller alphaScroller;

        Interpolator defaultInterpolator = new DecelerateInterpolator();


        int scaleCenterX;
        int scaleCenterY;

        float currentScale;

        float alpha;

        int dx;
        int dy;

        int preTranslateX;
        int preTranslateY;

        OnAllFinishListener onAllFinishListener;


        ViewTransform() {
            isRunning = false;
            translateScroller = new Scroller(getContext(), defaultInterpolator);
            scaleScroller = new Scroller(getContext(), defaultInterpolator);
            alphaScroller = new Scroller(getContext(), defaultInterpolator);
        }

        void animaScale(float from, float to, int centerX, int centerY) {
            this.scaleCenterX = centerX;
            this.scaleCenterY = centerY;
            KLog.i("animaScale", "from  >>>  " + from + "   to  >>  " + to);
            scaleScroller.startScroll((int) (from * 10000), 0, (int) ((to - from) * 10000), 0, ANIMA_DURATION);
        }

        void animaTranslate(int fromX, int toX, int fromY, int toY) {
            preTranslateX = 0;
            preTranslateY = 0;
            translateScroller.startScroll(0, 0, fromX - toX, fromY - toY, ANIMA_DURATION);
        }

        void animaAlpha(View target, float fromAlpha, float toAlpha) {
            this.targetView = target;
            alphaScroller.startScroll((int) (fromAlpha * 10000), 0, (int) ((toAlpha - fromAlpha) * 10000), 0, ANIMA_DURATION);
        }

        @Override
        public void run() {

            boolean isAllFinish = true;

            if (scaleScroller.computeScrollOffset()) {
                currentScale = (float) scaleScroller.getCurrX() / 10000f;
                isAllFinish = false;
            }

            if (translateScroller.computeScrollOffset()) {
                dx += translateScroller.getCurrX() - preTranslateX;
                dy += translateScroller.getCurrY() - preTranslateY;

                preTranslateX = translateScroller.getCurrX();
                preTranslateY = translateScroller.getCurrY();

                isAllFinish = false;
            }

            if (alphaScroller.computeScrollOffset()) {
                alpha = (float) alphaScroller.getCurrX() / 10000f;
                isAllFinish = false;
            }

            if (!isAllFinish) {
                setMatrixValue();
                postExecuteSelf();
            } else {
                reset();
                isRunning = false;
                if (onAllFinishListener != null) {
                    onAllFinishListener.onAllFinish();
                }
            }

        }

        private void setMatrixValue() {
            KLog.i(currentScale);
            if (mAttacher == null) return;
            postMatrixTranslate(dx, dy);
            setMatrixScale(currentScale, scaleCenterX, scaleCenterY);
            if (targetView != null) targetView.setAlpha(alpha);
            applyMatrix();
        }

        private void postExecuteSelf() {
            if (isRunning) post(this);
        }

        private void reset() {
            scaleCenterX = 0;
            scaleCenterY = 0;

            currentScale = 0;

            dx = 0;
            dy = 0;

            preTranslateX = 0;
            preTranslateY = 0;

            alpha = 0;
        }

        void stop() {
            removeCallbacks(this);
            scaleScroller.abortAnimation();
            translateScroller.abortAnimation();
            isRunning = false;
            onAllFinishListener = null;
            reset();
        }

        void start(@Nullable OnAllFinishListener onAllFinishListener) {
            if (isRunning) return;
            this.onAllFinishListener = onAllFinishListener;
            isRunning = true;
            postExecuteSelf();
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        viewTransfrom.stop();
        super.onDetachedFromWindow();
    }
}
