package razerdp.friendcircle.widget.imagecontainer;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 大灯泡 on 2016/11/7.
 */

interface CircleImageAdapter {

    void registerDataSetObserver(CircleImageDataObserver observer);

    void unregisterDataSetObserver(CircleImageDataObserver observer);

}
