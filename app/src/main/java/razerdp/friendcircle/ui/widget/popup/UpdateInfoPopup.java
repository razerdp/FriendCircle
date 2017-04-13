package razerdp.friendcircle.ui.widget.popup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.friendcircle.R;
import razerdp.github.com.baselibrary.utils.ToolUtil;
import razerdp.github.com.baselibrary.utils.ui.UIHelper;

/**
 * Created by 大灯泡 on 2017/4/7.
 * <p>
 * 升级日志的弹窗
 */

public class UpdateInfoPopup extends BasePopupWindow {
    private TextView title;
    private TextView content;

    public UpdateInfoPopup(final Activity context) {
        super(context);
        title = (TextView) findViewById(R.id.update_title);
        content = (TextView) findViewById(R.id.update_content);
        setBackPressEnable(false);
        content.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ToolUtil.copyToClipboardAndToast(context, content.getText().toString().trim());
                return true;
            }
        });
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    protected Animator initShowAnimator() {
        AnimatorSet set;
        set = new AnimatorSet();
        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(mAnimaView, View.TRANSLATION_Y, 300, 0).setDuration(600);
        transAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mAnimaView, View.ALPHA, 0.4f, 1).setDuration(250 * 3 / 2);
        set.playTogether(transAnimator, alphaAnimator);
        return set;
    }

    @Override
    protected Animator initExitAnimator() {
        AnimatorSet set;
        set = new AnimatorSet();
        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(mAnimaView, View.TRANSLATION_Y, 0, 250).setDuration(600);
        transAnimator.setInterpolator(new OvershootInterpolator(-6));
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mAnimaView, View.ALPHA, 1f, 0).setDuration(800);
        set.playTogether(transAnimator, alphaAnimator);
        return set;
    }

    @Override
    public View getClickToDismissView() {
        return findViewById(R.id.tv_ok);
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.popup_update);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_content);
    }


    public void setTitle(CharSequence titleMsg) {
        title.setText(titleMsg);
    }

    public void setContent(CharSequence contentMsg) {
        content.setText(contentMsg);
    }
}
