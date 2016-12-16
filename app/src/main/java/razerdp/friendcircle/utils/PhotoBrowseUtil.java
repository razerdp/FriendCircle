package razerdp.friendcircle.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by 大灯泡 on 2016/12/16.
 * <p>
 * 图片浏览工具类
 */
public class PhotoBrowseUtil {


    public static void playEnterAnima(View targetView, Rect startRect, Rect endRect, Point globalOffset, @Nullable final OnAnimaEndListener onAnimaEndListener) {
        if (targetView == null || startRect == null || endRect == null || globalOffset == null) return;
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

        enterSet.setDuration(450);
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


    public static void playExitAnima(View targetView, View alphaView, Rect startRect, Rect endRect, @Nullable final OnAnimaEndListener onAnimaEndListener) {
        if (targetView == null || startRect == null || endRect == null ) return;

        final Rect tStartRect = new Rect(startRect);
        final Rect tEndRect = new Rect(endRect);

        float[] scaleRatios = calculateRatios(tStartRect, tEndRect);

        targetView.setPivotX(0.5f);
        targetView.setPivotY(0.5f);


        int offsetWidth = (int) (tEndRect.left * scaleRatios[0]);
        int offsetHeight = (int) (tEndRect.top * scaleRatios[1]);

        final AnimatorSet exitSet = new AnimatorSet();

        AnimatorSet.Builder builder = exitSet.play(ObjectAnimator.ofFloat(targetView, View.X, startRect.left - offsetWidth))
                                             .with(ObjectAnimator.ofFloat(targetView, View.Y, startRect.top - offsetHeight))
                                             .with(ObjectAnimator.ofFloat(targetView, View.SCALE_X, scaleRatios[0]))
                                             .with(ObjectAnimator.ofFloat(targetView, View.SCALE_Y, scaleRatios[1]));
        if (alphaView != null) {
            builder.with(ObjectAnimator.ofFloat(alphaView, View.ALPHA, 1f, 0));
        }
        exitSet.setDuration(450);
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
