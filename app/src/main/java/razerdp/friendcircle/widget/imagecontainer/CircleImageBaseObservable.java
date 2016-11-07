package razerdp.friendcircle.widget.imagecontainer;

/**
 * Created by 大灯泡 on 2016/11/7.
 *
 *
 */
public class CircleImageBaseObservable extends Observable<CircleImageDataObserver> {

    public void notifyChanged() {
        synchronized(mObservers) {
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
