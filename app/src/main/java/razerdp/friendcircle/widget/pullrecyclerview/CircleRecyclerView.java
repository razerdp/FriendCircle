package razerdp.friendcircle.widget.pullrecyclerview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.IntDef;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.logging.Logger;

import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollStateListener;
import me.everything.android.ui.overscroll.IOverScrollUpdateListener;
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.RecyclerViewOverScrollDecorAdapter;
import razerdp.friendcircle.R;
import razerdp.friendcircle.utils.UIHelper;
import razerdp.friendcircle.widget.pullrecyclerview.interfaces.OnRefreshListener2;

import static android.R.attr.duration;
import static me.everything.android.ui.overscroll.IOverScrollState.STATE_BOUNCE_BACK;
import static me.everything.android.ui.overscroll.IOverScrollState.STATE_DRAG_END_SIDE;
import static me.everything.android.ui.overscroll.IOverScrollState.STATE_DRAG_START_SIDE;
import static me.everything.android.ui.overscroll.IOverScrollState.STATE_IDLE;
import static razerdp.friendcircle.widget.pullrecyclerview.CircleRecyclerView.Status.DEFAULT;
import static razerdp.friendcircle.widget.pullrecyclerview.CircleRecyclerView.Status.REFRESHING;


/**
 * Created by 大灯泡 on 2016/10/29.
 * <p>
 * 专为朋友圈项目定制的下拉recyclerview
 * 原因在于：
 * 1 - 目前而言，git上大多数的rv都是用的swiperefreshlayout
 * 2 - 大多数rv都不支持下拉后收回下拉头部的
 * 3 - 因为大多数的rv都支持太多功能了，显得有点重，并且有些想要的回调我们都没法拿到
 * <p>
 * 综上所述，干脆自己弄一个算了。。。。
 * <p>
 * 目标：
 * 【基本要求】因为相当于定制，只为本项目服务，因此不考虑通用性，更多考虑扩展性
 * <p>
 * <p>
 * 1 - 下拉和上拉支持回调
 * 2 - 跟iOS朋友圈下拉头部一样，支持头部在刷新时的回弹
 * 3 - 滑动到底部自动加载更多
 * 4 - addHeaderView。
 * <p>
 * 使用的库：overscroll-decor(https://github.com/EverythingMe/overscroll-decor)
 */

public class CircleRecyclerView extends FrameLayout {
    public static boolean DEBUG = true;
    private static final String TAG = "CircleRecyclerView";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({DEFAULT, REFRESHING})
    @interface Status {
        int DEFAULT = 0;
        int REFRESHING = 1;
    }

    @Status
    private int currentStatus;

    //observer
    private InnerRefreshIconObserver iconObserver;

    //callback
    private OnRefreshListener2 onRefreshListener;

    private RecyclerView recyclerView;
    private ImageView refreshIcon;

    private int refreshPosition;


    public CircleRecyclerView(Context context) {
        this(context, null);
    }

    public CircleRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        GradientDrawable background = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xff323232, 0xff323232, 0xffffffff, 0xffffffff});
//        setBackgroundColor(0xff323232);
        setBackground(background);

        if (recyclerView == null) {
            recyclerView = new RecyclerView(context);
            recyclerView.setBackgroundColor(Color.WHITE);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        }

        if (refreshIcon == null) {
            refreshIcon = new ImageView(context);
            refreshIcon.setBackgroundColor(Color.TRANSPARENT);
            refreshIcon.setImageResource(R.drawable.rotate_icon);
        }
        FrameLayout.LayoutParams iconParam = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iconParam.leftMargin = UIHelper.dipToPx(12);

        addView(recyclerView, RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
        addView(refreshIcon, iconParam);

        refreshPosition = UIHelper.dipToPx(90);

        iconObserver = new InnerRefreshIconObserver(refreshIcon, refreshPosition);

    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 2) {
            throw new IllegalStateException("咳咳，不能超过两个view哦");
        }
        super.onFinishInflate();
    }

    private void setCurrentStatus(@Status int status) {
        this.currentStatus = status;
    }

    public void compelete() {
        Log.i(TAG, "status switch to refresh");
        setCurrentStatus(DEFAULT);
        if (iconObserver != null) {
            iconObserver.catchResetEvent();
        }
    }

    //------------------------------------------get/set-----------------------------------------------

    public OnRefreshListener2 getOnRefreshListener() {
        return onRefreshListener;
    }

    public void setOnRefreshListener(OnRefreshListener2 onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
        initOverScroll();
    }

    private void initOverScroll() {
        IOverScrollDecor decor = new VerticalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(recyclerView), 2f, 1f, 1f);
        decor.setOverScrollStateListener(new IOverScrollStateListener() {
            @Override
            public void onOverScrollStateChange(IOverScrollDecor decor, int oldState, int newState) {
                switch (newState) {
                    case STATE_IDLE:
                        // No over-scroll is in effect.
                        break;
                    case STATE_DRAG_START_SIDE:
                        // Dragging started at the left-end.
                        break;
                    case STATE_DRAG_END_SIDE:
                        // Dragging started at the right-end.
                        break;
                    case STATE_BOUNCE_BACK:
                        if (oldState == STATE_DRAG_START_SIDE) {
                            // Dragging stopped -- view is starting to bounce back from the *left-end* onto natural position.
                        } else { // i.e. (oldState == STATE_DRAG_END_SIDE)
                            // View is starting to bounce back from the *right-end*.
                        }
                        break;
                }
            }
        });

        decor.setOverScrollUpdateListener(new IOverScrollUpdateListener() {
            @Override
            public void onOverScrollUpdate(IOverScrollDecor decor, int state, float offset) {
                if (offset > 0) {
                    if (currentStatus == REFRESHING) return;
                    iconObserver.catchPullDownEvent(offset);
                    if (offset >= refreshPosition && state == STATE_BOUNCE_BACK) {
                        if (currentStatus != REFRESHING) {
                            setCurrentStatus(REFRESHING);
                            if (onRefreshListener != null) {
                                Log.i(TAG, "status switch to refresh");
                                onRefreshListener.onRefresh();
                            }
                            iconObserver.catchRefreshEvent();
                        }
                    }
                } else if (offset < 0) {
                    iconObserver.catchPullUpEvent(offset);
                    // 'view' is currently being over-scrolled from the bottom.
                } else {
                    // No over-scroll is in-effect.
                    // This is synonymous with having (state == STATE_IDLE).
                }
            }
        });
    }


    /**
     * 刷新Icon的动作
     */

    private static class InnerRefreshIconObserver {
        private ImageView refreshIcon;
        private final int refreshPosition;
        private float lastOffset = 0.0f;
        private RotateAnimation rotateAnimation;
        private ValueAnimator mValueAnimator;

        public InnerRefreshIconObserver(ImageView refreshIcon, int refreshPosition) {
            this.refreshIcon = refreshIcon;
            this.refreshPosition = refreshPosition;

            rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                                                  0.5f);
            rotateAnimation.setDuration(600);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatCount(Animation.INFINITE);

        }

        public void catchPullDownEvent(float offset) {
            if (checkHacIcon()) {
                refreshIcon.setRotation(offset * 2);
                if (offset >= refreshPosition) {
                    offset = refreshPosition;
                }
                int resultOffset = (int) (offset - lastOffset);
                refreshIcon.offsetTopAndBottom(resultOffset);
                Log.d(TAG, "down  >>  " + offset + "  resultOffset   >>>   " + resultOffset);
                adjustRefreshIconPosition();
                lastOffset = offset;
            }

        }

        public void catchPullUpEvent(float offset) {
            if (checkHacIcon()) {
                refreshIcon.setRotation(offset * 2);
                int resultOffset = (int) (offset - lastOffset);
                refreshIcon.offsetTopAndBottom(resultOffset);
                Log.d(TAG, "up  >>>  " + offset + "  resultOffset   >>>   " + resultOffset);
                adjustRefreshIconPosition();
                lastOffset = offset;
            }

        }

        /**
         * 调整icon的位置界限
         */
        private void adjustRefreshIconPosition() {
            if (refreshIcon.getTop() < 0) {
                refreshIcon.offsetTopAndBottom(Math.abs(refreshIcon.getTop()));
            } else if (refreshIcon.getTop() > refreshPosition) {
                refreshIcon.offsetTopAndBottom(-(refreshIcon.getTop() - refreshPosition));
            }
        }

        public void catchRefreshEvent() {
            if (checkHacIcon()) {
                refreshIcon.clearAnimation();
                refreshIcon.startAnimation(rotateAnimation);
            }

        }

        public void catchResetEvent() {
            refreshIcon.clearAnimation();
            if (mValueAnimator == null) {
                mValueAnimator = ValueAnimator.ofFloat(refreshPosition, 0);
                mValueAnimator.setInterpolator(new DecelerateInterpolator());
                mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float result = (float) animation.getAnimatedValue();
                        catchPullUpEvent(result);
                    }
                });
                mValueAnimator.setDuration(300);
            }
            mValueAnimator.start();
        }

        private boolean checkHacIcon() {
            return refreshIcon != null;
        }
    }
}
