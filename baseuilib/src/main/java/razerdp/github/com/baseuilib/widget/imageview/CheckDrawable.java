package razerdp.github.com.baseuilib.widget.imageview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

import com.socks.library.KLog;

import razerdp.github.com.baselibrary.utils.ui.AnimUtils;
import razerdp.github.com.baseuilib.R;
import razerdp.github.com.baseuilib.interpolator.SpringInterpolator;

import static android.R.attr.animation;

/**
 * Created by 大灯泡 on 2017/3/24.
 */

public class CheckDrawable extends BitmapDrawable {
    private static final String TAG = "CheckDrawable";
    private static final int CHECK_ICON = R.drawable.ic_check;
    boolean isSelect;

    ValueAnimator animator;

    private Paint bgPaint;
    private Paint strokePaint;
    private Paint fillPaint;
    private boolean needAnima;
    private boolean isPlayingAnima;

    private int animaRadius = 0;

    public CheckDrawable(Context context) {
        super(context.getResources(), BitmapFactory.decodeResource(context.getResources(), CHECK_ICON));
        initPaint(context);
        animator = ValueAnimator.ofFloat(1.0f);
        animator.setDuration(1000);
        animator.setInterpolator(new SpringInterpolator());
    }

    private void initPaint(Context context) {
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(Color.argb(75, 0, 0, 0));
        bgPaint.setStyle(Paint.Style.FILL);

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(Color.WHITE);
        strokePaint.setStyle(Paint.Style.STROKE);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(context.getResources().getColor(R.color.wechat_green_bg));
        fillPaint.setStyle(Paint.Style.FILL);
    }

    public void setSelected(boolean select) {
        setSelected(select, needAnima);
    }

    public void setSelected(boolean select, boolean needAnima) {
        this.isSelect = select;
        this.needAnima = needAnima;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public boolean toggleSelected() {
        setSelected(!isSelect, true);
        return isSelect;
    }


    @Override
    public void draw(Canvas canvas) {
        if (isSelect) {
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
            KLog.i(TAG, "draw in drawable with anima");
            if (!isPlayingAnima) {
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float animaValue = (float) animation.getAnimatedValue();
                        animaRadius = (int) (radius * animaValue);
                        invalidateSelf();
                    }
                });
                animator.addListener(new AnimUtils.SimpleAnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isPlayingAnima = true;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        reset();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        reset();
                    }

                    void reset() {
                        isPlayingAnima = false;
                        needAnima = false;
                        animator.removeAllUpdateListeners();
                        animator.removeListener(this);
                        animaRadius = 0;
                        KLog.i(TAG, "selected  >>  " + isSelect);
                    }
                });
                animator.start();
            } else {
                if (animaRadius > 0) {
                    canvas.drawCircle(cx, cy, animaRadius, fillPaint);
                }
            }
        } else {
            KLog.i(TAG, "draw in drawable without anima");
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
