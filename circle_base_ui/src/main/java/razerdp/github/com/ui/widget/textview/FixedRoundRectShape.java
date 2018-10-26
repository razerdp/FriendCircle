package razerdp.github.com.ui.widget.textview;

import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

/**
 * Created by 大灯泡 on 2018/3/9.
 */
public class FixedRoundRectShape extends RectShape {
    private float[] mOuterRadii;
    private int strokeWidth;
    private Path mPath;
    private int horizontalReduce, verticalReduce;

    private boolean shadow = true;

    public FixedRoundRectShape setShadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    public FixedRoundRectShape(@Nullable float[] outerRadii, int strokeWidth, int horizontalReduce, int verticalReduce) {
        if (outerRadii != null && outerRadii.length < 8) {
            throw new ArrayIndexOutOfBoundsException("outer radii must have >= 8 values");
        }
        this.strokeWidth = strokeWidth;
        this.horizontalReduce = horizontalReduce;
        this.verticalReduce = verticalReduce;
        mOuterRadii = outerRadii;
        mPath = new Path();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawPath(mPath, paint);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void getOutline(Outline outline) {
        if (strokeWidth != 0 || !shadow) return;
        float radius = 0;
        if (mOuterRadii != null) {
            radius = mOuterRadii[0];
            for (int i = 1; i < 8; i++) {
                if (mOuterRadii[i] != radius) {
                    // can't call simple constructors, use path
                    outline.setConvexPath(mPath);
                    return;
                }
            }
        }

        final RectF rect = rect();
        outline.setRoundRect((int) Math.ceil(rect.left), (int) Math.ceil(rect.top),
                (int) Math.floor(rect.right), (int) Math.floor(rect.bottom),
                radius);
    }

    @Override
    protected void onResize(float width, float height) {
        super.onResize(width, height);
        RectF r = rect();
        r.inset(strokeWidth / 2 + horizontalReduce, strokeWidth / 2 + verticalReduce);

        mPath.reset();
        if (mOuterRadii != null) {
            mPath.addRoundRect(r, mOuterRadii, Path.Direction.CW);
        } else {
            mPath.addRect(r, Path.Direction.CW);
        }

    }


}
