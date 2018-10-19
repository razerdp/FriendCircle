package razerdp.friendcircle.ui.widget.popup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.friendcircle.R;
import razerdp.github.com.lib.utils.ToolUtil;
import razerdp.github.com.ui.widget.commentwidget.ExTextView;

/**
 * Created by 大灯泡 on 2017/4/7.
 * <p>
 * 升级日志的弹窗
 */

public class UpdateInfoPopup extends BasePopupWindow {
    private TextView title;
    private ExTextView content;

    public UpdateInfoPopup(final Activity context) {
        super(context);
        title = (TextView) findViewById(R.id.update_title);
        content = (ExTextView) findViewById(R.id.update_content);
        setBackPressEnable(false);
        content.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ToolUtil.copyToClipboardAndToast(context, content.getText().toString().trim());
                return true;
            }
        });
        setBlurBackgroundEnable(true);
        findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected Animator onCreateShowAnimator() {
        AnimatorSet set;
        set = new AnimatorSet();
        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(getContentView(), View.TRANSLATION_Y, 300, 0).setDuration(600);
        transAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(getContentView(), View.ALPHA, 0.4f, 1).setDuration(250 * 3 / 2);
        set.playTogether(transAnimator, alphaAnimator);
        return set;
    }


    @Override
    protected Animator onCreateDismissAnimator() {
        AnimatorSet set;
        set = new AnimatorSet();
        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(getContentView(), View.TRANSLATION_Y, 0, 250).setDuration(600);
        transAnimator.setInterpolator(new OvershootInterpolator(-6));
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(getContentView(), View.ALPHA, 1f, 0).setDuration(800);
        set.playTogether(transAnimator, alphaAnimator);
        return set;
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_update);
    }


    public void setTitle(CharSequence titleMsg) {
        title.setText(titleMsg);
    }

    public void setContent(CharSequence contentMsg) {
        content.setText(contentMsg);
    }
}
