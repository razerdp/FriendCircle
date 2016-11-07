package razerdp.friendcircle.widget.imagecontainer;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by 大灯泡 on 2016/11/7.
 */

public abstract class BaseCircleImageAdapter implements CircleImageAdapter {


    private final CircleImageBaseObservable mCircleDataObservable=new CircleImageBaseObservable();



    public abstract ImageView getView(ImageView convertView, ViewGroup parent, int position);

    public abstract int getCount();

    public abstract void onItemClick(View convertView, int position, Rect visibleRect, Rect[] allItemRects);

    @Override
    public void registerDataSetObserver(CircleImageDataObserver observer) {
        mCircleDataObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(CircleImageDataObserver observer) {
        mCircleDataObservable.unregisterObserver(observer);
    }

    public void notifyDataChanged(){
        mCircleDataObservable.notifyChanged();
        mCircleDataObservable.notifyInvalidated();
    }
}
