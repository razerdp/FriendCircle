package razerdp.friendcircle.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import java.util.ArrayList;
import razerdp.friendcircle.app.adapter.PhotoBoswerPagerAdapter;
import razerdp.friendcircle.widget.HackyViewPager;

/**
 * Created by 大灯泡 on 2016/4/12.
 * 相册展示的管理类
 */
public class PhotoPagerManager implements PhotoBoswerPagerAdapter.OnPhotoViewClickListener {

    private Context mContext;
    private PhotoBoswerPagerAdapter adapter;
    private HackyViewPager pager;

    private Rect finalBounds;
    private Point globalOffset;

    private View container;

    private PhotoPagerManager(Context context, HackyViewPager pager, View container) {
        if (container != null) {
            finalBounds = new Rect();
            globalOffset = new Point();
            this.mContext = context;
            this.container = container;
            this.pager = pager;
            adapter = new PhotoBoswerPagerAdapter(context);
            adapter.setOnPhotoViewClickListener(this);
        }
        else {
            throw new IllegalArgumentException("PhotoPagerManager >>> container不能为空哦");
        }
    }

    public static PhotoPagerManager create(Context context, HackyViewPager pager, View container) {
        return new PhotoPagerManager(context, pager, container);
    }

    public void showPhoto(
            @NonNull ArrayList<String> photoAddress, @NonNull ArrayList<Rect> originViewBounds, int curSelectedPos) {
        adapter.resetDatas(photoAddress, originViewBounds);
        pager.setAdapter(adapter);
        pager.setCurrentItem(curSelectedPos);
        pager.setLocked(photoAddress.size() == 1);
        container.getGlobalVisibleRect(finalBounds, globalOffset);
        showPhotoPager(originViewBounds, curSelectedPos);
    }

    private AnimatorSet curAnimator;

    private void showPhotoPager(@NonNull ArrayList<Rect> originViewBounds, int curSelectedPos) {
        Rect startBounds = originViewBounds.get(curSelectedPos);

        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float ratio = calculateRatio(startBounds, finalBounds);

        pager.setPivotX(0);
        pager.setPivotY(0);

        container.setVisibility(View.VISIBLE);
        container.setAlpha(1.0f);

        final AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(pager, View.X, startBounds.left, finalBounds.left))
           .with(ObjectAnimator.ofFloat(pager, View.Y, startBounds.top, finalBounds.top))
           .with(ObjectAnimator.ofFloat(pager, View.SCALE_X, ratio, 1f))
           .with(ObjectAnimator.ofFloat(pager, View.SCALE_Y, ratio, 1f));
        set.setDuration(300);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                curAnimator = set;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                curAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                curAnimator = null;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    @Override
    public void onPhotoViewClick(View view, Rect originBound, int curPos) {
        //如果展开动画没有展示完全就关闭，那么就停止展开动画进而执行退出动画
        if (curAnimator != null) {
            curAnimator.cancel();
        }

        container.getGlobalVisibleRect(finalBounds, globalOffset);

        originBound.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float ratio = calculateRatio(originBound, finalBounds);

        pager.setPivotX(0);
        pager.setPivotY(0);

        final AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(pager, View.X, originBound.left))
           .with(ObjectAnimator.ofFloat(pager, View.Y, originBound.top))
           .with(ObjectAnimator.ofFloat(pager, View.SCALE_X, 1f, ratio))
           .with(ObjectAnimator.ofFloat(pager, View.SCALE_Y, 1f, ratio))
           .with(ObjectAnimator.ofFloat(container, View.ALPHA, 1.0f, 0.0f));

        set.setDuration(300);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                curAnimator = set;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                curAnimator = null;
                container.clearAnimation();
                container.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                curAnimator = null;
                container.clearAnimation();
                container.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    private float calculateRatio(Rect startBounds, Rect finalBounds) {
        float ratio;
        if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            ratio = (float) startBounds.height() / finalBounds.height();
            float startWidth = ratio * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        }
        else {
            // Extend start bounds vertically
            ratio = (float) startBounds.width() / finalBounds.width();
            float startHeight = ratio * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }
        return ratio;
    }

    public void destroy() {
        adapter.destroy();
        mContext = null;
        adapter = null;
        pager = null;
        finalBounds = null;
        globalOffset = null;
        container = null;
    }
}
