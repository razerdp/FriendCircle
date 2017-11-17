package razerdp.github.com.ui.widget.pullrecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import razerdp.github.com.baseuilib.R;


/**
 * Created by 大灯泡 on 2016/11/22.
 * <p>
 * 加载更多
 */

public class PullRefreshFooter extends FrameLayout {

    private RotateAnimation rotateAnimation;
    private ImageView loadingView;

    public PullRefreshFooter(Context context) {
        this(context, null);
    }

    public PullRefreshFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.view_ptr_footer, this);
        loadingView = (ImageView) findViewById(R.id.iv_loading);
        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(600);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        setVisibility(GONE);
    }


    public void onRefreshing() {
        setVisibility(VISIBLE);
        loadingView.startAnimation(rotateAnimation);
    }

    public void onFinish() {
        setVisibility(GONE);
        loadingView.clearAnimation();
    }


}
