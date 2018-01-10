package razerdp.github.com.lib.manager.compress;

import java.util.List;

/**
 * Created by 大灯泡 on 2018/1/8.
 */
public abstract class OnMultiCompressListener implements BaseCompressListener {
    /**
     * @param imagePath
     * @hide
     * @deprecated
     */
    @Override
    public void onSuccess(Object imagePath) {
        if (List.class.isInstance(imagePath)) {
            onCompressSuccess((List<String>) imagePath);
        } else {
            onError("压缩失败，结果并不是数组集");
        }
    }

    public abstract void onCompressSuccess(List<String> targetImagePath);

    public static abstract class OnMultiCompressListenerAdapter extends OnMultiCompressListener {

        @Override
        public void onCompress(long current, long target) {

        }

        @Override
        public void onError(String tag) {

        }

        @Override
        public void onCompressSuccess(List<String> targetImagePath) {

        }
    }
}
