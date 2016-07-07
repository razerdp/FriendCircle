package razerdp.friendcircle.app.glide;

import android.graphics.drawable.Drawable;

import com.waynell.videolist.widget.TextureVideoView;

/**
 * Created by 大灯泡 on 2016/6/24.
 */
public interface OnGlideProgressListener<V extends TextureVideoView> {

    void onStart(V view, String url);

    void onLoading(V view, String url, long bytesRead, long expectedLength);

    void onFailed(V view, String url, Drawable errorDrawable);

    void onLoadFinish(V view, String url);
}
