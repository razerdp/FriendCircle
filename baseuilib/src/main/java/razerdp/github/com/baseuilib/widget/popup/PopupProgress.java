package razerdp.github.com.baseuilib.widget.popup;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.github.com.baseuilib.R;
import razerdp.github.com.baseuilib.widget.common.CircleProgressView;

/**
 * Created by 大灯泡 on 2017/3/24.
 * <p>
 * 进度条popup
 */

public class PopupProgress extends BasePopupWindow {

    private CircleProgressView progressView;
    private TextView progressTips;

    public PopupProgress(Activity context) {
        super(context);
        progressView = (CircleProgressView) findViewById(R.id.popup_progress);
        progressTips = (TextView) findViewById(R.id.progress_tips);
        progressView.setCurrentPresent(0);
        setBackPressEnable(false);
    }

    @Override
    protected Animation initShowAnimation() {
        return getDefaultAlphaAnimation();
    }

    @Override
    protected Animation initExitAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0F, 0.0F);
        alphaAnimation.setDuration(300L);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());
        return alphaAnimation;
    }

    @Override
    public View getClickToDismissView() {
        return null;
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.popup_progress);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_container);
    }

    public void setProgress(int progress) {
        if (progressView != null) {
            progressView.setCurrentPresent(progress);
        }
    }

    public void setProgressTips(String tips) {
        if (progressTips != null) {
            progressTips.setVisibility(TextUtils.isEmpty(tips) ? View.GONE : View.VISIBLE);
            progressTips.setText(tips);
        }
    }
}
