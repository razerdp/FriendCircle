package razerdp.github.com.ui.imageloader.glide;

import razerdp.github.com.lib.network.okhttp.DownLoadErrorException;
import razerdp.github.com.lib.network.okhttp.DownloadListener;

/**
 * Created by 大灯泡 on 2019/3/8.
 */
public abstract class GlideProgressLoaderListener implements DownloadListener<String> {
    @Override
    public void onStart() {

    }

    @Override
    public void onFinish(String target) {

    }

    @Override
    public void onFailure(DownLoadErrorException e) {

    }

    @Override
    public void onCancel() {

    }
}
