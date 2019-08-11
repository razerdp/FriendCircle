package com.razerdp.github.uilib.widget.pullrefresh.interfaces;

import com.razerdp.github.uilib.widget.pullrefresh.PullRefreshLayout;

/**
 * Created by 大灯泡 on 2019/8/4.
 */
public interface OnPullLayoutStateListener {

    void onStart(PullRefreshLayout.LayoutState state);

    void onPull(PullRefreshLayout.LayoutState state);

    void onRelease(PullRefreshLayout.LayoutState state);

    void onReset(PullRefreshLayout.LayoutState state);

}
