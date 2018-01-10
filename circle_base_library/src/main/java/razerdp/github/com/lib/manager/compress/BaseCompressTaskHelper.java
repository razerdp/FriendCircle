package razerdp.github.com.lib.manager.compress;

import android.content.Context;
import android.os.Looper;

import razerdp.github.com.lib.api.AppContext;
import razerdp.github.com.lib.thirdpart.WeakHandler;

/**
 * Created by 大灯泡 on 2018/1/10.
 */
abstract class BaseCompressTaskHelper<T> {
    protected String TAG = this.getClass().getSimpleName();
    protected BaseCompressListener mBaseCompressListener;
    protected T data;
    protected Context mContext;
    WeakHandler mWeakHandler;

    public BaseCompressTaskHelper(Context context, T options, BaseCompressListener baseCompressListener) {
        mContext = context;
        data = options;
        mBaseCompressListener = baseCompressListener;
        mWeakHandler = new WeakHandler(Looper.getMainLooper());
    }

    public abstract void start();


    public void callSuccess(final Object imagePath) {

        if (mBaseCompressListener == null) return;
        if (AppContext.isMainThread()) {
            mBaseCompressListener.onSuccess(imagePath);
        } else {
            mWeakHandler.post(new Runnable() {
                @Override
                public void run() {
                    mBaseCompressListener.onSuccess(imagePath);
                }
            });
        }
    }

    public void callError(final String message) {
        if (mBaseCompressListener == null) return;
        if (AppContext.isMainThread()) {
            mBaseCompressListener.onError(message);
        } else {
            mWeakHandler.post(new Runnable() {
                @Override
                public void run() {
                    mBaseCompressListener.onError(message);
                }
            });
        }
    }

    public void callCompress(final int cur, final int target) {
        if (mBaseCompressListener == null) return;
        if (AppContext.isMainThread()) {
            mBaseCompressListener.onCompress(cur, target);
        } else {
            mWeakHandler.post(new Runnable() {
                @Override
                public void run() {
                    mBaseCompressListener.onCompress(cur, target);
                }
            });
        }

    }
}
