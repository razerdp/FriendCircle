package razerdp.friendcircle.app.glide;

import com.waynell.videolist.widget.TextureVideoView;

/**
 * Created by 大灯泡 on 2016/7/7.
 */
public class GlidePorgressUtil {

    public static <V extends TextureVideoView> GlideVideoProgressTarget<V> createProgressTarget(V videoView, OnGlideProgressListener<V> l) {
        GlideVideoProgressTarget target = new GlideVideoProgressTarget<>(new GlideVieoViewTarget(
                videoView), videoView, l);
        return target;
    }
}
