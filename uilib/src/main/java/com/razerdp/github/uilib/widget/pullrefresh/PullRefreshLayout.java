package com.razerdp.github.uilib.widget.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.razerdp.github.uilib.widget.pullrefresh.interfaces.OnPullLayoutStateListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ListViewCompat;
import androidx.customview.widget.ViewDragHelper;

/**
 * Created by 大灯泡 on 2019/8/4.
 * 下拉框架
 */
public class PullRefreshLayout extends FrameLayout implements OnPullLayoutStateListener {
    private static final String TAG = "PullRefreshLayout";

    private ViewDragHelper mDragHelper;
    private State mState;
    private LayoutState mLayoutState;
    private Config mConfig = new Config();

    private View mHeader;
    private View mContent;

    private OnCheckPullListener mOnCheckPullListener;

    private ViewDragHelper.Callback mDrageCallback = new ViewDragHelper.Callback() {
        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return onTryCaptureView(child, indexOfChild(child)) ? 1 : 0;
        }

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return onTryCaptureView(child, pointerId);
        }

        //这里的view是content，
        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            if (mHeader == null) return;

            onDrag(changedView, left, top, dx, dy);
            mHeader.layout(mHeader.getLeft(),
                    top - mHeader.getHeight(),
                    mHeader.getRight(),
                    top);
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {

        }
    };

    public PullRefreshLayout(@NonNull Context context) {
        this(context, null);
    }

    public PullRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDragHelper = ViewDragHelper.create(this, 1.0f, mDrageCallback);
        mState = new State();
        mLayoutState = new LayoutState();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            mHeader = getChildAt(0);
            mContent = getChildAt(1);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (getChildCount() < 1) return;

        if (mHeader == null) {
            mHeader = getChildAt(0);
        }

        if (mContent == null) {
            mContent = getChildAt(1);
        }

        mHeader.layout(mHeader.getLeft(),
                mContent.getTop() - mHeader.getHeight(),
                mHeader.getRight(),
                mContent.getTop());
        if (mConfig.mRefreshPosition > 0) {
            mState.refreshPosition = mConfig.mRefreshPosition;
        } else if (mConfig.mRefreshRatio > 0) {
            mState.refreshPosition = (int) (mHeader.getHeight() * mConfig.mRefreshRatio);
        } else {
            mState.refreshPosition = (int) (mHeader.getHeight() * Config.DEFAULT_REFRESH_RATIO);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }


    //============================================================下拉检测
    private boolean onTryCaptureView(View child, int pointerId) {
        if (!mState.canPull() || child != mContent) return false;
        boolean canPull = false;
        if (mOnCheckPullListener != null) {
            canPull = mOnCheckPullListener.onCheckPullable(child, pointerId);
        } else {
            canPull = checkPullableInternal(child, pointerId);
        }
        return canPull;
    }

    private boolean checkPullableInternal(View child, int pointerId) {
        if (child instanceof ListView) {
            return ListViewCompat.canScrollList((ListView) child, -1);
        }
        //canScrollVertically返回fasle，即到顶部无法继续下滑，此时就可以认为允许下拉刷新了
        return !child.canScrollVertically(-1);
    }

    private void onDrag(View changedView, int left, int top, int dx, int dy) {
        mLayoutState.dx = dx;
        mLayoutState.dy = dy;
        mLayoutState.scrollX += dx;
        mLayoutState.scrollY += dy;
    }

    //=============================================================
    @Override
    public void onStart(LayoutState state) {

    }

    @Override
    public void onPull(LayoutState state) {

    }

    @Override
    public void onRelease(LayoutState state) {

    }

    @Override
    public void onReset(LayoutState state) {

    }

    public static class LayoutState {
        int dx;
        int dy;
        int scrollX;
        int scrollY;


    }

    private class State {

        static final int STATE_IDLE=0;
        static final int STATE_REFRESH=1;
        static final int STATE_ONDRAGE=2;
        static final int STATE_RELEASE=3;

        int state;
        int refreshPosition;

        boolean canPull() {
            return mConfig.mCanPull && getChildCount() > 1;
        }
    }
}
