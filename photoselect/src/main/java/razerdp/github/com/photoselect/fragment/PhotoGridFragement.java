package razerdp.github.com.photoselect.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.socks.library.KLog;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import razerdp.github.com.adapter.PhotoSelectAdapter;
import razerdp.github.com.baselibrary.base.BaseFragment;
import razerdp.github.com.baselibrary.helper.AppSetting;
import razerdp.github.com.baselibrary.manager.localphoto.LPException;
import razerdp.github.com.baselibrary.manager.localphoto.LocalPhotoManager;
import razerdp.github.com.baselibrary.utils.ui.UIHelper;
import razerdp.github.com.baselibrary.utils.ui.ViewUtil;
import razerdp.github.com.baseuilib.baseadapter.OnRecyclerViewItemClickListener;
import razerdp.github.com.baseuilib.baseadapter.itemdecoration.GridItemDecoration;
import razerdp.github.com.baseuilib.dialog.progress.ProgressDialogHelper;
import razerdp.github.com.baseuilib.widget.popup.PopupProgress;
import razerdp.github.com.model.PhotoBrowserInfo;
import razerdp.github.com.photoselect.PhotoMultiBrowserActivity;
import razerdp.github.com.photoselect.R;
import razerdp.github.com.router.RouterList;

/**
 * Created by 大灯泡 on 2017/3/29.
 * <p>
 * 相册缩略图浏览fragment
 */

public class PhotoGridFragement extends BaseFragment {
    private static final String TAG = "PhotoGridFragement";

    private ViewHolder vh;
    private PhotoSelectAdapter adapter;
    private String currentAlbumName;
    public static final int MAX_COUNT = 9;

    @Override
    public int getLayoutResId() {
        return R.layout.frag_photo_grid;
    }

    @Override
    protected void onInitData() {

    }

    private void scanImgSyncWithProgress() {
        AppSetting.saveBooleanPreferenceByKey(AppSetting.APP_HAS_SCAN_IMG, true);
        final PopupProgress popupProgress = new PopupProgress(getActivity());
        popupProgress.setProgressTips("正在扫描系统相册...");
        LocalPhotoManager.INSTANCE.scanImgAsync(new LocalPhotoManager.OnScanProgresslistener() {

            @Override
            public void onStart() {
                popupProgress.showPopupWindow();
                popupProgress.setProgress(0);
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

    @Override
    protected void onInitView(View rootView) {
        vh = new ViewHolder();
        //popup在activity没初始化完成前可能无法展示，因此需要延迟一点。。。
        getActivity().getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                scanImgSyncWithProgress();
            }
        }, 500);
    }

    private void initView() {
        vh.mPhotoEdit.setOnClickListener(onPhotoEditClickListener);
        vh.mPhotoPreview.setOnClickListener(onPhotoPreviewClickListener);
        vh.mFinish.setOnClickListener(onFinishClickListener);

        changeAlbum(LocalPhotoManager.INSTANCE.getAllPhotoTitle());
    }

    private void initSelectCountChangeListener() {
        adapter.setOnSelectCountChangeLisntenr(new PhotoSelectAdapter.OnSelectCountChangeLisntenr() {
            @Override
            public void onSelectCountChange(int count) {
                vh.setPhotoSlectCount(count);
            }
        });
    }

    public void changeAlbum(String albumName) {
        if (TextUtils.isEmpty(albumName)) return;
        if (TextUtils.equals(currentAlbumName, albumName)) return;
        currentAlbumName = albumName;
        if (adapter == null) {
            final int itemDecoration = UIHelper.dipToPx(2);
            adapter = new PhotoSelectAdapter(getActivity(), itemDecoration, LocalPhotoManager.INSTANCE.getLocalImages(albumName));
            initSelectCountChangeListener();
            GridLayoutManager manager=new GridLayoutManager(getActivity(), 4, LinearLayoutManager.VERTICAL, false);
            vh.mPhotoContent.setLayoutManager(manager);
            manager.setItemPrefetchEnabled(true);
            vh.mPhotoContent.addItemDecoration(new GridItemDecoration(itemDecoration));
            vh.mPhotoContent.setAdapter(adapter);
        } else {
            adapter.updateData(LocalPhotoManager.INSTANCE.getLocalImages(albumName));
            vh.setPhotoSlectCount(0);
        }
        adapter.setCurAlbumName(currentAlbumName);
        vh.mPhotoContent.getLayoutManager().scrollToPosition(adapter.getItemCount() - 1);
    }

    public void updateSelectList(List<LocalPhotoManager.ImageInfo> newDatas) {
        if (adapter != null) {
            adapter.updateSelections(newDatas);
            vh.setPhotoSlectCount(newDatas.size());
            adapter.notifyDataSetChanged();
        }
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
            PhotoBrowserInfo info = PhotoBrowserInfo.create(0, null, adapter.getSelectedRecordLists());
            ARouter.getInstance()
                   .build(RouterList.PhotoMultiBrowserActivity.path)
                   .withParcelable(RouterList.PhotoMultiBrowserActivity.key_browserinfo, info)
                   .withInt(RouterList.PhotoMultiBrowserActivity.key_maxSelectCount, MAX_COUNT)
                   .navigation((Activity) getContext(), RouterList.PhotoMultiBrowserActivity.requestCode);
        }
    };

    private View.OnClickListener onFinishClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UIHelper.ToastMessage("finish");
        }
    };

    class ViewHolder {
        RecyclerView mPhotoContent;
        TextView mPhotoEdit;
        TextView mPhotoPreview;
        TextView mSelectCount;
        TextView mFinish;

        ScaleAnimation scaleAnimation;

        public ViewHolder() {
            mPhotoContent = findView(R.id.photo_content);
            mPhotoEdit = findView(R.id.photo_edit);
            mPhotoPreview = findView(R.id.photo_preview);
            mSelectCount = findView(R.id.photo_select_count);
            mFinish = findView(R.id.photo_select_finish);
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
