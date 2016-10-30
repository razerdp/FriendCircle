package razerdp.friendcircle.widget.pullrecyclerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.IntDef;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import com.socks.library.KLog;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import razerdp.friendcircle.widget.pullrecyclerview.interfaces.CirclePtrRefreshView;
import razerdp.friendcircle.widget.pullrecyclerview.interfaces.OnRefreshListener2;
import razerdp.friendcircle.widget.pullrecyclerview.interfaces.PtrState;

import static razerdp.friendcircle.widget.pullrecyclerview.CircleRecyclerView.Status.*;


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
 * 1 - 下拉和上拉支持回调
 * 2 - 跟iOS朋友圈下拉头部一样，支持头部在刷新时的回弹
 * 3 - 滑动到底部自动加载更多
 * 4 - addHeaderView。
 */

public class CircleRecyclerView extends LinearLayout {
    public static boolean DEBUG = true;
    private static final String TAG = "CircleRecyclerView";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({DEFAULT, PULL_TO_REFRESH, REFRESHING})
    @interface Status {
        int DEFAULT = 0;
        int PULL_TO_REFRESH = 1;
        int REFRESHING = 2;
    }

    @Status
    private int currentStatus;

    //observer
    private InnerRefreshViewObserver refreshViewObserver;
    private InnerMotionEventObserver motionEventObserver;

    //callback
    private OnRefreshListener2 onRefreshListener;

    //view
    private CirclePtrRefreshView mPtrHeaderView;
    private CirclePtrRefreshView mPtrFooterView;
    private RecyclerView recyclerView;

    //options
    private int positionForRefresh;
    private boolean canPull = true;


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
        setOrientation(VERTICAL);
        setBackgroundColor(Color.TRANSPARENT);

        refreshViewObserver = new InnerRefreshViewObserver();
        motionEventObserver = new InnerMotionEventObserver(context);

        if (mPtrHeaderView == null) {
            mPtrHeaderView = new CirclePtrHeaderView(context);
        }
        if (mPtrFooterView == null) {
            mPtrFooterView = new CirclePtrFooterView(context);
        }
        if (recyclerView == null) {
            recyclerView = new RecyclerView(context);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, VERTICAL, false));
        }
        addView((View) mPtrHeaderView);
        addView(recyclerView);
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 2) {
            throw new IllegalStateException("超过了两个子view哦");
        }
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        checkRefreshViewValided();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mPtrHeaderView != null) {
            measureChildWithMargins((View) mPtrHeaderView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams ptrHeaderLayoutParams = (MarginLayoutParams) ((View) mPtrHeaderView).getLayoutParams();
            int ptrHeaderWidth = ((View) mPtrHeaderView).getMeasuredWidth() + ptrHeaderLayoutParams.leftMargin + ptrHeaderLayoutParams.rightMargin;
            int ptrHeaderHeight = ((View) mPtrHeaderView).getMeasuredHeight() + ptrHeaderLayoutParams.topMargin + ptrHeaderLayoutParams.bottomMargin;
            refreshViewObserver.setPtrHeaderWidth(ptrHeaderWidth);
            refreshViewObserver.setPtrHeaderHeight(ptrHeaderHeight);
        }

      /*  if (mPtrFooterView != null) {
            measureChildWithMargins((View) mPtrFooterView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams ptrFooterLayoutParams = (MarginLayoutParams) ((View) mPtrFooterView).getLayoutParams();
            int ptrFooterWidth = ((View) mPtrFooterView).getMeasuredWidth() + ptrFooterLayoutParams.leftMargin + ptrFooterLayoutParams.rightMargin;
            int ptrFooterHeight = ((View) mPtrFooterView).getMeasuredHeight() + ptrFooterLayoutParams.topMargin + ptrFooterLayoutParams.bottomMargin;
            refreshViewObserver.setPtrFooterWidth(ptrFooterWidth);
            refreshViewObserver.setPtrFooterHeight(ptrFooterHeight);
        }*/
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mPtrHeaderView != null) {
            final int left = 0;
            final int top = -refreshViewObserver.getPtrHeaderHeight() + motionEventObserver.getCurrentY();
            final int right = left + refreshViewObserver.getPtrHeaderWidth();
            final int bottom = top + refreshViewObserver.getPtrHeaderHeight();
            //摆放header的位置，受滑动距离影响，设为负值让其隐藏
            ((View) mPtrHeaderView).layout(left, top, right, bottom);
        }
        super.onLayout(changed, l, t, r, b);

    }

    private void setCurrentStatus(@Status int status) {
        this.currentStatus = status;

    }

    private void checkRefreshViewValided() {
        if (mPtrHeaderView != null && !(mPtrHeaderView instanceof View)) {
            throw new IllegalStateException("诶多。。。那个。。。接口都有个View字哦，请给一个View来-V-");
        }
        if (mPtrFooterView != null && !(mPtrFooterView instanceof View)) {
            throw new IllegalStateException("诶多。。。那个。。。接口都有个View字哦，请给一个View来-V-");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isCanPull()) {
            return super.dispatchTouchEvent(ev);
        } else {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    motionEventObserver.catchTouchDown(ev.getX(), ev.getY());
                    super.dispatchTouchEvent(ev);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    motionEventObserver.catchTouchMove(ev.getX(), ev.getY());
                    float offsetX = motionEventObserver.getOffsetX();
                    float offsetY = motionEventObserver.getOffsetY();
                    //横向移动比纵向大，则传递下去
                    if (Math.abs(offsetX) > Math.abs(offsetY * 2)) {
                        if (DEBUG) {
                            Log.d(TAG, "横向移动，取消事件");
                        }
                        return super.dispatchTouchEvent(ev);
                    }

                    if (motionEventObserver.isDrag()) {
                        if (motionEventObserver.isValidTouchSlop(offsetY)) {
                            doMove(ev);
                            return true;
                        }
                    }
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 处理移动事件
     */
    private void doMove(MotionEvent ev) {

        //如果是下拉
        if (motionEventObserver.isFromTop()) {
            mPtrHeaderView.onStart(PtrState.START_TO_PULL, positionForRefresh);
        }
        //刷新位置
        int currentY = (int) (motionEventObserver.getCurrentY() + motionEventObserver.getOffsetY());
        Log.i(TAG, ">>>isPullDown:   " + motionEventObserver.isPullDown() + "<<<  currentY  =  " + currentY);
        motionEventObserver.setCurrentY(currentY);
        if (currentY < positionForRefresh) {
            mPtrHeaderView.onMoved(PtrState.PULL_TO_REFRESH, positionForRefresh, (int) motionEventObserver.getOffsetY());
        } else {
            mPtrHeaderView.onMoved(PtrState.RELEASE_TO_REFRESH, positionForRefresh, (int) motionEventObserver.getOffsetY());
        }
        ((View) mPtrHeaderView).offsetTopAndBottom((int) motionEventObserver.getOffsetY());
        recyclerView.offsetTopAndBottom((int) motionEventObserver.getOffsetY());

    }

    //------------------------------------------get/set-----------------------------------------------

    public OnRefreshListener2 getOnRefreshListener() {
        return onRefreshListener;
    }

    public void setOnRefreshListener(OnRefreshListener2 onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public boolean isCanPull() {
        return canPull;
    }

    public void setCanPull(boolean canPull) {
        this.canPull = canPull;
    }

    /**
     * ============分割线===============
     * 各种状态的观察
     * ============分割线===============
     */

    /**
     * 刷新View的观察者
     */
    private static class InnerRefreshViewObserver {
        private static final String INNERTAG = "InnerRefreshViewObserve";
        private int ptrHeaderWidth;
        private int ptrHeaderHeight;
        private int ptrFooterWidth;
        private int ptrFooterHeight;

        InnerRefreshViewObserver() {
        }

        int getPtrHeaderWidth() {
            return ptrHeaderWidth;
        }

        void setPtrHeaderWidth(int ptrHeaderWidth) {
            this.ptrHeaderWidth = ptrHeaderWidth;
            if (DEBUG) {
                Log.i(INNERTAG, "ptrHeaderWidth   >>>   " + ptrHeaderWidth);
            }
        }

        int getPtrHeaderHeight() {
            return ptrHeaderHeight;
        }

        void setPtrHeaderHeight(int ptrHeaderHeight) {
            this.ptrHeaderHeight = ptrHeaderHeight;
            if (DEBUG) {
                Log.i(INNERTAG, "ptrHeaderHeight   >>>   " + ptrHeaderWidth);
            }
        }

        int getPtrFooterWidth() {
            return ptrFooterWidth;
        }

        void setPtrFooterWidth(int ptrFooterWidth) {
            this.ptrFooterWidth = ptrFooterWidth;
            if (DEBUG) {
                Log.i(INNERTAG, "ptrFooterWidth   >>>   " + ptrHeaderWidth);
            }
        }

        int getPtrFooterHeight() {
            return ptrFooterHeight;
        }

        void setPtrFooterHeight(int ptrFooterHeight) {
            this.ptrFooterHeight = ptrFooterHeight;
            if (DEBUG) {
                Log.i(INNERTAG, "ptrFooterHeight   >>>   " + ptrHeaderWidth);
            }
        }
    }

    /**
     * 手势动作的观察者
     */
    private static class InnerMotionEventObserver {
        //是否正在拖动拖动
        private boolean isDrag;
        //是否从初始状态拖动
        private boolean isFirstDrag;
        //是否到达顶部
        private boolean returnToTop;
        //上一次的位置
        private PointF lastPos;

        private int currentY;

        //位移量
        private float offsetX;
        private float offsetY;

        //最小滑动判定值
        private int minTouchSlop;

        InnerMotionEventObserver(Context context) {
            lastPos = new PointF();
            ViewConfiguration conf = ViewConfiguration.get(context);
            minTouchSlop = conf.getScaledTouchSlop();
        }

        void catchTouchDown(float x, float y) {
            isDrag = true;
            lastPos.set(x, y);
        }

        void catchTouchMove(float x, float y) {
            this.offsetX = x - lastPos.x;
            //阻尼1.2
            this.offsetY = (y - lastPos.y) / 1.2f;
            lastPos.set(x, y);
        }

        boolean isDrag() {
            return isDrag;
        }

        void setDrag(boolean drag) {
            isDrag = drag;
        }

        boolean isFirstDrag() {
            return isFirstDrag;
        }

        void setFirstDrag(boolean firstDrag) {
            isFirstDrag = firstDrag;
        }

        float getOffsetX() {
            return offsetX;
        }

        float getOffsetY() {
            return offsetY;
        }

        int getCurrentY() {
            return currentY < 0 ? 0 : currentY;
        }

        void setCurrentY(int currentY) {
            this.currentY = currentY;
            returnToTop = currentY <= 0;
        }

        boolean isValidTouchSlop(float offset) {
            return Math.abs(offset) >= minTouchSlop;
        }

        boolean isPullDown() {
            return offsetY > 0;
        }

        boolean isFromTop() {
            return currentY == 0;
        }
    }
}
