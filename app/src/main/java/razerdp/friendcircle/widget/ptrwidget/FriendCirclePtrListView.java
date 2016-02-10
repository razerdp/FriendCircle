package razerdp.friendcircle.widget.ptrwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import razerdp.friendcircle.R;
import razerdp.friendcircle.api.ptrwidget.OnLoadMoreRefreshListener;
import razerdp.friendcircle.api.ptrwidget.OnPullDownRefreshListener;
import razerdp.friendcircle.api.ptrwidget.PullMode;
import razerdp.friendcircle.api.ptrwidget.PullState;

/**
 * Created by 大灯泡 on 2016/2/9.
 * 用于朋友圈的下拉刷新控件
 * 扩展自android-Ultra-Pull-To-Refresh
 * git:
 * https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh
 */
public class FriendCirclePtrListView extends PtrFrameLayout implements PtrHandler {
    private static final String TAG = "FriendCirclePtrListView";

    //=============================================================元素定义
    private ListView mListView;
    private FriendCirclePtrHeader mHeader;
    private FriendCirclePtrFooter mFooter;
    //=============================================================接口
    private OnPullDownRefreshListener mOnPullDownRefreshListener;
    private OnLoadMoreRefreshListener mOnLoadMoreRefreshListener;

    //=============================================================status
    private PullState loadmoreState=PullState.NORMAL;
    private PullMode curMode;

    //=============================================================参数
    //是否有下一页
    private boolean hasMore;
    private boolean canPull=true;

    public FriendCirclePtrListView(Context context) {
        this(context, null);
    }

    public FriendCirclePtrListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FriendCirclePtrListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
        initAttrs(context, attrs);
    }

    private void initView(Context context) {
        //header
        mHeader = new FriendCirclePtrHeader(context);
        //listview
        mListView = new ListView(context);
        mListView.setSelector(android.R.color.transparent);
        mListView.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //footer
        mFooter = new FriendCirclePtrFooter(context);

        //view add
        setHeaderView(mHeader);
        addView(mListView);

        //ptr option
        addPtrUIHandler(mHeader.getPtrUIHandler());
        setPtrHandler(this);
        setResistance(2.3f);
        setRatioOfHeaderHeightToRefresh(.25f);
        setDurationToClose(200);
        setDurationToCloseHeader(1000);
        //刷新时的固定的偏移量
        setOffsetToKeepHeaderWhileLoading(0);

        //下拉刷新，即下拉到距离就刷新而不是松开刷新
        setPullToRefresh(false);
        //刷新的时候保持头部？
        setKeepHeaderWhenRefresh(false);

        setScrollListener();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FriendCirclePtrListView);

        final Drawable selector = a.getDrawable(R.styleable.FriendCirclePtrListView_listSelector);
        if (selector != null) {
            mListView.setSelector(selector);
        }

        mListView.setTranscriptMode(a.getInt(R.styleable.FriendCirclePtrListView_transcriptMode, 0));
        mListView.setCacheColorHint(a.getColor(R.styleable.FriendCirclePtrListView_cacheColorHint, 0));
        mListView.setFastScrollEnabled(a.getBoolean(R.styleable.FriendCirclePtrListView_fastScrollEnabled, false));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mListView.setFastScrollStyle(a.getResourceId(R.styleable.FriendCirclePtrListView_fastScrollStyle, 0));
        }
        mListView.setSmoothScrollbarEnabled(a.getBoolean(R.styleable.FriendCirclePtrListView_smoothScrollbar, true));
        mListView.setChoiceMode(a.getInt(R.styleable.FriendCirclePtrListView_choiceMode, 0));

        final Drawable d = a.getDrawable(R.styleable.FriendCirclePtrListView_listview_divider);
        if (d != null) {
            // Use an implicit divider height which may be explicitly
            // overridden by android:dividerHeight further down.
            mListView.setDivider(d);
        }

        // Use an explicit divider height, if specified.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (a.hasValueOrEmpty(R.styleable.FriendCirclePtrListView_dividerHeight)) {
                final int dividerHeight = a.getDimensionPixelSize(R.styleable.FriendCirclePtrListView_dividerHeight, 0);
                if (dividerHeight != 0) {
                    mListView.setDividerHeight(dividerHeight);
                }
            }
        }
        else {
            final int dividerHeight = a.getDimensionPixelSize(R.styleable.FriendCirclePtrListView_dividerHeight, 0);
            if (dividerHeight != 0) {
                mListView.setDividerHeight(dividerHeight);
            }
        }

        final Drawable osHeader = a.getDrawable(R.styleable.FriendCirclePtrListView_overScrollHeader);
        if (osHeader != null) {
            mListView.setOverscrollHeader(osHeader);
        }

        final Drawable osFooter = a.getDrawable(R.styleable.FriendCirclePtrListView_overScrollFooter);
        if (osFooter != null) {
            mListView.setOverscrollFooter(osFooter);
        }
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        curMode = PullMode.FROM_START;
        loadmoreState = PullState.NORMAL;
        if (mOnPullDownRefreshListener != null) mOnPullDownRefreshListener.onRefreshing(frame);
    }

    //=============================================================Getter/Setter
    public ImageView getRotateIcon() {
        return mHeader.getRotateIcon();
    }

    public void setRotateIcon(ImageView rotateIcon) {
        mHeader.setRotateIcon(rotateIcon);
    }

    public OnPullDownRefreshListener getOnPullDownRefreshListener() {
        return mOnPullDownRefreshListener;
    }

    public void setOnPullDownRefreshListener(OnPullDownRefreshListener onPullDownRefreshListener) {
        mOnPullDownRefreshListener = onPullDownRefreshListener;
    }

    public OnLoadMoreRefreshListener getOnLoadMoreRefreshListener() {
        return mOnLoadMoreRefreshListener;
    }

    public void setOnLoadMoreRefreshListener(OnLoadMoreRefreshListener onLoadMoreRefreshListener) {
        mOnLoadMoreRefreshListener = onLoadMoreRefreshListener;
    }

    public PullMode getCurMode() {
        return curMode;
    }

    public void setCurMode(PullMode curMode) {
        this.curMode = curMode;
    }

    public PullState getPullStatus() {
        return mHeader.getPullState();
    }

    public boolean isCanPull() {
        return canPull;
    }

    public void setCanPull(boolean canPull) {
        this.canPull = canPull;
        setEnabled(canPull);
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        if (mOnLoadMoreRefreshListener == null) return;
        if (mListView.getFooterViewsCount() == 0 && hasMore) {
            mListView.addFooterView(mFooter);
        }
        this.hasMore = hasMore;
    }

    //=============================================================tools
    private void changeLoadMoreState(PullState curLoadMoreState) {
        final PtrUIHandler footerHandler = mFooter.getPtrUIHandler();
        switch (curLoadMoreState) {
            case NORMAL:
                footerHandler.onUIReset(this);
                break;
            case REFRESHING:
                footerHandler.onUIRefreshBegin(this);
                if (mOnLoadMoreRefreshListener != null) mOnLoadMoreRefreshListener.onRefreshing(this);
                break;
            default:
                break;
        }
        loadmoreState = curLoadMoreState;
    }

    public void loadmoreCompelete() {
        changeLoadMoreState(PullState.NORMAL);
    }

    int lastItem = 0;

    private void setScrollListener() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (mOnLoadMoreRefreshListener != null) {
                    if (SCROLL_STATE_IDLE == scrollState &&
                            0 != mListView.getFirstVisiblePosition() && lastItem == mListView.getCount()) {
                        if (hasMore && loadmoreState != PullState.REFRESHING) {
                            // TODO: 2016/2/10 待完成
                            //当有更多同时当前加载更多布局不再刷新状态，则执行刷新
                            curMode = PullMode.FROM_BOTTOM;
                            changeLoadMoreState(PullState.REFRESHING);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount;
            }
        });
    }

    public void autoRefresh() {
        if (mHeader != null) mHeader.setAutoRefresh(true);
        //必须延迟，否则因为过快，还没measure完，也就是headerHeight=0就导致了自动刷新刷在了0位置
        postDelayed(new Runnable() {
            @Override
            public void run() {
                autoRefresh();
            }
        }, 200);
    }

    /** ============================================================= 下面是listview的方法，让ptrframe看起来就是一个listview */
    public void addHeaderView(View v) {
        mListView.addHeaderView(v);
    }

    public void addFooterView(View v) {
        mListView.addFooterView(v);
    }

    public int getHeaderViewsCount() {
        return mListView.getHeaderViewsCount();
    }

    public boolean removeHeaderView(View v) {
        return mListView.removeHeaderView(v);
    }

    public boolean removeFooterView(View v) {
        return mListView.removeFooterView(v);
    }

    public void setSelection(int position) {
        mListView.setSelection(position);
    }

    public boolean post(Runnable action) {
        return mListView.post(action);
    }

    public boolean postDelayed(Runnable action, long delayMillis) {
        return mListView.postDelayed(action, delayMillis);
    }

    public Drawable getDivider() {
        return mListView.getDivider();
    }

    public ListAdapter getAdapter() {
        return mListView.getAdapter();
    }

    public void setAdapter(ListAdapter adapter) {
        mListView.setAdapter(adapter);
    }

    public boolean getItemsCanFocus() {
        return mListView.getItemsCanFocus();
    }

    public int getDividerHeight() {
        return mListView.getDividerHeight();
    }

    public void setDividerHeight(int height) {
        mListView.setDividerHeight(height);
    }

    public void setCacheColorHint(int color) {
        mListView.setCacheColorHint(color);
    }

    public int getCacheColorHint() {
        return mListView.getCacheColorHint();
    }

    public void smoothScrollByOffset(int offset) {
        mListView.smoothScrollByOffset(offset);
    }

    public void smoothScrollToPosition(int position) {
        mListView.smoothScrollToPosition(position);
    }

    public void smoothScrollToPositionFromTop(int position, int offset, int duration) {
        mListView.smoothScrollToPositionFromTop(position, offset, duration);
    }

    public void smoothScrollToPositionFromTop(int position, int offset) {
        mListView.smoothScrollToPositionFromTop(position, offset);
    }

    public boolean removeCallbacks(Runnable action) {
        return mListView.removeCallbacks(action);
    }

    public void removeAllViews() {
        mListView.removeAllViews();
    }

    public void removeView(View child) {
        mListView.removeView(child);
    }

    public void removeViewAt(int index) {
        mListView.removeViewAt(index);
    }

    public void removeViews(int start, int count) {
        mListView.removeViews(start, count);
    }

    public void setEmptyView(View emptyView) {
        mListView.setEmptyView(emptyView);
    }

    public View getEmptyView() {
        return mListView.getEmptyView();
    }
}
