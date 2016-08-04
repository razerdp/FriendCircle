package razerdp.friendcircle.app.glide;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.waynell.videolist.widget.TextureVideoView;

import java.io.File;

/**
 * Created by 大灯泡 on 2016/7/7.
 */
public class GlideVideoProgressTarget<V extends TextureVideoView> extends ProgressTarget<String, File> {
    private OnGlideProgressListener<V> listener;
    private V view;

    public GlideVideoProgressTarget(Target<File> target, V view) {
        super(target);
        this.view = view;
    }

    public GlideVideoProgressTarget(Target<File> target, V view, OnGlideProgressListener<V> listener) {
        super(target);
        this.view = view;
        this.listener = listener;
    }

    @Override
    protected void onConnecting() {
        if (listener != null) {
            listener.onStart(view, getModel());
        }

    }

    @Override
    protected void onDownloading(long bytesRead, long expectedLength) {
        if (listener != null) {
            listener.onLoading(view, getModel(), bytesRead, expectedLength);
        }
    }

    @Override
    protected void onDownloaded() {

    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
        super.onLoadFailed(e, errorDrawable);
        if (listener != null) {
            listener.onFailed(view, getModel(), errorDrawable);
        }
    }

    @Override
    protected void onDelivered() {

    }

    @Override
    protected void onDelivered(File resource) {
        super.onDelivered(resource);
        if (listener != null && resource != null) {
            listener.onLoadFinish(view, resource.getAbsolutePath());
        }
    }
}
