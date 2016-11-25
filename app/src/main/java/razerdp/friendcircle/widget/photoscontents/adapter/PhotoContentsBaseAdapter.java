package razerdp.friendcircle.widget.photoscontents.adapter;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import razerdp.friendcircle.widget.photoscontents.adapter.observer.PhotoAdapterObservable;
import razerdp.friendcircle.widget.photoscontents.adapter.observer.PhotoBaseDataObserver;


/**
 * Created by 大灯泡 on 2016/11/9.
 */

public abstract class PhotoContentsBaseAdapter {


    private PhotoAdapterObservable mObservable = new PhotoAdapterObservable();


    public void registerDataSetObserver(PhotoBaseDataObserver observer) {
        mObservable.registerObserver(observer);

    }

    public void unregisterDataSetObserver(PhotoBaseDataObserver observer) {
        mObservable.unregisterObserver(observer);
    }

    public void notifyDataChanged() {
        mObservable.notifyChanged();
        mObservable.notifyInvalidated();
    }


    public abstract ImageView onCreateView(ImageView convertView, ViewGroup parent, int position);

    public abstract void onBindData(int position, @NonNull ImageView convertView);

    public abstract int getCount();

    public abstract void onItemClick(View convertView, int position, Rect visibleRect, Rect[] allItemRects);
}
