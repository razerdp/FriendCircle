package razerdp.friendcircle.ui.widget.popup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import cn.bmob.v3.exception.BmobException;
import razerdp.basepopup.BasePopupWindow;
import razerdp.friendcircle.R;
import razerdp.friendcircle.app.manager.LocalHostManager;
import razerdp.friendcircle.app.mvp.model.entity.UserInfo;
import razerdp.friendcircle.app.net.request.RegisterRequest;
import razerdp.github.com.baselibrary.utils.ui.UIHelper;
import razerdp.github.com.baseuilib.widget.common.LoadingView;
import razerdp.github.com.baselibrary.helper.AppSetting;
import razerdp.github.com.net.base.OnResponseListener;

/**
 * Created by 大灯泡 on 2016/12/14.
 * <p>
 * 注册
 */

public class RegisterPopup extends BasePopupWindow implements View.OnClickListener {

    private ViewHolder vh;

    public RegisterPopup(Activity context) {
        super(context);
        setBackPressEnable(false);
        vh = new ViewHolder(getPopupWindowView());
        setViewClickListener(this, vh.cancel, vh.ok);
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    protected Animator initShowAnimator() {
        AnimatorSet set;
        set = new AnimatorSet();
        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(mAnimaView, "translationY", 300, 0).setDuration(600);
        transAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mAnimaView, "alpha", 0.4f, 1).setDuration(250 * 3 / 2);
        set.playTogether(transAnimator, alphaAnimator);
        return set;
    }

    @Override
    protected Animator initExitAnimator() {
        AnimatorSet set;
        set = new AnimatorSet();
        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(mAnimaView, "translationY", 0, 250).setDuration(600);
        transAnimator.setInterpolator(new OvershootInterpolator(-6));
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mAnimaView, "alpha", 1f, 0).setDuration(800);
        set.playTogether(transAnimator, alphaAnimator);
        return set;
    }

    @Override
    public View getClickToDismissView() {
        return null;
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.popup_register);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                AppSetting.saveBooleanPreferenceByKey(AppSetting.CHECK_REGISTER, true);
                if (onRegisterSuccess != null) onRegisterSuccess.onSuccess(LocalHostManager.INSTANCE.getDeveloperHostUser());
                dismiss();
                break;
            case R.id.ok:
                String username = vh.ed_username.getText().toString().trim();
                String pass = vh.ed_pass.getText().toString().trim();
                String nick = vh.ed_nick.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    UIHelper.ToastMessage("用户名不能为空哦");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    UIHelper.ToastMessage("密码不能为空哦");
                    return;
                }
                if (TextUtils.isEmpty(nick)) {
                    UIHelper.ToastMessage("昵称不能为空哦");
                    return;
                }
                new RegisterRequest().setUsername(username).setPassword(pass).setNick(nick).setOnResponseListener(new OnResponseListener<UserInfo>() {
                    @Override
                    public void onStart(int requestType) {
                        AppSetting.saveBooleanPreferenceByKey(AppSetting.CHECK_REGISTER, true);
                        vh.rl_loading.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onSuccess(UserInfo response, int requestType) {
                        dismiss();
                        AppSetting.saveBooleanPreferenceByKey(AppSetting.CHECK_REGISTER, true);
                        LocalHostManager.INSTANCE.updateLocalHost(response);
                        vh.rl_loading.setVisibility(View.GONE);
                        if (onRegisterSuccess != null) onRegisterSuccess.onSuccess(response);
                    }

                    @Override
                    public void onError(BmobException e, int requestType) {
                        vh.rl_loading.setVisibility(View.GONE);
                        UIHelper.ToastMessage("注册失败....");
                    }
                }).execute();
                break;
        }
    }

    private onRegisterSuccess onRegisterSuccess;

    public RegisterPopup.onRegisterSuccess getOnRegisterSuccess() {
        return onRegisterSuccess;
    }

    public void setOnRegisterSuccess(RegisterPopup.onRegisterSuccess onRegisterSuccess) {
        this.onRegisterSuccess = onRegisterSuccess;
    }

    public interface onRegisterSuccess {
        void onSuccess(UserInfo userInfo);
    }


    static class ViewHolder {
        public View rootView;
        public LoadingView loadingView;
        public EditText ed_username;
        public EditText ed_pass;
        public EditText ed_nick;
        public Button cancel;
        public Button ok;
        public RelativeLayout rl_loading;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.ed_username = (EditText) rootView.findViewById(R.id.ed_username);
            this.ed_pass = (EditText) rootView.findViewById(R.id.ed_pass);
            this.ed_nick = (EditText) rootView.findViewById(R.id.ed_nick);
            this.cancel = (Button) rootView.findViewById(R.id.cancel);
            this.loadingView= (LoadingView) rootView.findViewById(R.id.view_loading);
            this.ok = (Button) rootView.findViewById(R.id.ok);
            this.rl_loading= (RelativeLayout) rootView.findViewById(R.id.rl_loading);
            loadingView.start();
        }

    }
}
