package razerdp.friendcircle.widget.ptrwidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * Created by 大灯泡 on 2016/2/21.
 * 自定义imgspan
 */
public class CustomImageSpan extends ImageSpan {

    public CustomImageSpan(Drawable drawable) {
        super(drawable);
    }

    public CustomImageSpan(Context context, int resID) {
        super(context, resID, ALIGN_BASELINE);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable drawable = getDrawable();
        canvas.save();
        int transY;
        //居中
        transY = ((bottom - top) - drawable.getBounds().bottom) / 2 + top;
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }
}
