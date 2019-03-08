package razerdp.github.com.lib.network.okhttp;

import android.support.annotation.MainThread;

/**
 * Created by 大灯泡 on 2019/3/8.
 */
public interface DownloadListener<T> {
    void onStart();

    @MainThread
    void onProgress(int progress);

    void onFinish(T target);

    void onFailure(DownLoadErrorException e);

    void onCancel();

    abstract class SimpleDownloadListenerAdapter implements DownloadListener {
        @Override
        public void onStart() {

        }

        @Override
        public void onProgress(int progress) {

        }

        @Override
        public void onFailure(DownLoadErrorException e) {

        }

        @Override
        public void onCancel() {

        }
    }
}
