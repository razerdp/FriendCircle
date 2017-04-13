package razerdp.github.com.photoselect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;
import java.util.List;

import razerdp.github.com.adapter.PhotoBrowserAdapter;
import razerdp.github.com.baselibrary.base.BaseActivity;
import razerdp.github.com.baselibrary.manager.localphoto.LocalPhotoManager;
import razerdp.github.com.baselibrary.utils.ToolUtil;
import razerdp.github.com.baselibrary.utils.ui.AnimUtils;
import razerdp.github.com.baselibrary.utils.ui.UIHelper;
import razerdp.github.com.baselibrary.utils.ui.ViewUtil;
import razerdp.github.com.baseuilib.widget.common.HackyViewPager;
import razerdp.github.com.baseuilib.widget.imageview.CheckDrawable;
import razerdp.github.com.model.PhotoBrowserInfo;
import razerdp.github.com.router.RouterList;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by 大灯泡 on 2017/3/30.
 * <p>
 * 多功能图片浏览activity
 * <p>
 * target:以后实现编辑功能。。。。
 */

@Route(path = "/photo/browser")
public class PhotoMultiBrowserActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "PhotoMultiBrowserActivi";


    @Autowired(name = "maxSelectCount")
    public int maxSelectCount;

    @Autowired(name = "browserinfo")
    public PhotoBrowserInfo browserInfo;

    private PhotoBrowserAdapter adapter;
    //记录本地图片选择情况
    private List<LocalPhotoManager.ImageInfo> localSelectedPhotos;
    ViewHolder vh;

    @Override
    public void onHandleIntent(Intent intent) {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (browserInfo == null) {
            finish();
            return;
        }
        hideStatusBar();
        setContentView(R.layout.activity_photo_multi_browser);
        initView();
    }

    private void initView() {
        vh = new ViewHolder();
        localSelectedPhotos = new ArrayList<>();
        if (TextUtils.isEmpty(browserInfo.getCurrentAlbumName())) {
            adapter = new PhotoBrowserAdapter(this, browserInfo.getSelectedDatas());
        } else {
            adapter = new PhotoBrowserAdapter(this, LocalPhotoManager.INSTANCE.getLocalImages(browserInfo.getCurrentAlbumName()));
        }
        if (!ToolUtil.isListEmpty(browserInfo.getSelectedDatas())) {
            localSelectedPhotos.addAll(browserInfo.getSelectedDatas());
        }
        checkAndSetPhotoSelectCount();
        vh.selectedTouchDelegate.setOnClickListener(new InnerSelectPhotoListener());
        adapter.setOnViewTapListener(onViewTapListener);
        vh.viewPager.setAdapter(adapter);
        vh.viewPager.addOnPageChangeListener(onPageChangeListener);
        //如果初始值为0的话，不会触发pagechangelistener里面的pageselected，所以对于预览模式需要手动设定
        //详看源码(api:25.2.0)viewpager:line657
        //final boolean dispatchSelected = mCurItem != item;
        if (browserInfo.getCurPos()==0){
            vh.setSelected(checkIsSelect(0),false);
        }
        vh.viewPager.setCurrentItem(browserInfo.getCurPos());
        ViewUtil.setViewsClickListener(this, vh.back, vh.mFinish, vh.mPhotoEdit);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.back) {
            finishWithoutSave();
        } else if (i == R.id.photo_select_finish) {
            finish();
        } else if (i == R.id.photo_edit) {
            UIHelper.ToastMessage("编辑功能大概要好长好长的一段时间后才会研究的啦~");
        }

    }

    private boolean checkPhotoSelectCountValided(boolean toast) {
        boolean result = localSelectedPhotos.size() <= maxSelectCount;
        if (toast && !result) {
            UIHelper.ToastMessage("最多只能选" + maxSelectCount + "张照片哦");
        }
        return result;
    }

    private boolean checkAndSetPhotoSelectCount() {
        if (!checkPhotoSelectCountValided(true)) {
            return false;
        }
        vh.setPhotoSlectCount(localSelectedPhotos.size());
        return true;
    }

    private PhotoViewAttacher.OnViewTapListener onViewTapListener = new PhotoViewAttacher.OnViewTapListener() {
        @Override
        public void onViewTap(View view, float x, float y) {
            vh.toggleActionBarAnima();
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            boolean isSelected = checkIsSelect(position);
            vh.setSelected(isSelected, false);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private boolean checkIsSelect(int position) {
        if (position < 0 || position >= adapter.getCount()) return false;
        return checkIsSelect(adapter.getImageInfo(position));
    }

    private boolean checkIsSelect(LocalPhotoManager.ImageInfo imageInfo) {
        if (imageInfo == null) return false;
        if (ToolUtil.isListEmpty(localSelectedPhotos)) return false;
        for (LocalPhotoManager.ImageInfo localSelectedPhoto : localSelectedPhotos) {
            if (localSelectedPhoto.compareTo(imageInfo) == 0) {
                return true;
            }
        }
        return false;
    }

    public void finishWithoutSave() {
        super.finish();
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(RouterList.PhotoMultiBrowserActivity.key_result, (ArrayList<? extends Parcelable>) localSelectedPhotos);
        setResult(RESULT_OK, intent);
        super.finish();
    }


    private class InnerSelectPhotoListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            LocalPhotoManager.ImageInfo info = adapter.getImageInfo(vh.viewPager.getCurrentItem());
            boolean isMax = localSelectedPhotos.size() == maxSelectCount;
            boolean isSelected = checkIsSelect(info);
            if (isMax) {
                if (!isSelected) {
                    UIHelper.ToastMessage("最多只能选" + maxSelectCount + "张照片哦");
                    return;
                }
            }
            if (!isSelected) {
                addSelect(info);
            } else {
                removeSelect(info);
            }
        }


        private void addSelect(LocalPhotoManager.ImageInfo imageInfo) {
            if (checkPhotoSelectCountValided(true)) {
                localSelectedPhotos.add(imageInfo);
                vh.setSelected(true, true);
                checkAndSetPhotoSelectCount();
            }
        }

        private void removeSelect(LocalPhotoManager.ImageInfo imageInfo) {
            boolean hasRemoved = false;
            for (int i = 0; i < localSelectedPhotos.size(); i++) {
                LocalPhotoManager.ImageInfo info = localSelectedPhotos.get(i);
                if (info.compareTo(imageInfo) == 0) {
                    localSelectedPhotos.remove(i);
                    hasRemoved = true;
                    break;
                }
            }
            if (hasRemoved) {
                vh.setSelected(false, false);
                checkAndSetPhotoSelectCount();
            }
        }
    }

    class ViewHolder {
        RelativeLayout mTopBar;
        FrameLayout back;
        FrameLayout selectedTouchDelegate;
        ImageView selectedIcon;
        CheckDrawable checkDrawable;

        HackyViewPager viewPager;

        RelativeLayout mBottomBar;
        RecyclerView mPhotoContent;
        TextView mPhotoEdit;
        TextView mSelectCount;
        TextView mFinish;

        ScaleAnimation scaleAnimation;
        AnimationSet topBarHideAnimation;
        AnimationSet topBarShowAnimation;

        AnimationSet bottomBarHideAnimation;
        AnimationSet bottomBarShowAnimation;


        private boolean isHide;


        public ViewHolder() {
            mTopBar = findView(R.id.browser_top_bar);
            back = findView(R.id.back);
            selectedIcon = findView(R.id.select);
            selectedTouchDelegate=findView(R.id.select_touch_delegate);
            checkDrawable = new CheckDrawable(selectedIcon.getContext());
            selectedIcon.setImageDrawable(checkDrawable);

            viewPager = findView(R.id.photo_viewpager);

            mBottomBar = findView(R.id.browser_bottom_bar);
            mPhotoContent = findView(R.id.photo_content);
            mPhotoEdit = findView(R.id.photo_edit);
            mSelectCount = findView(R.id.photo_select_count);
            mFinish = findView(R.id.photo_select_finish);

            ViewUtil.expandViewTouchDelegate(mFinish,20,20,20,20);

            mTopBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //这个clicklistener仅仅用来拦截事件，不让其传到viewpager触发隐藏动画
                }
            });
            mBottomBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //这个clicklistener仅仅用来拦截事件，不让其传到viewpager触发隐藏动画
                }
            });
            buildAnima();
        }

        private void buildAnima() {
            if (scaleAnimation == null) {
                scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(600);
                scaleAnimation.setInterpolator(new BounceInterpolator());
            }
            if (topBarHideAnimation == null) {
                topBarHideAnimation = new AnimationSet(false);
                TranslateAnimation topBarTranslateAnima = AnimUtils.getPortraitTranslateAnimation(0, UIHelper.dipToPx(-70), 600);
                AlphaAnimation alphaAnimation = AnimUtils.getAlphaAnimation(1.0f, 0f, 600 * 3 / 2);
                topBarHideAnimation.addAnimation(topBarTranslateAnima);
                topBarHideAnimation.addAnimation(alphaAnimation);
                topBarHideAnimation.setFillAfter(true);
            }
            if (topBarShowAnimation == null) {
                topBarShowAnimation = new AnimationSet(false);
                TranslateAnimation topBarTranslateAnima = AnimUtils.getPortraitTranslateAnimation(UIHelper.dipToPx(-70), 0, 600);
                AlphaAnimation alphaAnimation = AnimUtils.getAlphaAnimation(0.0f, 1f, 600 * 3 / 2);
                topBarShowAnimation.addAnimation(topBarTranslateAnima);
                topBarShowAnimation.addAnimation(alphaAnimation);
                topBarShowAnimation.setFillAfter(true);
            }
            if (bottomBarHideAnimation == null) {
                bottomBarHideAnimation = new AnimationSet(false);
                TranslateAnimation topBarTranslateAnima = AnimUtils.getPortraitTranslateAnimation(0, UIHelper.dipToPx(50), 600);
                AlphaAnimation alphaAnimation = AnimUtils.getAlphaAnimation(1.0f, 0f, 600 * 3 / 2);
                bottomBarHideAnimation.addAnimation(topBarTranslateAnima);
                bottomBarHideAnimation.addAnimation(alphaAnimation);
                bottomBarHideAnimation.setFillAfter(true);
            }

            if (bottomBarShowAnimation == null) {
                bottomBarShowAnimation = new AnimationSet(false);
                TranslateAnimation topBarTranslateAnima = AnimUtils.getPortraitTranslateAnimation(UIHelper.dipToPx(50), 0, 600);
                AlphaAnimation alphaAnimation = AnimUtils.getAlphaAnimation(0f, 1f, 600 * 3 / 2);
                bottomBarShowAnimation.addAnimation(topBarTranslateAnima);
                bottomBarShowAnimation.addAnimation(alphaAnimation);
                bottomBarShowAnimation.setFillAfter(true);
            }
        }

        public void toggleActionBarAnima() {
            mTopBar.clearAnimation();
            mBottomBar.clearAnimation();
            if (isHide) {
                mTopBar.startAnimation(topBarShowAnimation);
                mBottomBar.startAnimation(bottomBarShowAnimation);
            } else {
                mTopBar.startAnimation(topBarHideAnimation);
                mBottomBar.startAnimation(bottomBarHideAnimation);
            }
            isHide = !isHide;
        }

        public void setPhotoSlectCount(int count) {
            if (count <= 0) {
                mFinish.setTextColor(UIHelper.getResourceColor(R.color.wechat_green_transparent));
                mSelectCount.clearAnimation();
                mSelectCount.setVisibility(View.GONE);
                ViewUtil.setViewsEnableAndClickable(false, false, mPhotoEdit, mFinish);
            } else {
                mFinish.setTextColor(UIHelper.getResourceColor(R.color.wechat_green_bg));
                mSelectCount.setVisibility(View.VISIBLE);
                mSelectCount.clearAnimation();
                mSelectCount.setText(String.valueOf(count));
                mSelectCount.startAnimation(scaleAnimation);
                ViewUtil.setViewsEnableAndClickable(count == 1, count == 1, mPhotoEdit);
                ViewUtil.setViewsEnableAndClickable(true, true, mFinish);
            }

        }

        public void setSelected(boolean isSelected, boolean needAnima) {
            checkDrawable.setSelected(isSelected, needAnima);
            selectedIcon.invalidateDrawable(checkDrawable);
        }
    }
}
