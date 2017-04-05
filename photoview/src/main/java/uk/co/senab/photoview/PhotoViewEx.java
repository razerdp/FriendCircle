package uk.co.senab.photoview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher.OnMatrixChangedListener;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

/**
 * Created by 大灯泡 on 2017/4/1.
 * <p>
 * 进一步扩展photoview
 */

public class PhotoViewEx extends ImageView implements IPhotoView {

    private final PhotoViewAttacher mAttacher;

    private ScaleType mPendingScaleType;

    /**
     * 因为glide会调用setImageDrawable
     * 而restore无法确保在attach.update之后调用
     * 因此加入两个标志位
     * hasSaveScaleState确保点击原图不会导致黑屏
     * needRestoreScaleState确保在update之后恢复原来的缩放
     */
    private boolean hasSaveScaleState;
    private boolean needRestoreScaleState;

    /**
     * 此项为另外添加，为了让viewpager的destroyItem方法不会让photoview的缩放功能失效(
     * 默认是cleanup在DetachedFromWindow的时候
     * viewpager是需要即使DetachedFromWindow也不cleanup)，
     * 如果是除了viewpager以外的地方使用photoview，请勿设置此项目,
     */
    private boolean isCacheInViewPager = false;

    public PhotoViewEx(Context context) {
        this(context, null);
    }

    public PhotoViewEx(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public PhotoViewEx(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        super.setScaleType(ScaleType.MATRIX);
        mAttacher = new PhotoViewAttacher(this);

        if (null != mPendingScaleType) {
            setScaleType(mPendingScaleType);
            mPendingScaleType = null;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
            Log.e("photoview", e.getMessage(), e);
            Log.e("photoview", "图片被回收，在此处被捕捉，防止崩溃");
            setImageDrawable(null);
            System.gc();
        }
    }

    /**
     * @deprecated use {@link #setRotationTo(float)}
     */
    public void setPhotoViewRotation(float rotationDegree) {
        mAttacher.setRotationTo(rotationDegree);
    }

    @Override
    public void setRotationTo(float rotationDegree) {
        mAttacher.setRotationTo(rotationDegree);
    }

    @Override
    public void setRotationBy(float rotationDegree) {
        mAttacher.setRotationBy(rotationDegree);
    }

    @Override
    public boolean canZoom() {
        return mAttacher.canZoom();
    }

    @Override
    public RectF getDisplayRect() {
        return mAttacher.getDisplayRect();
    }


    @Override
    public boolean setDisplayMatrix(Matrix finalRectangle) {
        return mAttacher.setDisplayMatrix(finalRectangle);
    }

    @Override
    public void getDisplayMatrix(Matrix matrix) {
        mAttacher.getDisplayMatrix(matrix);
    }


    @Override
    public float getMinimumScale() {
        return mAttacher.getMinimumScale();
    }


    @Override
    public float getMediumScale() {
        return mAttacher.getMediumScale();
    }

    @Override
    public float getMaximumScale() {
        return mAttacher.getMaximumScale();
    }

    @Override
    public float getScale() {
        return mAttacher.getScale();
    }

    @Override
    public ScaleType getScaleType() {
        return mAttacher.getScaleType();
    }

    @Override
    public void setAllowParentInterceptOnEdge(boolean allow) {
        mAttacher.setAllowParentInterceptOnEdge(allow);
    }


    @Override
    public void setMinimumScale(float minimumScale) {
        mAttacher.setMinimumScale(minimumScale);
    }


    @Override
    public void setMediumScale(float mediumScale) {
        mAttacher.setMediumScale(mediumScale);
    }


    @Override
    public void setMaximumScale(float maximumScale) {
        mAttacher.setMaximumScale(maximumScale);
    }

    @Override
    public void setScaleLevels(float minimumScale, float mediumScale, float maximumScale) {

    }

    @Override
    // setImageBitmap calls through to this method
    public void setImageDrawable(Drawable drawable) {
        if (hasSaveScaleState) return;
        super.setImageDrawable(drawable);
        if (null != mAttacher) {
            mAttacher.update();
        }
        if (needRestoreScaleState) {
            restoreScaleState();
            needRestoreScaleState = false;
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        if (null != mAttacher) {
            mAttacher.update();
        }
        hasSaveScaleState = false;
        needRestoreScaleState = false;
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        if (null != mAttacher) {
            mAttacher.update();
        }
    }

    @Override
    public void setOnMatrixChangeListener(OnMatrixChangedListener listener) {
        mAttacher.setOnMatrixChangeListener(listener);
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        mAttacher.setOnLongClickListener(l);
    }

    @Override
    public void setOnPhotoTapListener(OnPhotoTapListener listener) {
        mAttacher.setOnPhotoTapListener(listener);
    }

    public OnPhotoTapListener getOnPhotoTapListener() {
        return mAttacher.getOnPhotoTapListener();
    }

    @Override
    public void setOnViewTapListener(OnViewTapListener listener) {
        mAttacher.setOnViewTapListener(listener);
    }

    public OnViewTapListener getOnViewTapListener() {
        return mAttacher.getOnViewTapListener();
    }

    @Override
    public void setScale(float scale) {
        mAttacher.setScale(scale);
    }

    @Override
    public void setScale(float scale, boolean animate) {
        mAttacher.setScale(scale, animate);
    }

    @Override
    public void setScale(float scale, float focalX, float focalY, boolean animate) {
        mAttacher.setScale(scale, focalX, focalY, animate);
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (null != mAttacher) {
            mAttacher.setScaleType(scaleType);
        } else {
            mPendingScaleType = scaleType;
        }
    }

    @Override
    public void setZoomable(boolean zoomable) {
        mAttacher.setZoomable(zoomable);
    }

    @Override
    public Bitmap getVisibleRectangleBitmap() {
        return mAttacher.getVisibleRectangleBitmap();
    }

    @Override
    public void setZoomTransitionDuration(int milliseconds) {
        mAttacher.setZoomTransitionDuration(milliseconds);
    }

    @Override
    public IPhotoView getIPhotoViewImplementation() {
        return mAttacher;
    }

    @Override
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener newOnDoubleTapListener) {
        mAttacher.setOnDoubleTapListener(newOnDoubleTapListener);
    }

    @Override
    public void setOnScaleChangeListener(PhotoViewAttacher.OnScaleChangeListener onScaleChangeListener) {
        mAttacher.setOnScaleChangeListener(onScaleChangeListener);
    }

    @Override
    public void setOnSingleFlingListener(PhotoViewAttacher.OnSingleFlingListener onSingleFlingListener) {
        mAttacher.setOnSingleFlingListener(onSingleFlingListener);

    }

    public boolean isCacheInViewPager() {
        return isCacheInViewPager;
    }

    public void setCacheInViewPager(boolean isCleanOnDetachedFromWindow) {
        this.isCacheInViewPager = isCleanOnDetachedFromWindow;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (!isCacheInViewPager) {
            mAttacher.cleanup();
        }
        super.onDetachedFromWindow();
    }


    /**
     * =============================================================新增方法：用于在加载原图的时候可以保持原来放大的位置而非再次以原来的大小
     */
    //url:https://github.com/chrisbanes/PhotoView/issues/318
    float preScale;
    float xPrev;
    float yPrev;

    /**
     * 保存原来的放大位置，请在加载原图之前保存
     */
    public void saveScaleState() {
        if (mAttacher == null) return;
        hasSaveScaleState = true;
        needRestoreScaleState = true;
        preScale = getScale();
        RectF rect = mAttacher.getDisplayRect();
        if (rect == null) return;
        xPrev = (float) (this.getWidth() / 2.0 - rect.left);
        yPrev = (float) (this.getHeight() / 2.0 - rect.top);
    }


    /**
     * 加载原图后恢复
     */
    public void restoreScaleState() {
        if (mAttacher == null) return;
        hasSaveScaleState = false;
        mAttacher.setScale(preScale, false);
        RectF rect = mAttacher.getDisplayRect();
        if (rect == null) return;
        float xAfter = (float) (this.getWidth() / 2.0 - rect.left);
        float yAfter = (float) (this.getHeight() / 2.0 - rect.top);
        mAttacher.onDrag(xAfter - xPrev, yAfter - yPrev);
    }

}
