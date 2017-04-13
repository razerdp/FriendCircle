package razerdp.github.com.baseuilib.widget.pullrecyclerview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.socks.library.KLog;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollStateListener;
import me.everything.android.ui.overscroll.IOverScrollUpdateListener;
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.RecyclerViewOverScrollDecorAdapter;
import razerdp.github.com.baselibrary.utils.ToolUtil;
import razerdp.github.com.baselibrary.utils.ui.UIHelper;
import razerdp.github.com.baseuilib.R;
import razerdp.github.com.baseuilib.widget.pullrecyclerview.wrapperadapter.FixedViewInfo;
import razerdp.github.com.baseuilib.widget.pullrecyclerview.wrapperadapter.HeaderViewWrapperAdapter;
import razerdp.github.com.baselibrary.utils.ui.AnimUtils;
import razerdp.github.com.baseuilib.widget.pullrecyclerview.interfaces.OnRefreshListener2;
import razerdp.github.com.baselibrary.utils.ui.ViewOffsetHelper;

import static me.everything.android.ui.overscroll.IOverScrollState.STATE_BOUNCE_BACK;
import static me.everything.android.ui.overscroll.IOverScrollState.STATE_DRAG_END_SIDE;
import static me.everything.android.ui.overscroll.IOverScrollState.STATE_DRAG_START_SIDE;
import static me.everything.android.ui.overscroll.IOverScrollState.STATE_IDLE;
import static razerdp.github.com.baseuilib.widget.pullrecyclerview.CircleRecyclerView.Mode.FROM_BOTTOM;
import static razerdp.github.com.baseuilib.widget.pullrecyclerview.CircleRecyclerView.Mode.FROM_START;
import static razerdp.github.com.baseuilib.widget.pullrecyclerview.CircleRecyclerView.Status.DEFAULT;
import static razerdp.github.com.baseuilib.widget.pullrecyclerview.CircleRecyclerView.Status.REFRESHING;


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
    private static final String TAG = "CircleRecyclerView";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({DEFAULT, REFRESHING})
    @interface Status {
        int DEFAULT = 0;
        int REFRESHING = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FROM_START, FROM_BOTTOM})
    @interface Mode {
        int FROM_START = 0;
        int FROM_BOTTOM = 1;
    }

    @Status
    private int currentStatus;

    @Mode
    private int pullMode;


    //observer
    private InnerRefreshIconObserver iconObserver;

    //callback
    private OnRefreshListener2 onRefreshListener;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ImageView refreshIcon;

    private int refreshPosition;
    private PullRefreshFooter footerView;


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
        if (isInEditMode()) return;
        GradientDrawable background = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xff323232, 0xff323232, 0xffffffff, 0xffffffff});
        setBackground(background);

        if (recyclerView == null) {
            recyclerView = (RecyclerView) LayoutInflater.from(context).inflate(R.layout.view_recyclerview, this, false);
            //new出来的recyclerview并没有滚动条，原因：没有走到View.initializeScrollbars(TypedArray a)
            //recyclerView = new RecyclerView(context);
            recyclerView.setBackgroundColor(Color.WHITE);
            linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            //渲染优化，放到render thread做，（prefetch在v25之后可用），机型在萝莉炮(lollipop)后才可以享受此优化（事实上默认是开启的）
            linearLayoutManager.setItemPrefetchEnabled(true);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        //取消默认item变更动画
        recyclerView.setItemAnimator(null);

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

        footerView = new PullRefreshFooter(getContext());

        addFooterView(footerView);

        recyclerView.addOnScrollListener(onScrollListener);
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
        Log.i(TAG, "compelete");
        if (pullMode == FROM_START && iconObserver != null) {
            iconObserver.catchResetEvent();
        }
        if (pullMode == FROM_BOTTOM && footerView != null) {
            footerView.onFinish();
        }
        setCurrentStatus(DEFAULT);
    }

    public void autoRefresh() {
        if (currentStatus == REFRESHING || pullMode == FROM_BOTTOM || iconObserver == null || onRefreshListener == null) return;
        pullMode = FROM_START;
        setCurrentStatus(REFRESHING);
        iconObserver.autoRefresh();
        onRefreshListener.onRefresh();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure() called with: widthMeasureSpec = [" + widthMeasureSpec + "], heightMeasureSpec = [" + heightMeasureSpec + "]");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout: ");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (onPreDispatchTouchListener != null) {
            onPreDispatchTouchListener.onPreTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    //------------------------------------------get/set-----------------------------------------------

    public OnRefreshListener2 getOnRefreshListener() {
        return onRefreshListener;
    }

    public void setOnRefreshListener(OnRefreshListener2 onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public OnPreDispatchTouchListener getOnPreDispatchTouchListener() {
        return onPreDispatchTouchListener;
    }

    public void setOnPreDispatchTouchListener(OnPreDispatchTouchListener onPreDispatchTouchListener) {
        this.onPreDispatchTouchListener = onPreDispatchTouchListener;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            if (mHeaderViewInfos.size() > 0 || mFooterViewInfos.size() > 0) {
                recyclerView.setAdapter(wrapHeaderRecyclerViewAdapterInternal(adapter, mHeaderViewInfos, mFooterViewInfos));
            } else {
                recyclerView.setAdapter(adapter);
            }
        }
        initOverScroll();
    }

    private void initOverScroll() {
        IOverScrollDecor decor = new VerticalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(
                recyclerView), 2f, 1f, 2f);
        decor.setOverScrollStateListener(new IOverScrollStateListener() {
            @Override
            public void onOverScrollStateChange(IOverScrollDecor decor,
                                                int oldState,
                                                int newState) {
                switch (newState) {
                    case STATE_IDLE:
                        // No over-scroll is in effect.
                        break;
                    case STATE_DRAG_START_SIDE:
                        // Dragging started at the left-end.
                        break;
                    case STATE_DRAG_END_SIDE:
                        // Dragging started at the right-end.
                        KLog.i("refreshState", "current state  >>>   " + currentStatus + "   refresh mode  >>>   " + pullMode);

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
                    iconObserver.catchPullEvent(offset);
                    if (offset >= refreshPosition && state == STATE_BOUNCE_BACK) {
                        if (currentStatus != REFRESHING) {
                            setCurrentStatus(REFRESHING);
                            if (onRefreshListener != null) {
                                Log.i(TAG, "refresh");
                                onRefreshListener.onRefresh();
                            }
                            pullMode = FROM_START;
                            iconObserver.catchRefreshEvent();
                        }
                    }
                } else if (offset < 0) {
                    //底部的overscroll
                }
            }
        });
    }

    /**
     * 判断recyclerview是否滑到底部
     * <p>
     * 原理：判断滑过的距离加上屏幕上的显示的区域是否比整个控件高度高
     *
     * @return
     */
    public boolean isScrollToBottom() {
        return recyclerView != null && recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange();
    }


    public int findFirstVisibleItemPosition() {
        return linearLayoutManager.findFirstVisibleItemPosition();
    }


    /**
     * scroll listener
     */
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //fix issue #42
            //按照原来的习惯，我是当RecyclerView滑动停止状态下才检查是否要自动加在更多，但就出现了一个问题，如#42提出的，手指不离开屏幕一直滑动
            //就会无法加载更多，在iOS朋友圈里，不离开屏幕是可以继续加载的，因此将这里的逻辑移动到了onScrolled，不对滑动状态进行监听了
            /*if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (isScrollToBottom() && currentStatus != REFRESHING) {
                    onRefreshListener.onLoadMore();
                    KLog.i("loadmoretag", "loadmore");
                    pullMode = FROM_BOTTOM;
                    setCurrentStatus(REFRESHING);
                    footerView.onRefreshing();
                }
            }*/
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (isScrollToBottom() && currentStatus != REFRESHING) {
                onRefreshListener.onLoadMore();
                KLog.i("loadmoretag", "loadmore");
                pullMode = FROM_BOTTOM;
                setCurrentStatus(REFRESHING);
                footerView.onRefreshing();
            }
        }
    };


    /**
     * 刷新Icon的动作观察者
     */

    private static class InnerRefreshIconObserver {
        private ViewOffsetHelper viewOffsetHelper;
        private ImageView refreshIcon;
        private final int refreshPosition;
        private RotateAnimation rotateAnimation;
        private ValueAnimator mValueAnimator;

        InnerRefreshIconObserver(ImageView refreshIcon, int refreshPosition) {
            this.refreshIcon = refreshIcon;
            this.refreshPosition = refreshPosition;

            viewOffsetHelper = new ViewOffsetHelper(refreshIcon);

            rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(1000);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatCount(Animation.INFINITE);
            rotateAnimation.setFillBefore(true);

        }

        void catchPullEvent(float offset) {
            if (checkHacIcon()) {
                refreshIcon.setRotation(-offset * 2);
                if (offset >= refreshPosition) {
                    offset = refreshPosition;
                }
                viewOffsetHelper.absoluteOffsetTopAndBottom((int) offset);
                adjustRefreshIconPosition();
            }
        }

        /**
         * 调整icon的位置界限
         */
        private void adjustRefreshIconPosition() {
            if (refreshIcon.getY() < 0) {
                refreshIcon.offsetTopAndBottom(Math.abs(refreshIcon.getTop()));
            } else if (refreshIcon.getY() > refreshPosition) {
                refreshIcon.offsetTopAndBottom(-(refreshIcon.getTop() - refreshPosition));
            }
        }

        void catchRefreshEvent() {
            if (checkHacIcon()) {
                refreshIcon.clearAnimation();
                if (refreshIcon.getTop() < refreshPosition) {
                    viewOffsetHelper.absoluteOffsetTopAndBottom(refreshPosition);
                }
                refreshIcon.startAnimation(rotateAnimation);
            }
        }

        void catchResetEvent() {
            KLog.i("refreshTop", " top  >>>  " + refreshIcon.getTop());
            if (mValueAnimator == null) {
                mValueAnimator = ValueAnimator.ofFloat(refreshPosition, 0);
                mValueAnimator.setInterpolator(new LinearInterpolator());
                mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float result = (float) animation.getAnimatedValue();
                        catchPullEvent(result);
                    }
                });
                mValueAnimator.addListener(new AnimUtils.SimpleAnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        refreshIcon.clearAnimation();
                    }
                });
                mValueAnimator.setDuration(540);
            }

            refreshIcon.post(new Runnable() {
                @Override
                public void run() {
                    mValueAnimator.start();
                }
            });
        }

        private boolean checkHacIcon() {
            return refreshIcon != null;
        }

        void autoRefresh() {
            ValueAnimator animator = ValueAnimator.ofFloat(0, refreshPosition);
            animator.setInterpolator(new LinearInterpolator());
            animator.setDuration(540);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float result = (float) animation.getAnimatedValue();
                    catchPullEvent(result);
                }
            });
            animator.addListener(new AnimUtils.SimpleAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    catchRefreshEvent();
                }
            });
            animator.start();
        }
    }


    //------------------------------------------分割线-----------------------------------------------
    //------------------------------------------分割线-----------------------------------------------
    //------------------------------------------分割线-----------------------------------------------

    /**
     * 以下为recyclerview 的headeradapter实现方案
     * <p>
     * 以Listview的headerView和footerView为模板做出的recyclerview的header和footer
     */
    private ArrayList<FixedViewInfo> mHeaderViewInfos = new ArrayList<>();
    private ArrayList<FixedViewInfo> mFooterViewInfos = new ArrayList<>();

    /**
     * 不完美解决方法：添加一个header，则从-2开始减1
     * header:-2~-98
     */
    private static final int ITEM_VIEW_TYPE_HEADER_START = -2;
    /**
     * 不完美解决方法：添加一个header，则从-99开始减1
     * footer:-99~-无穷
     */
    private static final int ITEM_VIEW_TYPE_FOOTER_START = -99;

    public void addHeaderView(View headerView) {
        final FixedViewInfo info = new FixedViewInfo(headerView, ITEM_VIEW_TYPE_HEADER_START - mHeaderViewInfos.size());
        if (mHeaderViewInfos.size() == Math.abs(ITEM_VIEW_TYPE_FOOTER_START - ITEM_VIEW_TYPE_HEADER_START)) {
            mHeaderViewInfos.remove(mHeaderViewInfos.size() - 1);
        }
        if (checkFixedViewInfoNotAdded(info, mHeaderViewInfos)) {
            mHeaderViewInfos.add(info);
        }
        checkAndNotifyWrappedViewAdd(recyclerView.getAdapter(), info, true);

    }

    private void checkAndNotifyWrappedViewAdd(RecyclerView.Adapter adapter, FixedViewInfo info, boolean isHeader) {
        //header和footer只能再setAdapter前使用，如果是set了之后再用，为何不add普通的viewholder而非要Headr或者footer呢
        if (adapter != null) {
            if (!(adapter instanceof HeaderViewWrapperAdapter)) {
                adapter = wrapHeaderRecyclerViewAdapterInternal(adapter);
                if (isHeader) {
                    adapter.notifyItemInserted(((HeaderViewWrapperAdapter) adapter).findHeaderPosition(info.view));
                } else {
                    adapter.notifyItemInserted(((HeaderViewWrapperAdapter) adapter).findFooterPosition(info.view));
                }
            }
        }
    }

    public void addFooterView(View footerView) {
        final FixedViewInfo info = new FixedViewInfo(footerView, ITEM_VIEW_TYPE_FOOTER_START - mFooterViewInfos.size());
        if (checkFixedViewInfoNotAdded(info, mFooterViewInfos)) {
            mFooterViewInfos.add(info);
        }
        checkAndNotifyWrappedViewAdd(recyclerView.getAdapter(), info, false);
    }

    private boolean checkFixedViewInfoNotAdded(FixedViewInfo info, List<FixedViewInfo> infoList) {
        boolean result = true;
        if (ToolUtil.isListEmpty(infoList) || info == null) {
            result = true;
        } else {
            for (FixedViewInfo fixedViewInfo : infoList) {
                if (fixedViewInfo.view == info.view) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public int getHeaderViewCount() {
        return mHeaderViewInfos.size();
    }

    public int getFooterViewCount() {
        return mFooterViewInfos.size();
    }


    protected HeaderViewWrapperAdapter wrapHeaderRecyclerViewAdapterInternal(@NonNull RecyclerView.Adapter mWrappedAdapter,
                                                                             ArrayList<FixedViewInfo> mHeaderViewInfos,
                                                                             ArrayList<FixedViewInfo> mFooterViewInfos) {
        return new HeaderViewWrapperAdapter(recyclerView, mWrappedAdapter, mHeaderViewInfos, mFooterViewInfos);

    }

    protected HeaderViewWrapperAdapter wrapHeaderRecyclerViewAdapterInternal(@NonNull RecyclerView.Adapter mWrappedAdapter) {
        return wrapHeaderRecyclerViewAdapterInternal(mWrappedAdapter, mHeaderViewInfos, mFooterViewInfos);

    }


    //------------------------------------------interface-----------------------------------------------
    private OnPreDispatchTouchListener onPreDispatchTouchListener;

    public interface OnPreDispatchTouchListener {
        boolean onPreTouch(MotionEvent ev);
    }


}
