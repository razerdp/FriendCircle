package razerdp.github.com.ui.widget.textview;

import android.graphics.Outline;
import android.graphics.RectF;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by 大灯泡 on 2018/3/9.
 */
public class FixedOvalShape extends OvalShape {
    private int strokeWidth;
    private int horizontalReduce, verticalReduce;

    public FixedOvalShape(int strokeWidth, int horizontalReduce, int verticalReduce) {
        this.strokeWidth = strokeWidth;
        this.horizontalReduce = horizontalReduce;
        this.verticalReduce = verticalReduce;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void getOutline(Outline outline) {
        if (strokeWidth != 0) return;
    }

    @Override
    protected void onResize(float width, float height) {
        super.onResize(width, height);
        RectF r = rect();
        r.inset(horizontalReduce, verticalReduce);
    }
}
