package razerdp.friendcircle.widget.ptrwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import razerdp.friendcircle.R;
import razerdp.friendcircle.api.ptrwidget.OnLoadMoreRefreshListener;
import razerdp.friendcircle.api.ptrwidget.OnPullDownRefreshListener;
import razerdp.friendcircle.api.ptrwidget.PullMode;
import razerdp.friendcircle.api.ptrwidget.PullStatus;

/**
 * Created by 大灯泡 on 2016/2/9.
 * 用于朋友圈的下拉刷新控件
 * 扩展自android-Ultra-Pull-To-Refresh
 * git:
 * https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh
 */
public class FriendCirclePtrListView extends PtrFrameLayout implements PtrHandler{
    private static final String TAG = "FriendCirclePtrListView";

    //=============================================================元素定义
    private ListView mListView;
    private FriendCirclePtrHeader mHeader;
    private FriendCirclePtrFooter mFooter;
    //=============================================================接口
    private OnPullDownRefreshListener mOnPullDownRefreshListener;
    private OnLoadMoreRefreshListener mOnLoadMoreRefreshListener;

    //=============================================================status
    private PullStatus loadmoreState;
    private PullMode curMode;

    //=============================================================参数
    //是否有下一页
    private boolean hasMore;

    public FriendCirclePtrListView(Context context) {
        this(context,null);
    }

    public FriendCirclePtrListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FriendCirclePtrListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        //header
        mHeader=new FriendCirclePtrHeader(context);
        //listview
        mListView = new ListView(context);
        mListView.setSelector(android.R.color.transparent);
        mListView.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //footer
        mFooter=new FriendCirclePtrFooter(context);

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
        curMode=PullMode.FROM_START;
        loadmoreState=PullStatus.NORMAL;
        if (mOnPullDownRefreshListener!=null)mOnPullDownRefreshListener.onRefreshing(frame);

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
    public PullStatus getPullStatus() {
        return mHeader.getPullStatus();
    }


    //=============================================================tools
    int lastItem=0;
    private void setScrollListener() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (mOnLoadMoreRefreshListener != null) {
                    if (SCROLL_STATE_IDLE == scrollState &&
                            0 != mListView.getFirstVisiblePosition() && lastItem == mListView.getCount()) {
                        if (hasMore && loadmoreState != PullStatus.REFRESHING) {
                            //setLoadMoreState(PullToRefreshState.REFRESHING);
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG,"-----------执行了measure");
    }

    @Override
    protected void onLayout(boolean flag, int i, int j, int k, int l) {
        super.onLayout(flag, i, j, k, l);
        Log.d(TAG,"-----------执行了layout");

    }
}
