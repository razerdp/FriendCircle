/*
	Copyright (C) 2013 Make Ramen, LLC
 */
package razerdp.github.com.ui.widget.imageview;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.widget.ImageView.ScaleType;

public class RoundedDrawable extends Drawable {
    public static final String TAG = "RoundedDrawable";

    public static final int DEFAULT_BORDER_COLOR = Color.BLACK;

    private final RectF mBounds = new RectF();

    private final RectF mDrawableRect = new RectF();
    private float mCornerRadius;

    private final RectF mBitmapRect = new RectF();
    private final BitmapShader mBitmapShader;
    private final Paint mBitmapPaint;
    private final int mBitmapWidth;
    private final int mBitmapHeight;

    private final RectF mBorderRect = new RectF();
    private final Paint mBorderPaint;
    private boolean mOval = false;
    private float mBorderWidth;
    private ColorStateList mBorderColor;
    private boolean mIsNoBottomRadius = false;

    private Bitmap bitmap;

    private ScaleType mScaleType = ScaleType.FIT_XY;

    private final Matrix mShaderMatrix = new Matrix();

    RoundedDrawable(Bitmap bitmap, float cornerRadius, int border, ColorStateList borderColor) {
        this(bitmap, cornerRadius, border, borderColor, false);
    }

    RoundedDrawable(Bitmap bitmap, float cornerRadius, int border, ColorStateList borderColor, boolean oval) {

        setOval(oval);
        mBorderWidth = border;
        mBorderColor = borderColor != null ? borderColor : ColorStateList.valueOf(DEFAULT_BORDER_COLOR);

        mBitmapWidth = bitmap.getWidth();
        mBitmapHeight = bitmap.getHeight();
        mBitmapRect.set(0, 0, mBitmapWidth, mBitmapHeight);
        this.bitmap = bitmap;

        mCornerRadius = cornerRadius;
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapShader.setLocalMatrix(mShaderMatrix);

        mBitmapPaint = new Paint();
        mBitmapPaint.setStyle(Paint.Style.FILL);
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);

        mBitmapPaint.setColor(0xFFFFFF00);
        mBitmapPaint.setShadowLayer(150, 13, 13, 0x000000);


        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor.getColorForState(getState(), DEFAULT_BORDER_COLOR));
        mBorderPaint.setStrokeWidth(border);

    }

    @Override
    public boolean isStateful() {
        return mBorderColor.isStateful();
    }

    @Override
    protected boolean onStateChange(int[] state) {
        int newColor = mBorderColor.getColorForState(state, 0);
        if (mBorderPaint.getColor() != newColor) {
            mBorderPaint.setColor(newColor);
            return true;
        } else {
            return super.onStateChange(state);
        }
    }

    protected void setScaleType(ScaleType scaleType) {
        if (scaleType == null) {
            scaleType = ScaleType.FIT_XY;
        }
        if (mScaleType != scaleType) {
            mScaleType = scaleType;
            setMatrix();
        }
    }

    protected ScaleType getScaleType() {
        return mScaleType;
    }

    private void setMatrix() {
        mBorderRect.set(mBounds);
        mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);

        float scale;
        float dx;
        float dy;

        switch (mScaleType) {
            case CENTER:
                mBorderRect.set(mBounds);
                mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);

                mShaderMatrix.set(null);
                mShaderMatrix.setTranslate((int) ((mDrawableRect.width() - mBitmapWidth) * 0.5f + 0.5f), (int) ((mDrawableRect.height() - mBitmapHeight) * 0.5f + 0.5f));
                break;
            case CENTER_CROP:
                mBorderRect.set(mBounds);
                mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);

                mShaderMatrix.set(null);

                dx = 0;
                dy = 0;

                if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
                    scale = mDrawableRect.height() / mBitmapHeight;
                    dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
                } else {
                    scale = mDrawableRect.width() / mBitmapWidth;
                    dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
                }

                mShaderMatrix.setScale(scale, scale);
                mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);
                break;
            case CENTER_INSIDE:
                mShaderMatrix.set(null);

                if (mBitmapWidth <= mBounds.width() && mBitmapHeight <= mBounds.height()) {
                    scale = 1.0f;
                } else {
                    scale = Math.min(mBounds.width() / mBitmapWidth, mBounds.height() / mBitmapHeight);
                }

                dx = (int) ((mBounds.width() - mBitmapWidth * scale) * 0.5f + 0.5f);
                dy = (int) ((mBounds.height() - mBitmapHeight * scale) * 0.5f + 0.5f);

                mShaderMatrix.setScale(scale, scale);
                mShaderMatrix.postTranslate(dx, dy);

                mBorderRect.set(mBitmapRect);
                mShaderMatrix.mapRect(mBorderRect);
                mDrawableRect.set(mBorderRect.left + mBorderWidth, mBorderRect.top + mBorderWidth, mBorderRect.right - mBorderWidth, mBorderRect.bottom - mBorderWidth);
                mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
                break;
            case FIT_CENTER:
                mBorderRect.set(mBitmapRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.CENTER);
                mShaderMatrix.mapRect(mBorderRect);
                mDrawableRect.set(mBorderRect.left + mBorderWidth, mBorderRect.top + mBorderWidth, mBorderRect.right - mBorderWidth, mBorderRect.bottom - mBorderWidth);
                mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
                break;
            case FIT_END:
                mBorderRect.set(mBitmapRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.END);
                mShaderMatrix.mapRect(mBorderRect);
                mDrawableRect.set(mBorderRect.left + mBorderWidth, mBorderRect.top + mBorderWidth, mBorderRect.right - mBorderWidth, mBorderRect.bottom - mBorderWidth);
                mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
                break;
            case FIT_START:
                mBorderRect.set(mBitmapRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.START);
                mShaderMatrix.mapRect(mBorderRect);
                mDrawableRect.set(mBorderRect.left + mBorderWidth, mBorderRect.top + mBorderWidth, mBorderRect.right - mBorderWidth, mBorderRect.bottom - mBorderWidth);
                mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
                break;
            case FIT_XY:
            default:
                mBorderRect.set(mBounds);
                mDrawableRect.set(0 + mBorderWidth, 0 + mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
                mShaderMatrix.set(null);
                mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
                break;
        }

        mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        mBounds.set(bounds);

        setMatrix();
    }

    @Override
    public void draw(Canvas canvas) {

        if (mOval) {
            if (mBorderWidth > 0) {
                canvas.drawOval(mBorderRect, mBorderPaint);
                canvas.drawOval(mDrawableRect, mBitmapPaint);
            } else {
                canvas.drawOval(mDrawableRect, mBitmapPaint);
            }
        } else {
            if (mBorderWidth > 0) {
                canvas.drawRoundRect(mBorderRect, mCornerRadius, mCornerRadius, mBorderPaint);
                canvas.drawRoundRect(mDrawableRect, Math.max(mCornerRadius - mBorderWidth, 0), Math.max(mCornerRadius - mBorderWidth, 0), mBitmapPaint);
            } else {
                canvas.drawRoundRect(mDrawableRect, mCornerRadius, mCornerRadius, mBitmapPaint);
                //下面两句是使view的底下两个角没有圆角效果
                if (mIsNoBottomRadius) {
                    Rect bottomRect = new Rect(0, (int) (mDrawableRect.height() / 2), (int) mDrawableRect.width(), (int) mDrawableRect.height());
                    canvas.drawRect(bottomRect, mBitmapPaint);
                }
//				canvas.drawRoundRect(mDrawableRect, 200, 200, mBitmapPaint);
            }
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
        mBitmapPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mBitmapPaint.setColorFilter(cf);
        invalidateSelf();
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmapWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmapHeight;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap;
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (width > 0 && height > 0) {
            bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } else {
            bitmap = null;
        }

        return bitmap;
    }

    public static Drawable fromDrawable(Drawable drawable, float radius) {
        return fromDrawable(drawable, radius, 0, ColorStateList.valueOf(DEFAULT_BORDER_COLOR), false);
    }

    public static Drawable fromDrawable(Drawable drawable, float radius, int border, ColorStateList borderColor, boolean isOval) {
        if (drawable != null) {
            if (drawable instanceof TransitionDrawable) {
                TransitionDrawable td = (TransitionDrawable) drawable;
                int num = td.getNumberOfLayers();

                Drawable[] drawableList = new Drawable[num];
                for (int i = 0; i < num; i++) {
                    Drawable d = td.getDrawable(i);
                    if (d instanceof ColorDrawable) {
                        drawableList[i] = d;
                    } else {
                        drawableList[i] = new RoundedDrawable(drawableToBitmap(td.getDrawable(i)), radius, border, borderColor, isOval);
                    }
                }
                return new TransitionDrawable(drawableList);
            }

            Bitmap bm = drawableToBitmap(drawable);
            if (bm != null) {
                return new RoundedDrawable(bm, radius, border, borderColor, isOval);
            } else {
            }
        }
        return drawable;
    }

    public static RoundedDrawable fromBitmap(Bitmap bm, float radius) {
        return fromBitmap(bm, radius, false);
    }

    public static RoundedDrawable fromBitmap(Bitmap bm, float radius, boolean isOval) {
        return new RoundedDrawable(bm, radius, 0, null, isOval);
    }

    public float getCornerRadius() {
        return mCornerRadius;
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }

    public int getBorderColor() {
        return mBorderColor.getDefaultColor();
    }

    public ColorStateList getBorderColors() {
        return mBorderColor;
    }

    public boolean isOval() {
        return mOval;
    }

    public void setCornerRadius(float radius) {
        mCornerRadius = radius;
    }

    public void setBorderWidth(int width) {
        mBorderWidth = width;
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }

    public void setBorderColor(int color) {
        setBorderColors(ColorStateList.valueOf(color));
    }

    public void setBorderColors(ColorStateList colors) {
        mBorderColor = colors != null ? colors : ColorStateList.valueOf(0);
        mBorderPaint.setColor(mBorderColor.getColorForState(getState(), DEFAULT_BORDER_COLOR));
    }

    public void setOval(boolean oval) {
        mOval = oval;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public boolean ismIsNoBottomRadius() {
        return mIsNoBottomRadius;
    }

    public void setmIsNoBottomRadius(boolean mIsNoBottomRadius) {
        this.mIsNoBottomRadius = mIsNoBottomRadius;
    }


}