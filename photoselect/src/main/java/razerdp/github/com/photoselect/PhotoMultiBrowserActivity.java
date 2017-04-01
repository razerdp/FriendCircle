package razerdp.github.com.photoselect;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import razerdp.github.com.adapter.PhotoBrowserAdapter;
import razerdp.github.com.baselibrary.base.BaseActivity;
import razerdp.github.com.baselibrary.manager.localphoto.LocalPhotoManager;
import razerdp.github.com.baselibrary.utils.ui.UIHelper;
import razerdp.github.com.baselibrary.utils.ui.ViewUtil;
import razerdp.github.com.baseuilib.widget.common.HackyViewPager;
import razerdp.github.com.baseuilib.widget.imageview.CheckDrawable;
import razerdp.github.com.model.PhotoBrowserInfo;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by 大灯泡 on 2017/3/30.
 * <p>
 * 多功能图片浏览activity
 * <p>
 * target:以后实现编辑功能。。。。
 */

@Route(path = "/photo/browser")
public class PhotoMultiBrowserActivity extends BaseActivity {
    private static final String TAG = "PhotoMultiBrowserActivi";

    @Autowired(name = "browserinfo")
    private PhotoBrowserInfo browserInfo;
    private PhotoBrowserAdapter adapter;


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
        final ViewHolder vh = new ViewHolder();
        if (TextUtils.isEmpty(browserInfo.getCurrentAlbumName())) {
            adapter = new PhotoBrowserAdapter(this, browserInfo.getSelectedDatas());
        } else {
            adapter = new PhotoBrowserAdapter(this, LocalPhotoManager.INSTANCE.getLocalImages(browserInfo.getCurrentAlbumName()));
        }
        adapter.setOnViewTapListener(onViewTapListener);
        vh.viewPager.setAdapter(adapter);
        vh.viewPager.setCurrentItem(browserInfo.getCurPos());
    }


    private PhotoViewAttacher.OnViewTapListener onViewTapListener = new PhotoViewAttacher.OnViewTapListener() {
        @Override
        public void onViewTap(View view, float x, float y) {
            // TODO: 2017/4/1 top bar和bottom bar隐藏

        }
    };


    class ViewHolder {
        RelativeLayout mTopBar;
        ImageView back;
        ImageView selectedIcon;
        CheckDrawable checkDrawable;

        HackyViewPager viewPager;

        RelativeLayout mBottomBar;
        RecyclerView mPhotoContent;
        TextView mPhotoEdit;
        TextView mSelectCount;
        TextView mFinish;

        ScaleAnimation scaleAnimation;

        public ViewHolder() {
            mTopBar = findView(R.id.browser_top_bar);
            back = findView(R.id.back);
            selectedIcon = findView(R.id.select);
            checkDrawable = new CheckDrawable(selectedIcon.getContext());
            selectedIcon.setImageDrawable(checkDrawable);

            viewPager = findView(R.id.photo_viewpager);

            mBottomBar = findView(R.id.browser_bottom_bar);
            mPhotoContent = findView(R.id.photo_content);
            mPhotoEdit = findView(R.id.photo_edit);
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
                mFinish.setTextColor(UIHelper.getResourceColor(R.color.wechat_green_transparent));
                mSelectCount.clearAnimation();
                mSelectCount.setVisibility(View.GONE);
                ViewUtil.setViewsEnableAndClickable(false, false, mPhotoEdit, mFinish);
            } else {
                //如果选择的照片大于一张，是不允许编辑的(iOS版微信的交互如此设计)
                mFinish.setTextColor(UIHelper.getResourceColor(R.color.wechat_green_bg));
                mSelectCount.setVisibility(View.VISIBLE);
                mSelectCount.clearAnimation();
                mSelectCount.setText(String.valueOf(count));
                mSelectCount.startAnimation(scaleAnimation);
                ViewUtil.setViewsEnableAndClickable(count == 1, count == 1, mPhotoEdit);
                ViewUtil.setViewsEnableAndClickable(true, true, mFinish);
            }

        }

        public void setSelected(boolean isSelected) {
            checkDrawable.setSelected(isSelected, isSelected);
            selectedIcon.invalidateDrawable(checkDrawable);
        }
    }
}
