package razerdp.github.com.photoselect;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import razerdp.github.com.baselibrary.base.BaseActivity;
import razerdp.github.com.baselibrary.utils.ui.UIHelper;
import razerdp.github.com.baselibrary.utils.ui.ViewUtil;
import razerdp.github.com.baseuilib.widget.common.HackyViewPager;
import razerdp.github.com.baseuilib.widget.imageview.CheckDrawable;

/**
 * Created by 大灯泡 on 2017/3/30.
 * <p>
 * 多功能图片浏览activity
 * <p>
 * target:以后实现编辑功能。。。。
 */

public class PhotoMultiBrowserActivity extends BaseActivity {
    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_photo_multi_browser);
        final ViewHolder vh = new ViewHolder();
        final boolean[] selected = {false};
        vh.selectedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected[0] = !selected[0];
                vh.setSelected(selected[0]);
            }
        });
    }


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
