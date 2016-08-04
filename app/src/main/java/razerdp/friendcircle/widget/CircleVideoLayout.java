package razerdp.friendcircle.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.waynell.videolist.widget.TextureVideoView;

import razerdp.friendcircle.R;
import razerdp.friendcircle.app.glide.GlidePorgressUtil;
import razerdp.friendcircle.app.glide.GlideVideoProgressTarget;
import razerdp.friendcircle.app.glide.OnGlideProgressListener;
import razerdp.friendcircle.widget.imageview.ForceClickImageView;

/**
 * Created by 大灯泡 on 2016/7/7.
 * <p/>
 * 小视频控件封装
 */
public class CircleVideoLayout extends RelativeLayout implements ViewPropertyAnimatorListener {
    private static final String TAG = "CircleVideoLayout";

    private String localVideoUrl;

    private CircleProgressView progressView;
    private TextureVideoView videoView;
    private ForceClickImageView cover;

    private static final int STATE_IDLE = 0x10;
    private static final int STATE_FINISH = 0x11;
    private static final int STATE_PLAYING = 0x12;

    private int videoState = STATE_IDLE;


    public CircleVideoLayout(Context context) {
        this(context, null);
    }

    public CircleVideoLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleVideoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.widget_video_layout, this);
        progressView = (CircleProgressView) findViewById(R.id.circle_progress);
        cover = (ForceClickImageView) findViewById(R.id.video_cover);
        videoView = (TextureVideoView) findViewById(R.id.video_view);

    }


    public void loadVideo(final String coverUrl, final String videoUrl) {
        if (TextUtils.isEmpty(videoUrl) || videoState != STATE_IDLE) return;
        if (!TextUtils.isEmpty(coverUrl)) {
            Glide.with(getContext())
                 .load(coverUrl)
                 .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                 .into(cover);
        }

        GlideVideoProgressTarget progressTarget = GlidePorgressUtil.createProgressTarget(
                videoView,
                new OnGlideProgressListener<TextureVideoView>() {
                    @Override
                    public void onStart(TextureVideoView view, String url) {
                        progressView.setStart();
                    }

                    @Override
                    public void onLoading(TextureVideoView view, String url, long bytesRead, long expectedLength) {
                        progressView.setCurrentPresent((int) (100.0f * bytesRead / expectedLength));
                    }

                    @Override
                    public void onFailed(TextureVideoView view, String url, Drawable errorDrawable) {
                        progressView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadVideo(coverUrl, videoUrl);

                            }
                        });
                    }

                    @Override
                    public void onLoadFinish(TextureVideoView view, String url) {
                        progressView.setFinish(true);
                        localVideoUrl = url;
                        videoState = STATE_FINISH;
                    }
                });
        progressTarget.setModel(videoUrl);
        Glide.with(getContext())
             .load(new GlideUrl(videoUrl))
             .into(progressTarget);

    }


    public void playVideo() {
        videoState = STATE_PLAYING;
        if (TextUtils.isEmpty(localVideoUrl)) {
            videoState = STATE_IDLE;
            return;
        }
        videoView.setAlpha(1.0f);
        ViewCompat.animate(cover)
                  .cancel();
        ViewCompat.animate(cover)
                  .setListener(this)
                  .alpha(0f);

        videoView.setVideoPath(localVideoUrl);
        videoView.start();

    }

    public void stopVideo() {
        videoState = STATE_IDLE;
        videoView.stop();

        videoView.setAlpha(0f);
        ViewCompat.animate(cover)
                  .cancel();
        ViewCompat.animate(cover)
                  .setListener(this)
                  .alpha(1f);
    }

    @Override
    public void onAnimationStart(View view) {

    }

    @Override
    public void onAnimationEnd(View view) {
        switch (videoState) {
            case STATE_IDLE:
                cover.setVisibility(VISIBLE);
                break;
            case STATE_FINISH:
                cover.setVisibility(VISIBLE);
                break;
            case STATE_PLAYING:
                cover.setVisibility(GONE);
                break;
            default:
                break;
        }

    }

    @Override
    public void onAnimationCancel(View view) {

    }
}
