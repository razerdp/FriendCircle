package razerdp.friendcircle.widget.circleimagecontainer.util;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

/**
 * Created by 大灯泡 on 2016/11/9.
 */

public class ImageRect {
    private RectF rect;

    public ImageRect(ImageView imageview) {
        rect = new RectF();
        if (imageview != null && imageview.getDrawable() != null) {
            Rect drawableRect = imageview.getDrawable().getBounds();
            Matrix imgMatrix = imageview.getImageMatrix();
            float[] matrixValues = new float[9];
            imgMatrix.getValues(matrixValues);
            rect.left = matrixValues[2];
            rect.top = matrixValues[5];
            rect.right = rect.left + drawableRect.width() * matrixValues[0];
            rect.bottom = rect.top + drawableRect.height() * matrixValues[0];
        }
    }

    public boolean isImageEmpty() {
        return rect.isEmpty();
    }

    public RectF getImageRect() {
        return rect;
    }
}
