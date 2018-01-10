package razerdp.github.com.lib.manager.compress;

/**
 * Created by 大灯泡 on 2018/1/8.
 */
public abstract class OnCompressListener implements BaseCompressListener {

    public abstract void onRotate(int width, int height);

    /**
     * @param imagePath
     * @hide
     * @deprecated
     */
    @Override
    public void onSuccess(Object imagePath) {
        if (String.class.isInstance(imagePath)) {
            onCompressSuccess((String) imagePath);
        } else {
            onError("压缩失败，结果并不是string");
        }
    }

    public abstract void onCompressSuccess(String targetImagePath);

    public static abstract class OnCompressListenerAdapter extends OnCompressListener {
        @Override
        public void onRotate(int width, int height) {

        }

        @Override
        public void onError(String tag) {

        }

        @Override
        public void onCompressSuccess(String targetImagePath) {

        }

        @Override
        public void onCompress(long current, long target) {

        }
    }
}
