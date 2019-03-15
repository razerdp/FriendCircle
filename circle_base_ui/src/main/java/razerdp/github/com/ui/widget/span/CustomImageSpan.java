package razerdp.github.com.ui.widget.span;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * 垂直居中的ImageSpan
 *
 * @author KenChung
 */
public class CustomImageSpan extends ImageSpan {

    private boolean aequilate = true;
    private Rect mRect = new Rect();

    public CustomImageSpan(Drawable drawable) {
        super(drawable);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
    }

    public CustomImageSpan(Context context, int resID) {
        super(context, resID, ALIGN_BASELINE);
    }

    public boolean isAequilate() {
        return aequilate;
    }

    public CustomImageSpan setAequilate(boolean aequilate) {
        this.aequilate = aequilate;
        return this;
    }

    public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fontMetricsInt) {
        Drawable drawable = getDrawable();
        if (mRect.isEmpty()) {
            mRect.set(drawable.getBounds());
        }
        Rect rect = drawable.getBounds();
        if (fontMetricsInt != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            if (aequilate) {
                float scale = (float) (Math.abs(fontMetricsInt.ascent) + Math.abs(fontMetricsInt.descent)) / mRect.height();
                drawable.setBounds(0, 0, (int) (mRect.width() * scale), (int) (mRect.height() * scale));
                rect = drawable.getBounds();
            }
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;

            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;

            fontMetricsInt.ascent = -bottom;
            fontMetricsInt.top = -bottom;
            fontMetricsInt.bottom = top;
            fontMetricsInt.descent = top;
        }
        return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        Drawable drawable = getDrawable();
        canvas.save();
        int transY = 0;
        transY = ((bottom - top) - drawable.getBounds().bottom) / 2 + top;
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }
}