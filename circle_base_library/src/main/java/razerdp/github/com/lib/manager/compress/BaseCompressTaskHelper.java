package razerdp.github.com.lib.manager.compress;

import android.content.Context;
import android.os.Looper;

import java.util.List;

import razerdp.github.com.lib.api.AppContext;
import razerdp.github.com.lib.thirdpart.WeakHandler;

/**
 * Created by 大灯泡 on 2018/1/10.
 */
abstract class BaseCompressTaskHelper<T> {
    protected String TAG = this.getClass().getSimpleName();
    protected OnCompressListener mOnCompressListener;
    protected T data;
    protected Context mContext;
    WeakHandler mWeakHandler;

    public BaseCompressTaskHelper(Context context, T options, OnCompressListener onCompressListener) {
        mContext = context;
        data = options;
        mOnCompressListener = onCompressListener;
        mWeakHandler = new WeakHandler(Looper.getMainLooper());
    }

    public abstract void start();


    void callSuccess(final List<CompressResult> imagePath) {

        if (mOnCompressListener == null) return;
        if (AppContext.isMainThread()) {
            mOnCompressListener.onSuccess(imagePath);
        } else {
            mWeakHandler.post(new Runnable() {
                @Override
                public void run() {
                    mOnCompressListener.onSuccess(imagePath);
                }
            });
        }
    }

    void callError(final String message) {
        if (mOnCompressListener == null) return;
        if (AppContext.isMainThread()) {
            mOnCompressListener.onError(message);
        } else {
            mWeakHandler.post(new Runnable() {
                @Override
                public void run() {
                    mOnCompressListener.onError(message);
                }
            });
        }
    }

    void callCompress(final int cur, final int target) {
        if (mOnCompressListener == null) return;
        if (AppContext.isMainThread()) {
            mOnCompressListener.onCompress(cur, target);
        } else {
            mWeakHandler.post(new Runnable() {
                @Override
                public void run() {
                    mOnCompressListener.onCompress(cur, target);
                }
            });
        }
    }

    void callRotated(final int picIndex, final int width, final int height) {
        if (mOnCompressListener == null) return;
        if (AppContext.isMainThread()) {
            mOnCompressListener.onRotate(picIndex, width, height);
        } else {
            mWeakHandler.post(new Runnable() {
                @Override
                public void run() {
                    mOnCompressListener.onRotate(picIndex, width, height);
                }
            });
        }
    }
}
