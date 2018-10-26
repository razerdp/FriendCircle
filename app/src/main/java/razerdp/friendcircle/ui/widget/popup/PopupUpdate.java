package razerdp.friendcircle.ui.widget.popup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.socks.library.KLog;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import razerdp.basepopup.BasePopupWindow;
import razerdp.friendcircle.R;
import razerdp.friendcircle.app.mvp.model.UpdateInfo;
import razerdp.github.com.lib.helper.AppFileHelper;
import razerdp.github.com.lib.helper.PermissionHelper;
import razerdp.github.com.lib.interfaces.IPermission;
import razerdp.github.com.lib.interfaces.OnPermissionGrantListener;
import razerdp.github.com.ui.util.UIHelper;
import razerdp.github.com.ui.widget.popup.PopupProgress;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by 大灯泡 on 2017/7/19.
 */

public class PopupUpdate extends BasePopupWindow {

    private static final String MIME_TYPE_APK = "application/vnd.android.package-archive";

    private TextView title;
    private TextView content;
    private Button update;

    private UpdateInfo mUpdateInfo;

    public PopupUpdate(final Activity context) {
        super(context);
        setBlurBackgroundEnable(true);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        update = (Button) findViewById(R.id.update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionAndUpdate(mUpdateInfo);
            }
        });

        findViewById(R.id.update_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUpdateInfo != null) {
                    BmobFile file = mUpdateInfo.getFile();
                    if (file != null) {
                        Uri webPage = Uri.parse(file.getFileUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
                        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                            getContext().startActivity(intent);
                        }
                    }
                }
            }
        });
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void download() {
        BmobFile file = mUpdateInfo.getFile();
        if (file != null) {
            final PopupProgress popupProgress = new PopupProgress(getContext());
            popupProgress.setProgressTips("正在更新");
            popupProgress.getProgressView().setCircleColor(UIHelper.getColor(R.color.green));
            popupProgress.getProgressView().setStrokeColor(UIHelper.getColor(R.color.green));
            file.download(new File(AppFileHelper.getDownloadPath(file.getUrl())), new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        install(s);
                    }
                }

                @Override
                public void onProgress(Integer integer, long l) {
                    if (!popupProgress.isShowing()) {
                        popupProgress.showPopupWindow();
                    }
                    popupProgress.setProgress(integer);
                }
            });
        }
    }

    private void install(String file) {
        KLog.i("文件 = " + file);
        Intent intent = new Intent();
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri fileUri = FileProvider.getUriForFile(getContext(), "github.razerdp.friendcircle", new File(file));
            intent.setDataAndType(fileUri, MIME_TYPE_APK);
        } else {
            intent.setDataAndType(Uri.parse(file), MIME_TYPE_APK);
        }
        getContext().startActivity(intent);
    }

    private void requestPermissionAndUpdate(UpdateInfo updateInfo) {
        if (getContext() instanceof IPermission) {
            ((IPermission) getContext()).getPermissionHelper().requestPermission(new OnPermissionGrantListener.OnPermissionGrantListenerAdapter() {
                @Override
                public void onPermissionGranted(PermissionHelper.Permission... grantedPermissions) {
                    if (mUpdateInfo != null) {
                        AppFileHelper.initStroagePath((Activity) getContext());
                        download();
                        dismissWithOutAnimate();
                    }
                }
            }, PermissionHelper.Permission.WRITE_EXTERNAL_STORAGE, PermissionHelper.Permission.READ_EXTERNAL_STORAGE);
        }
    }


    public void showPopupWindow(UpdateInfo updateInfo) {
        if (updateInfo == null) return;
        this.mUpdateInfo = updateInfo;
        bindData();
        super.showPopupWindow();
    }

    private void bindData() {
        title.setText(String.format("%s(%s)", "更新版本", "v" + mUpdateInfo.getVersion()));
        content.setText(mUpdateInfo.getDesc());
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
        return createPopupById(R.layout.popup_update_app);
    }
}
