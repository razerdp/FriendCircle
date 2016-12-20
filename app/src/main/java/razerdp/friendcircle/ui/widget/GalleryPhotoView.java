package razerdp.friendcircle.ui.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
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


    public void playEnterAnima(Rect from) {
        getImageMatrix().reset();

        Rect mine = getMineRect();

        float scaleX = (float) from.width() / mine.width();
        float scaleY = (float) from.height() / mine.height();
        float scale = Math.min(scaleX, scaleY);

        postScale(scale, from.centerX(), from.centerY());
        applyMatrix();

   /*     int dx = from.left;
        int dy = from.top;

//        viewTransfrom.animaTranslate(dx, dy);
        viewTransfrom.animaScale(scale, 1.0f, from.centerX(), from.centerY());

        viewTransfrom.start();*/

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
