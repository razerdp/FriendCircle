package razerdp.github.com.lib.manager.compress;

/**
 * Created by 大灯泡 on 2018/1/10.
 */
interface BaseCompressListener {

    void onSuccess(Object imagePath);

    void onCompress(long current, long target);

    void onError(String tag);
}
