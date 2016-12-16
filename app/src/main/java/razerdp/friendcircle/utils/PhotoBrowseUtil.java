package razerdp.friendcircle.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.socks.library.KLog;

/**
 * Created by 大灯泡 on 2016/12/16.
 * <p>
 * 图片浏览工具类
 */
public class PhotoBrowseUtil {

    private static final long ANIMA_DURATION=350;


    /**
     * 进场动画
     *
     * @param targetView
     * @param startRect
     * @param onAnimaEndListener
     */
    public static void playEnterAnima(View targetView, Rect startRect, @Nullable final OnAnimaEndListener onAnimaEndListener) {
        if (targetView == null || startRect == null) return;
        final Rect endRect = new Rect();
        final Point globalOffset = new Point();

        targetView.getGlobalVisibleRect(endRect, globalOffset);

        final Rect tStartRect = new Rect(startRect);
        final Rect tEndRect = new Rect(endRect);

        tStartRect.offset(-globalOffset.x, -globalOffset.y);
        tEndRect.offset(-globalOffset.x, -globalOffset.y);

        float[] scaleRatios = calculateRatios(tStartRect, tEndRect);

        targetView.setPivotX(0.5f);
        targetView.setPivotY(0.5f);

        final AnimatorSet enterSet = new AnimatorSet();
        enterSet.play(ObjectAnimator.ofFloat(targetView, View.X, tStartRect.left, tEndRect.left))
                .with(ObjectAnimator.ofFloat(targetView, View.Y, tStartRect.top, tEndRect.top))
                .with(ObjectAnimator.ofFloat(targetView, View.SCALE_X, scaleRatios[0], 1f))
                .with(ObjectAnimator.ofFloat(targetView, View.SCALE_Y, scaleRatios[1], 1f));

        enterSet.setDuration(ANIMA_DURATION);
        enterSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onAnimaEndListener != null) {
                    onAnimaEndListener.onAnimaEnd(animation);
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        enterSet.start();
    }


    /**
     * 退场动画
     * <p>
     * startRect仅仅用来计算缩放比率。
     *
     * @param targetView
     * @param alphaView
     * @param startRect
     * @param endRect
     * @param onAnimaEndListener
     */
    public static void playExitAnima(View targetView, final View alphaView, Rect startRect, Rect endRect, @Nullable final OnAnimaEndListener onAnimaEndListener) {
        if (targetView == null || startRect == null || endRect == null) return;

        Rect tempRect=new Rect();
        Point tempPoint=new Point();
        targetView.getGlobalVisibleRect(tempRect,tempPoint);

        final Rect tStartRect = new Rect(startRect);
        final Rect tEndRect = new Rect(endRect);

        //因为startrect是用的imageview的drawable的rect，所以不需要进行offset，否则最终缩放回去会有微小的偏差
        //tStartRect.offset(-tempPoint.x,-tempPoint.y);
        tEndRect.offset(-tempPoint.x,-tempPoint.y);

        //退出动画需要缩小，而start指当前的，end指原来的view，因此需要掉转过来计算
        float[] scaleRatios = calculateRatios(tEndRect, tStartRect);

        targetView.setPivotX(0.5f);
        targetView.setPivotY(0.5f);


        //计算位移量
        int offsetWidth = (int) (tStartRect.left * scaleRatios[0]);
        int offsetHeight = (int) (tStartRect.top * scaleRatios[1]);

        final AnimatorSet exitSet = new AnimatorSet();

        //退场动画针对原来view的所在位置，因此使用endrect
        AnimatorSet.Builder builder = exitSet.play(ObjectAnimator.ofFloat(targetView, View.X, tEndRect.left - offsetWidth))
                                             .with(ObjectAnimator.ofFloat(targetView, View.Y, tEndRect.top - offsetHeight))
                                             .with(ObjectAnimator.ofFloat(targetView, View.SCALE_X, scaleRatios[0]))
                                             .with(ObjectAnimator.ofFloat(targetView, View.SCALE_Y, scaleRatios[1]));
        if (alphaView != null) {
            builder.with(ObjectAnimator.ofFloat(alphaView, View.ALPHA, 1f, 0.0f));
        }
        exitSet.setDuration(ANIMA_DURATION);
        exitSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onAnimaEndListener != null) {
                    onAnimaEndListener.onAnimaEnd(animation);
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        exitSet.start();


    }


    /**
     * 计算imageview里面的图片的frame位置
     *
     * @param imageView
     * @return
     */
    public static Rect calcuateDrawableBounds(ImageView imageView) {
        if (imageView == null) return null;
        if (imageView.getDrawable() == null) return null;
        Rect result = new Rect();
        Rect drawableRect = imageView.getDrawable().getBounds();
        Matrix drawableMatrix = imageView.getImageMatrix();
        float[] matrixValues = new float[9];
        if (drawableMatrix != null) {
            drawableMatrix.getValues(matrixValues);
        }
        result.left = (int) matrixValues[Matrix.MTRANS_X];
        result.top = (int) matrixValues[Matrix.MTRANS_Y];
        result.right = (int) (result.left + drawableRect.width() * matrixValues[Matrix.MSCALE_X]);
        result.bottom = (int) (result.top + drawableRect.height() * matrixValues[Matrix.MSCALE_Y]);

        return result;
    }

    public static float[] calculateRatios(Rect startBounds, Rect endBounds) {
        float[] result = new float[2];
        float widthRatio = startBounds.width() * 1.0f / endBounds.width() * 1.0f;
        float heightRatio = startBounds.height() * 1.0f / endBounds.height() * 1.0f;
        result[0] = widthRatio;
        result[1] = heightRatio;
        return result;
    }


    public interface OnAnimaEndListener {
        void onAnimaEnd(Animator animator);
    }
}
