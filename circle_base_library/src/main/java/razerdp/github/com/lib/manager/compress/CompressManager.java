package razerdp.github.com.lib.manager.compress;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Created by 大灯泡 on 2018/1/8.
 */
public class CompressManager {
    WeakReference<Context> mWeakReference;

    private CompressManager(Context context) {
        mWeakReference = new WeakReference<Context>(context);
    }


    Context getContext() {
        return mWeakReference == null ? null : mWeakReference.get();
    }
}
