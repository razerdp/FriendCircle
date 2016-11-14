package razerdp.friendcircle.widget.circleimagecontainer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.Space;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import razerdp.friendcircle.utils.SimpleObjectPool;
import razerdp.friendcircle.widget.circleimagecontainer.adapter.CircleBaseImageAdapter;
import razerdp.friendcircle.widget.circleimagecontainer.adapter.observer.CircleBaseDataObserver;


/**
 * Created by 大灯泡 on 2016/11/9.
 * <p>
 * 适用于朋友圈的九宫格图片显示(用于listview等)
 */

public class CircleImageContainer extends GridLayout {

    private CircleBaseImageAdapter mAdapter;
    private CircleImageAdapterObserver mAdapterObserver = new CircleImageAdapterObserver();

    private InnerRecyclerHelper recycler;

    private int mItemCount;

    private boolean mDataChanged;

    private int defaultDividerWidth;

    private int multiChildSize;


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
        defaultDividerWidth = dp2Px(8f);
        recycler = new InnerRecyclerHelper();
        setOrientation(HORIZONTAL);
        setColumnCount(3);
        setRowCount(3);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int childRestWidth = widthSize - getPaddingLeft() - getPaddingRight();
        updateItemCount();
        multiChildSize = childRestWidth / 3 - defaultDividerWidth * 2;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        invalidate();
        final int childCount = getChildCount();
        if (mAdapter == null || mItemCount == 0) {
            resetContainer();
            return;
        }
        final int oldChildCount = childCount;
        if (oldChildCount > 0) {
            for (int i = 0; i < oldChildCount; i++) {
                View v = getChildAt(i);
                v.forceLayout();
                if (v instanceof Space) {
                    recycler.addEmptyHolderView((Space) v);
                } else if (v instanceof ImageView) {
                    recycler.addCachedView(i, (ImageView) v);
                }
            }
        }

        updateItemCount();
        //清除旧的view
        detachAllViewsFromParent();
        int newChildCount = mItemCount;
        if (newChildCount > 1) {
            fillView(newChildCount);
        }
        super.onLayout(changed, l, t, r, b);
    }


    private void fillView(int childCount) {
        if (childCount == 4) {
            fillFourViews();
        } else {
            for (int i = 0; i < childCount; i++) {
                final ImageView child = obtainView(i);
                setupViewAndAddView(i, child);
            }
        }
    }

    private void fillFourViews() {
        for (int i = 0; i < 5; i++) {
            if (i == 2) {
                Space space = recycler.getEmptyHolderView();
                if (space == null) {
                    space = new Space(getContext());
                    recycler.addEmptyHolderView(space);
                }
                setupViewAndAddView(i, space);
            } else {
                final ImageView child = obtainView(i);
                setupViewAndAddView(i, child);
            }
        }
    }


    private void setupViewAndAddView(int position, @NonNull View v) {
        int childPosition = position;
        if (mItemCount == 4) {
            childPosition = childPosition > 1 ? childPosition - 1 : childPosition;
        }
        setItemLayoutParams(v);
        if (v.isLayoutRequested()) {
            if (v instanceof ImageView) {
                mAdapter.onBindData(childPosition, (ImageView) v);
            }
            attachViewToParent(v, position, v.getLayoutParams());
        } else {
            if (v instanceof ImageView) {
                mAdapter.onBindData(childPosition, (ImageView) v);
            }
            addViewInLayout(v, position, v.getLayoutParams(), true);
        }

    }


    private void setItemLayoutParams(@NonNull View v) {
        ViewGroup.LayoutParams p = v.getLayoutParams();
        if (p == null || !(p instanceof LayoutParams)) {
            LayoutParams childLP = generateDefaultMultiLayoutParams();
            v.setLayoutParams(childLP);
        }
    }


    public void setAdapter(CircleBaseImageAdapter adapter) {
        if (mAdapter != null && mAdapterObserver != null) {
            mAdapter.unregisterDataSetObserver(mAdapterObserver);
        }

        resetContainer();

        mAdapter = adapter;
        mAdapterObserver = new CircleImageAdapterObserver();
        mAdapter.registerDataSetObserver(mAdapterObserver);
        requestLayout();
    }

    public CircleBaseImageAdapter getAdapter() {
        return mAdapter;
    }


    private void resetContainer() {
        recycler.clearCache();
        removeAllViewsInLayout();
        invalidate();
    }

    private void updateItemCount() {
        mItemCount = mAdapter == null ? 0 : mAdapter.getCount();
    }

    private ImageView obtainView(int position) {
        final ImageView cachedView = recycler.getCachedView(position);
        final ImageView child = mAdapter.onCreateView(cachedView, this, position);
        if (child != cachedView) {
            setItemLayoutParams(child);
            recycler.addCachedView(position, child);
        }
        return child;
    }

    protected LayoutParams generateDefaultMultiLayoutParams() {
        LayoutParams p = new CircleImageContainer.LayoutParams(multiChildSize, multiChildSize);
        p.leftMargin = defaultDividerWidth >> 1;
        p.topMargin = defaultDividerWidth >> 1;
        p.rightMargin = defaultDividerWidth >> 1;
        p.bottomMargin = defaultDividerWidth >> 1;
        return p;
    }


    private int dp2Px(float dp) {
        return (int) (dp * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    public static class LayoutParams extends GridLayout.LayoutParams {

        public LayoutParams(int width, int height) {
            this(new ViewGroup.LayoutParams(width, height));
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }


    private class InnerRecyclerHelper {
        private SparseArray<CachedViewInfo> mCachedViews;
        private SimpleObjectPool<Space> emptyViews;

        InnerRecyclerHelper() {
            mCachedViews = new SparseArray<>();
            emptyViews = new SimpleObjectPool<>(4);
        }

        ImageView getCachedView(int position) {
            CachedViewInfo info = mCachedViews.get(position);
            if (info != null) {
                mCachedViews.remove(position);
                return info.isCached ? info.imageView : null;
            }
            return null;
        }

        void addCachedView(int position, ImageView view) {
            CachedViewInfo info = new CachedViewInfo();
            info.imageView = view;
            info.isCached = true;
            mCachedViews.put(position, info);
        }

        Space getEmptyHolderView() {
            return emptyViews.get();
        }

        void addEmptyHolderView(Space emptyView) {
            emptyViews.put(emptyView);
        }


        void clearCache() {
            mCachedViews.clear();
        }

    }

    private class CachedViewInfo {
        public ImageView imageView;
        public boolean isCached;
    }

    private class CircleImageAdapterObserver extends CircleBaseDataObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            updateItemCount();
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
