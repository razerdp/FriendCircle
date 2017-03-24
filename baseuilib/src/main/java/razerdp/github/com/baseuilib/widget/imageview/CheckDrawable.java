package razerdp.github.com.baseuilib.widget.imageview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.animation.BounceInterpolator;

import com.socks.library.KLog;

import razerdp.github.com.baselibrary.utils.ui.AnimUtils;
import razerdp.github.com.baseuilib.R;

/**
 * Created by 大灯泡 on 2017/3/24.
 */

public class CheckDrawable extends BitmapDrawable {
    private static final String TAG = "CheckDrawable";
    private static final int CHECK_ICON = R.drawable.ic_check;
    boolean select;

    ValueAnimator animator;

    private Paint bgPaint;
    private Paint strokePaint;
    private Paint fillPaint;
    private boolean needAnima;
    private boolean isPlayingAnima;

    public CheckDrawable(Context context) {
        super(context.getResources(), BitmapFactory.decodeResource(context.getResources(), CHECK_ICON));
        initPaint(context);
        animator = ValueAnimator.ofFloat(1.0f);
        animator.setDuration(500);
        animator.setInterpolator(new BounceInterpolator());
    }

    private void initPaint(Context context) {
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(Color.argb(75, 0, 0, 0));
        bgPaint.setStyle(Paint.Style.FILL);

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(Color.WHITE);
        strokePaint.setStyle(Paint.Style.STROKE);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(0xff10d21c);
        fillPaint.setStyle(Paint.Style.FILL);
    }

    public void setSelected(boolean select) {
        setSelected(select, needAnima);
    }

    public void setSelected(boolean select, boolean needAnima) {
        this.select = select;
        this.needAnima = needAnima;
    }

    public boolean toggleSelected() {
        boolean needAnima = !select;
        setSelected(!select, needAnima);
        return select;
    }


    @Override
    public void draw(Canvas canvas) {
        KLog.i(TAG, "bounds  >>  " + getBounds().toShortString());
        if (select) {
            drawSelected(canvas);
        } else {
            drawNormal(canvas);
        }
        super.draw(canvas);

    }

    private void drawSelected(final Canvas canvas) {
        final int cx = getBounds().centerX();
        final int cy = getBounds().centerY();
        final int radius = getBounds().width() / 2;
        if (needAnima) {
            if (!isPlayingAnima) {
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float animaValue = (float) animation.getAnimatedValue();
                        KLog.i(TAG, animaValue, radius * animaValue);
                        KLog.i("draw", "内部的draw");
                        canvas.drawCircle(cx, cy, radius * animaValue, fillPaint);
                    }
                });
                animator.addListener(new AnimUtils.SimpleAnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isPlayingAnima = true;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        isPlayingAnima = false;
                        needAnima = false;
                        animator.removeAllUpdateListeners();
                        animation.removeListener(this);
                        KLog.i(TAG, "selected  >>  " + select);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isPlayingAnima = false;
                        needAnima = false;
                        animator.removeAllUpdateListeners();
                        animation.removeListener(this);

                        KLog.i(TAG, "selected  >>  " + select);
                    }
                });
                animator.start();
            }
        } else {
            canvas.drawCircle(cx, cy, radius, fillPaint);
        }

    }

    private void drawNormal(Canvas canvas) {
        int cx = getBounds().centerX();
        int cy = getBounds().centerY();
        int radius = getBounds().width() / 2;
        int strokeRadius = radius + 2;
        canvas.drawCircle(cx, cy, radius, bgPaint);
        canvas.drawCircle(cx, cy, strokeRadius, strokePaint);
    }

}
