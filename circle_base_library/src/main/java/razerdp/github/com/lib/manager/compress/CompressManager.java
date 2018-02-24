package razerdp.github.com.lib.manager.compress;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import com.socks.library.KLog;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import razerdp.github.com.lib.utils.ToolUtil;

/**
 * Created by 大灯泡 on 2018/1/8.
 */
public class CompressManager {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SIZE, SCALE, BOTH})
    public @interface CompressType {

    }

    public static final int SIZE = 0x10;//质量压缩
    public static final int SCALE = 0x11;//分辨率压缩
    public static final int BOTH = 0x12;//上面两个。。。


    @Retention(RetentionPolicy.SOURCE)
    @StringDef({JPG, PNG})
    public @interface ImageType {
    }

    public static final String JPG = ".jpg";
    public static final String PNG = ".png";


    WeakReference<Context> mWeakReference;

    private List<CompressOption> mOptions;

    private CompressManager(Context context) {
        mWeakReference = new WeakReference<Context>(context);
        mOptions = new ArrayList<>();
    }

    public static CompressManager create(Context context) {
        return new CompressManager(context);
    }


    Context getContext() {
        return mWeakReference == null ? null : mWeakReference.get();
    }

    public CompressOption addTask() {
        return addTaskInternal(null);
    }


    CompressOption addTaskInternal(CompressOption compressOption) {
        CompressOption option = new CompressOption(this);
        if (compressOption != null) {
            mOptions.add(compressOption);
        } else {
            mOptions.add(option);
        }
        return option;
    }

    public void start() {
        start(null);
    }

    public void start(OnCompressListener listener) {
        BaseCompressTaskHelper helper;
        if (getContext() == null) {
            KLog.e("context为空");
            return;
        }
        if (ToolUtil.isListEmpty(mOptions)) {
            KLog.e("配置为空");
            return;
        }
        new CompressTaskQueue(getContext(), mOptions, listener).start();
    }
}
