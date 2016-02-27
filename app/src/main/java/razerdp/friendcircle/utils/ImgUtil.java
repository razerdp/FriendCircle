package razerdp.friendcircle.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by 大灯泡 on 2016/2/27.
 * 图片工具类
 */
public class ImgUtil {
    public static Bitmap ScaleBitmap(Bitmap bm, int outputX, int outputY) {
        float oldWidth = bm.getWidth();
        float oldHeight = bm.getHeight();
        float scale = oldWidth / oldHeight;

        float newWidth = oldWidth;
        float newHeight = oldHeight;
        boolean isScale = false;
        if (scale >= 1.0f) {
            if (oldWidth > outputX) {
                newWidth = outputX;
                newHeight = (newWidth / scale);
                isScale = true;
            }
        } else {
            if (oldHeight > outputY) {
                newHeight = outputY;
                newWidth = (newHeight * scale);
                isScale = true;
            }
        }
        if (isScale) {
            final Matrix matrix = new Matrix();
            matrix.postScale(newWidth / oldWidth, newHeight / oldHeight);
            // matrix.postRotate(rotate);
            final Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0,
                    (int) oldWidth, (int) oldHeight, matrix, true);
            bm.recycle();
            bm = rotatedBitmap;
        }
        return bm;
    }
}
