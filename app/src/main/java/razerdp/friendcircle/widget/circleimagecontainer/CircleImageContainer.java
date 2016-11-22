package razerdp.friendcircle.widget.circleimagecontainer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.socks.library.KLog;

import org.apmem.tools.layouts.FlowLayout;

import razerdp.friendcircle.widget.circleimagecontainer.adapter.CircleBaseImageAdapter;
import razerdp.friendcircle.widget.circleimagecontainer.adapter.observer.CircleBaseDataObserver;


/**
 * Created by 大灯泡 on 2016/11/9.
 * <p>
 * 适用于朋友圈的九宫格图片显示(用于listview等)
 */

public class CircleImageContainer extends FlowLayout {

    private CircleBaseImageAdapter mAdapter;
    private CircleImageAdapterObserver mAdapterObserver = new CircleImageAdapterObserver();

    private InnerRecyclerHelper recycler;

    private int mItemCount;

    private boolean mDataChanged;

    private int itemMargin;

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
        itemMargin = dp2Px(4f);
        recycler = new InnerRecyclerHelper();
        setOrientation(HORIZONTAL);
        setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int childRestWidth = widthSize - getPaddingLeft() - getPaddingRight();
        updateItemCount();
        multiChildSize = childRestWidth / 3 - itemMargin * 2;
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
                recycler.addCachedView(i, (ImageView) v);
            }
        }

        updateItemCount();
        //清除旧的view
        detachAllViewsFromParent();

        int newChildCount = mItemCount;
        if (newChildCount > 0) {
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
                setupViewAndAddView(i, child, false);
            }
        }
    }

    private void fillFourViews() {
        for (int i = 0; i < 4; i++) {
            final ImageView child = obtainView(i);
            if (i == 2) {
                KLog.d("执行到4，换行。。。");
                setupViewAndAddView(i, child, true);
            } else {
                setupViewAndAddView(i, child, false);
            }
        }
    }


    private void setupViewAndAddView(int position, @NonNull View v, boolean newLine) {
        setItemLayoutParams(v, newLine);
        if (v.isLayoutRequested()) {
            mAdapter.onBindData(position, (ImageView) v);
            attachViewToParent(v, position, v.getLayoutParams());
        } else {
            mAdapter.onBindData(position, (ImageView) v);
            addViewInLayout(v, position, v.getLayoutParams(), true);
        }

    }


    private void setItemLayoutParams(@NonNull View v, boolean needLine) {
        ViewGroup.LayoutParams p = v.getLayoutParams();
        if (p == null || !(p instanceof LayoutParams)) {
            LayoutParams childLP = generateDefaultMultiLayoutParams();
            childLP.setNewLine(needLine);
            v.setLayoutParams(childLP);
        } else {
            ((LayoutParams) p).setNewLine(needLine);
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
            setItemLayoutParams(child, false);
            recycler.addCachedView(position, child);
        }
        return child;
    }

    protected LayoutParams generateDefaultMultiLayoutParams() {
        LayoutParams p = new CircleImageContainer.LayoutParams(multiChildSize, multiChildSize);
        p.rightMargin = itemMargin;
        p.bottomMargin = itemMargin;
        return p;
    }


    private int dp2Px(float dp) {
        return (int) (dp * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    public static class LayoutParams extends FlowLayout.LayoutParams {

        public LayoutParams(int width, int height) {
            this(new ViewGroup.LayoutParams(width, height));
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }


    private class InnerRecyclerHelper {
        private SparseArray<ImageView> mCachedViews;

        InnerRecyclerHelper() {
            mCachedViews = new SparseArray<>();
        }

        ImageView getCachedView(int position) {
            final ImageView imageView = mCachedViews.get(position);
            if (imageView != null) {
                mCachedViews.remove(position);
                return imageView;
            }
            return null;
        }

        void addCachedView(int position, ImageView view) {
            mCachedViews.put(position, view);
        }

        void clearCache() {
            mCachedViews.clear();
        }

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
