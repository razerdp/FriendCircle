package razerdp.github.com.photoselect;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.socks.library.KLog;

import java.util.LinkedHashMap;
import java.util.List;

import razerdp.github.com.adapter.PhotoSelectAdapter;
import razerdp.github.com.baselibrary.helper.AppSetting;
import razerdp.github.com.baselibrary.manager.localphoto.LPException;
import razerdp.github.com.baselibrary.manager.localphoto.LocalPhotoManager;
import razerdp.github.com.baselibrary.utils.ui.SwitchActivityTransitionUtil;
import razerdp.github.com.baselibrary.utils.ui.UIHelper;
import razerdp.github.com.baselibrary.utils.ui.ViewUtil;
import razerdp.github.com.baseuilib.base.BaseTitleBarActivity;
import razerdp.github.com.baseuilib.baseadapter.itemdecoration.GridItemDecoration;
import razerdp.github.com.baseuilib.widget.common.TitleBar;
import razerdp.github.com.baseuilib.widget.popup.PopupProgress;

/**
 * Created by 大灯泡 on 2017/3/22.
 * <p>
 * 图片选择器
 */

public class PhotoSelectActivity extends BaseTitleBarActivity {
    private static final String TAG = "PhotoSelectActivity";

    private ViewHolder vh;

    private PhotoSelectAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoselect);
        init();
    }

    @Override
    public void onHandleIntent(Intent intent) {

    }

    private void init() {
        initTitle();
        initData();
    }

    private void initTitle() {
        setTitle(LocalPhotoManager.INSTANCE.getAllPhotoTitle());
        setTitleMode(TitleBar.MODE_BOTH);
        setTitleLeftText("返回");
        setTitleRightText("取消");
        setTitleRightIcon(0);
    }

    private void initData() {
        boolean hasScanImg = AppSetting.loadBooleanPreferenceByKey(AppSetting.APP_HAS_SCAN_IMG, false);
        if (!hasScanImg) {
            scanImgSyncWithProgress();
        } else {
            scanImgSync();
        }
    }

    private void scanImgSync() {
        LocalPhotoManager.INSTANCE.scanImgAsync(new LocalPhotoManager.OnScanListener() {

            @Override
            public void onStart() {
                KLog.i(TAG, "onStart");

            }

            @Override
            public void onFinish() {
                KLog.i(TAG, "onFinish");
                initView();
            }

            @Override
            public void onError(LPException e) {
                KLog.e(TAG, e);
                UIHelper.ToastMessage(e.getMessage());

            }
        });
    }

    private void scanImgSyncWithProgress() {
        AppSetting.saveBooleanPreferenceByKey(AppSetting.APP_HAS_SCAN_IMG, true);
        final PopupProgress popupProgress = new PopupProgress(this);
        popupProgress.setProgressTips("正在扫描系统相册...");
        //popup在activity没初始化完成前可能无法展示，因此需要延迟一点。。。
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                LocalPhotoManager.INSTANCE.scanImgAsync(new LocalPhotoManager.OnScanProgresslistener() {

                    @Override
                    public void onStart() {
                        popupProgress.showPopupWindow();
                        KLog.i(TAG, "onStart");
                    }

                    @Override
                    public void onProgress(int progress) {
                        popupProgress.setProgress(progress);
                    }

                    @Override
                    public void onFinish() {
                        KLog.i(TAG, "onFinish");
                        AppSetting.saveBooleanPreferenceByKey(AppSetting.APP_HAS_SCAN_IMG, true);
                        popupProgress.dismiss();
                        initView();
                    }

                    @Override
                    public void onError(LPException e) {
                        KLog.e(TAG, e);
                        UIHelper.ToastMessage(e.getMessage());
                        popupProgress.dismiss();
                    }
                });
            }
        }, 500);

    }

    private void initView() {
        vh = new ViewHolder(this);
        vh.mPhotoEdit.setOnClickListener(onPhotoEditClickListener);
        vh.mPhotoPreview.setOnClickListener(onPhotoPreviewClickListener);
        vh.mFinish.setOnClickListener(onFinishClickListener);

        LinkedHashMap<String, List<LocalPhotoManager.ImageInfo>> info = LocalPhotoManager.INSTANCE.getLocalImages();
        final int itemDecoration = UIHelper.dipToPx(2);
        adapter = new PhotoSelectAdapter(this, itemDecoration, info.get(LocalPhotoManager.INSTANCE.getAllPhotoTitle()));
        initSelectCountChangeListener();
        vh.mPhotoContent.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, true));
        vh.mPhotoContent.addItemDecoration(new GridItemDecoration(itemDecoration));
        vh.mPhotoContent.setAdapter(adapter);
    }

    private void initSelectCountChangeListener() {
        adapter.setOnSelectCountChangeLisntenr(new PhotoSelectAdapter.OnSelectCountChangeLisntenr() {
            @Override
            public void onSelectCountChange(int count) {
                vh.setPhotoSlectCount(count);
            }
        });
    }


    //=============================================================click event
    private View.OnClickListener onPhotoEditClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UIHelper.ToastMessage("编辑功能估计要有很长一段时间之后才能去完善这哦");
        }
    };

    private View.OnClickListener onPhotoPreviewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UIHelper.ToastMessage("预览");

        }
    };

    private View.OnClickListener onFinishClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UIHelper.ToastMessage("finish");
        }
    };


    @Override
    public void onTitleLeftClick() {

    }

    @Override
    public void onTitleRightClick() {
        finish();
    }


    @Override
    public void finish() {
        super.finish();
        SwitchActivityTransitionUtil.transitionVerticalOnFinish(this);
    }

    class ViewHolder {
        RecyclerView mPhotoContent;
        TextView mPhotoEdit;
        TextView mPhotoPreview;
        TextView mSelectCount;
        TextView mFinish;

        ScaleAnimation scaleAnimation;

        public ViewHolder(PhotoSelectActivity activity) {
            mPhotoContent = activity.findView(R.id.photo_content);
            mPhotoEdit = activity.findView(R.id.photo_edit);
            mPhotoPreview = activity.findView(R.id.photo_preview);
            mSelectCount = activity.findView(R.id.photo_select_count);
            mFinish = activity.findView(R.id.photo_select_finish);
            buildAnima();
            setPhotoSlectCount(0);
        }

        private void buildAnima() {
            if (scaleAnimation == null) {
                scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(600);
                scaleAnimation.setInterpolator(new BounceInterpolator());
            }
        }

        public void setPhotoSlectCount(int count) {
            if (count <= 0) {
                mPhotoEdit.setTextColor(UIHelper.getResourceColor(R.color.text_gray));
                mPhotoPreview.setTextColor(UIHelper.getResourceColor(R.color.text_gray));
                mFinish.setTextColor(UIHelper.getResourceColor(R.color.wechat_green_transparent));
                mSelectCount.clearAnimation();
                mSelectCount.setVisibility(View.GONE);

                ViewUtil.setViewsEnableAndClickable(false, false, mPhotoEdit, mPhotoPreview, mFinish);
            } else {
                //如果选择的照片大于一张，是不允许编辑的(iOS版微信的交互如此设计)
                mPhotoEdit.setTextColor(count == 1 ? UIHelper.getResourceColor(R.color.text_black) : UIHelper.getResourceColor(R.color.text_gray));
                mPhotoPreview.setTextColor(UIHelper.getResourceColor(R.color.text_black));
                mFinish.setTextColor(UIHelper.getResourceColor(R.color.wechat_green_bg));
                mSelectCount.setVisibility(View.VISIBLE);
                mSelectCount.clearAnimation();
                mSelectCount.setText(String.valueOf(count));
                mSelectCount.startAnimation(scaleAnimation);
                ViewUtil.setViewsEnableAndClickable(count == 1, count == 1, mPhotoEdit);
                ViewUtil.setViewsEnableAndClickable(true, true, mPhotoPreview, mFinish);
            }

        }
    }
}
