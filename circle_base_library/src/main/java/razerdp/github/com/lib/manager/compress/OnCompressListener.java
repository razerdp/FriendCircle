package razerdp.github.com.lib.manager.compress;

/**
 * Created by 大灯泡 on 2018/1/8.
 */
public interface OnCompressListener {
    void onRotate(int width, int height);

    void onError(String tag);

    void onCompress(long current, long max);


    public abstract class OnCompressListenerAdapter implements OnCompressListener{
        @Override
        public void onRotate(int width, int height) {

        }
    }
}
