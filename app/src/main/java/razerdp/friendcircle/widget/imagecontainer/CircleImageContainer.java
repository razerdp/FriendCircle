package razerdp.friendcircle.widget.imagecontainer;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
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

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int singleChildMaxWidth = 0;
        int singleChildHeight = 0;

        final int childCount = getChildCount();

        //直接定死子控件的宽度,中间两条分割线
        maxChildWidth = containerWidth / 3 - defaultLTRBDivier * 2;

        if (childCount != 1) {
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                childView.measure(MeasureSpec.makeMeasureSpec(maxChildWidth, widthMode), MeasureSpec.makeMeasureSpec(maxChildWidth, heightMode));
                childView.setOnClickListener(onViewClickListener);
            }
            KLog.i("测量多个子控件");
        } else {
            //单图做宽高最大限制
            View childView = getChildAt(0);
            childView.setOnClickListener(onViewClickListener);
            childView.measure(widthMeasureSpec,heightMeasureSpec);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            float ratio = childWidth * 1.0f / childHeight * 1.0f;
            if (ratio != 0) {
                singleChildMaxWidth = Math.min(childWidth, containerWidth);
                singleChildHeight = (int) (singleChildMaxWidth / ratio);
                childView.measure(MeasureSpec.makeMeasureSpec(singleChildMaxWidth, widthMode), MeasureSpec.makeMeasureSpec(singleChildHeight, heightMode));
            }
            KLog.i("测量单个控件");

        }

        //针对一张图，4张图的处理
        if (childCount == 4) {
            containerWidth = maxChildWidth * 2 + defaultLTRBDivier;
            containerHeight = containerWidth;
        } else if (childCount == 1) {
            containerWidth = singleChildMaxWidth == 0 ? getChildAt(0).getMeasuredWidth() : singleChildMaxWidth;
            containerHeight = singleChildHeight == 0 ? getChildAt(0).getMeasuredHeight() : singleChildHeight;
        } else {
            int rowNum = childCount % 3 == 0 ? childCount / 3 : (childCount / 3) + 1;
            int columnNum = Math.min(childCount, 3);
            containerWidth = maxChildWidth * columnNum + defaultLTRBDivier * (columnNum - 1);
            containerHeight = rowNum * maxChildWidth + (rowNum - 1) * defaultLTRBDivier;
        }
        setMeasuredDimension(containerWidth, containerHeight);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int rowNum;
        int columnNum;

        final int childCount = mAdapter == null ? 0 : mAdapter.getCount();
        if (childCount == 1) {
            rowNum = 1;
            columnNum = 1;
        } else {
            rowNum = childCount % 3 == 0 ? childCount / 3 : (childCount / 3) + 1;
            columnNum = Math.min(childCount, 3);
        }

        if (childCount == 1) {
            View view = getChildAt(0);
            int left = getPaddingLeft();
            int top = getPaddingTop();
            int right = left + view.getMeasuredWidth();
            int bottom = top + view.getMeasuredHeight();
            KLog.d("childCount  >>>   " + childCount + "  left  =  " + left + "  top  =  " + top + "  right  =  " + right + "  bottom  =  " + bottom);
            view.layout(left, top, right, bottom);
        } else if (childCount == 4) {
            for (int i = 0; i < 4; i++) {
                View view = getChildAt(i);
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
                int left = (maxChildWidth + defaultLTRBDivier) * (i % columnNum) + getPaddingLeft();
                int top = (maxChildWidth + defaultLTRBDivier) * (i / rowNum) + getPaddingTop();
                int right = left + maxChildWidth - getPaddingRight();
                int bottom = top + maxChildWidth - getPaddingBottom();
                view.layout(left, top, right, bottom);
                KLog.d("childCount  >>>   " + childCount + "   position  =  " + i + "  left  =  " + left + "  top  =  " + top + "  right  =  " + right + "  bottom  =  " + bottom);
            }
        }
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
                    removeViewInLayout(child);
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
            boolean hasCached = checkAndCacheView();
            final int childCount = getChildCount();
            if (hasCached) {
                for (int i = 0; i < childCount; i++) {
                    mAdapter.getView((ImageView) getChildAt(i), CircleImageContainer.this, i);
                }
            } else {
                //没有缓存成功意味着要么没有数据，要么本来的比新来的少，需要补充
                if (childCount == 0) {
                    for (int i = 0; i < mItemCount; i++) {
                        ImageView childImageView = mAdapter.getView(obtainView(), CircleImageContainer.this, i);
                        if (childImageView.getLayoutParams() == null) {
                            addViewInLayout(childImageView, i, generateDefaultLayoutParams(), true);
                        } else {
                            addViewInLayout(childImageView, i, childImageView.getLayoutParams(), true);
                        }
                    }
                } else {
                    int diff = mItemCount - childCount;
                    for (int i = 0; i < childCount; i++) {
                        mAdapter.getView((ImageView) getChildAt(i), CircleImageContainer.this, i);
                    }
                    for (int i = childCount + 1; i < childCount + diff; i++) {
                        ImageView childImageView = mAdapter.getView(obtainView(), CircleImageContainer.this, i);
                        if (childImageView.getLayoutParams() == null) {
                            addViewInLayout(childImageView, i, generateDefaultLayoutParams(), true);
                        } else {
                            addViewInLayout(childImageView, i, childImageView.getLayoutParams(), true);
                        }
                    }
                }
            }
            KLog.d();
            CircleImageContainer.this.requestLayout();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            invalidate();
        }
    }

}
