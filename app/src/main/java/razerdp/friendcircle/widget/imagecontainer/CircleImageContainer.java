package razerdp.friendcircle.widget.imagecontainer;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.socks.library.KLog;

import razerdp.friendcircle.utils.SimpleObjectPool;
import razerdp.friendcircle.utils.UIHelper;


/**
 * Created by 大灯泡 on 2016/11/7.
 * <p>
 * 用于朋友圈的九宫格控件
 */

public class CircleImageContainer extends ViewGroup {
    private static final String TAG = "CircleImageContainer";
    //默认的上下左右的分割线
    private int defaultLTRBDivier;

    private BaseCircleImageAdapter mAdapter;
    private InnerViewCacher mInnerViewCacher;

    private CircleImageAdapterObserver mAdapterObservervable = new CircleImageAdapterObserver();


    private int maxChildWidth;

    private int mItemCount;

    private int rowNum;
    private int columnNum;

    private boolean mDataChanged;


    public CircleImageContainer(Context context) {
        super(context);
        init(context);
    }

    public CircleImageContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircleImageContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        defaultLTRBDivier = UIHelper.dipToPx(3f);
        mInnerViewCacher = new InnerViewCacher();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int containerWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int containerHeight;

        //直接定死子控件的宽度,中间两条分割线
        maxChildWidth = containerWidth / 3 - defaultLTRBDivier * 2;

        final int wantedChildCount = mItemCount;
        /**
         * row 1~3 = 1
         * row 4~6 = 2
         * row 7~9 = 3
         *
         * 如上，1~9张按照每个三张可以分成三组，行数实际上是由每组可以整除3的那个决定
         * 其余则是除以3+1决定
         */
        rowNum = wantedChildCount % 3 == 0 ? wantedChildCount / 3 : (wantedChildCount / 3) + 1;
        /**
         * column 1~3 = 1,2,3
         * column 4~9 = 3 (4虽然只有两列，但因为它的两列需要保持跟三列一样的大小，所以取三列)
         *
         * 因此小于散的取本身，大于三的取3
         */
        columnNum = Math.min(wantedChildCount, 3);

        int parentWidth = 0;
        int parentHeight = 0;

        if (mItemCount == 1) {
            //单张图片
            Log.i(TAG, "测量单张");
        } else if (mItemCount == 4) {
            //四张图片
            parentWidth = maxChildWidth * 2 + defaultLTRBDivier;
            parentHeight = parentWidth;
            Log.i(TAG, "测量4张");
        } else {
            //其他数量图片
            parentWidth = maxChildWidth * columnNum + defaultLTRBDivier * (columnNum - 1);
            parentHeight = maxChildWidth * rowNum + (rowNum - 1) * defaultLTRBDivier;
            Log.i(TAG, "测量多张");
        }
        setMeasuredDimension(parentWidth, parentHeight);

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        invalidate();
        if (mAdapter == null) {
            resetContainer();
            return;
        }
        makeAndAddView();

        final int childCount = getChildCount();

        if (childCount == 1) {
            View view = getChildAt(0);
            view.setOnClickListener(onViewClickListener);
            int left = getPaddingLeft();
            int top = getPaddingTop();
            int right = left + view.getMeasuredWidth();
            int bottom = top + view.getMeasuredHeight();
            KLog.d("childCount  >>>   " + childCount + "  left  =  " + left + "  top  =  " + top + "  right  =  " + right + "  bottom  =  " + bottom);
            view.layout(left, top, right, bottom);
        } else if (childCount == 4) {
            for (int i = 0; i < 4; i++) {
                View view = getChildAt(i);
                view.setOnClickListener(onViewClickListener);
                int left = (maxChildWidth + defaultLTRBDivier) * (i % 2) + getPaddingLeft();
                int top = (maxChildWidth + defaultLTRBDivier) * (i / 2) + defaultLTRBDivier + getPaddingTop();
                int right = left + maxChildWidth - getPaddingRight();
                int bottom = top + maxChildWidth - getPaddingBottom();
                view.layout(left, top, right, bottom);
                KLog.d("childCount  >>>   " + childCount + "   position  =  " + i + "  left  =  " + left + "  top  =  " + top + "  right  =  " + right + "  bottom  =  " + bottom);
            }
        } else {
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                view.setOnClickListener(onViewClickListener);
                int left = (maxChildWidth + defaultLTRBDivier) * (i % columnNum) + getPaddingLeft();
                int top = (maxChildWidth + defaultLTRBDivier) * (i / rowNum) + getPaddingTop();
                int right = left + maxChildWidth - getPaddingRight();
                int bottom = top + maxChildWidth - getPaddingBottom();
                view.layout(left, top, right, bottom);
                KLog.d("childCount  >>>   " + childCount + "   position  =  " + i + "  left  =  " + left + "  top  =  " + top + "  right  =  " + right + "  bottom  =  " + bottom);
            }
        }
    }

    private void makeAndAddView() {
        final int childCount = getChildCount();

        if (childCount == mItemCount && !mDataChanged) {
            for (int i = 0; i < childCount; i++) {
                mAdapter.getView((ImageView) getChildAt(i), this, i);
            }
        } else {
            if (childCount == 0) {
                for (int i = 0; i < mItemCount; i++) {
                    addInternalViewOnLayout(i);
                }
            } else {
                for (int i = 0; i < childCount; i++) {
                    mAdapter.getView((ImageView) getChildAt(i), this, i);
                }
                int diff = mItemCount - childCount;
                for (int i = childCount; i < diff; i++) {
                    addInternalViewOnLayout(i);
                }
            }
        }
    }

    private void addInternalViewOnLayout(int position) {
        boolean needToMeasure;
        ImageView cachedView = obtainView();
        needToMeasure = cachedView != null && cachedView.isLayoutRequested();
        View childView = mAdapter.getView(cachedView, this, position);
        LayoutParams params = childView.getLayoutParams();
        if (params == null) {
            params = generateDefaultLayoutParams();
        }
        if (needToMeasure) {
            addViewInLayout(childView, position, params);
        } else {
            attachViewToParent(childView, position, params);
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    public void setAdapter(BaseCircleImageAdapter adapter) {
        if (mAdapter != null) {
            resetContainer();
        }
        if (mAdapter != null && mAdapterObservervable != null) {
            mAdapter.unregisterDataSetObserver(mAdapterObservervable);
        }
        mAdapter = adapter;
        mAdapter.registerDataSetObserver(mAdapterObservervable);
        mAdapter.notifyDataChanged();
    }

    public BaseCircleImageAdapter getAdapter() {
        return mAdapter;
    }

    private void resetContainer() {
        mInnerViewCacher.clearCache();
        removeAllViewsInLayout();
        invalidate();
    }


    private ImageView obtainView() {
        return mInnerViewCacher.get();
    }

    private OnClickListener onViewClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mAdapter != null) {
                Rect rect = new Rect();
                v.getGlobalVisibleRect(rect);
                Rect[] rects = new Rect[mItemCount];
                for (int i = 0; i < mItemCount; i++) {
                    Rect childRect = new Rect();
                    View childView = getChildAt(i);
                    childView.getGlobalVisibleRect(childRect);
                    rects[i] = childRect;
                }
                mAdapter.onItemClick(v, indexOfChild(v), rect, rects);
            }

        }
    };

    private void updateItemCount() {
        mItemCount = mAdapter == null ? 0 : mAdapter.getCount();
    }

    private boolean checkAndCacheView() {
        //因为在listview里面用，那么本view已经是被复用过的，那么如果本来存在有view，则根据数量进行缓存
        if (getChildCount() != 0) {
            final int visibleChildCount = getChildCount();
            boolean needToCacheCount = visibleChildCount - mItemCount > 0;
            if (needToCacheCount) {
                for (int i = mItemCount; i < visibleChildCount; i++) {
                    View child = getChildAt(i);
                    if (child instanceof ImageView) {
                        mInnerViewCacher.put((ImageView) child);
                    }
                    detachViewFromParent(child);
                }
            }
            return needToCacheCount;
        }
        return false;
    }

    private class InnerViewCacher {
        private SimpleObjectPool<ImageView> simpleViewPool;

        InnerViewCacher() {
            simpleViewPool = new SimpleObjectPool<>(9);
        }

        void put(ImageView view) {
            simpleViewPool.put(view);
        }

        ImageView get() {
            return simpleViewPool.get();
        }

        void clearCache() {
            simpleViewPool.clearPool();
        }

    }

    class CircleImageAdapterObserver extends CircleImageDataObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            updateItemCount();
            //做了缓存的话意味着原来的view的数量比新的多，已经进行过remove，所以是可以直接使用
            checkAndCacheView();
            mDataChanged = true;
            requestLayout();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            invalidate();
        }
    }

}
