package razerdp.friendcircle.widget.circleimagecontainer.adapter.observer;

/**
 * Created by 大灯泡 on 2016/11/9.
 */

public class CircleAdapterObservable extends CircleImageObservable<CircleBaseDataObserver> {

    public void notifyChanged() {
        synchronized (mObservers) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged();
            }
        }
    }

    public void notifyInvalidated() {
        synchronized (mObservers) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onInvalidated();
            }
        }
    }
}
