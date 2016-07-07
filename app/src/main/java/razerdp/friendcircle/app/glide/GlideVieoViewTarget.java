package razerdp.friendcircle.app.glide;

import android.media.MediaPlayer;
import android.os.Build;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.waynell.videolist.widget.TextureVideoView;

import java.io.File;

/**
 * Created by 大灯泡 on 2016/7/7.
 */
public class GlideVieoViewTarget extends ViewTarget<TextureVideoView, File> implements TextureVideoView.MediaPlayerCallback {


    private final TextureVideoView vieoView;

    public GlideVieoViewTarget(TextureVideoView view) {
        super(view);
        view.setMediaPlayerCallback(this);
        vieoView = view;
    }

    @Override
    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
    }

    @Override
    public void getSize(SizeReadyCallback cb) {
        cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // do nothing
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        // do nothing
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        // do nothing
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }
}
