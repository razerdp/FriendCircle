package razerdp.friendcircle.widget.photoscontents;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.socks.library.KLog;

import org.apmem.tools.layouts.FlowLayout;

import razerdp.friendcircle.utils.SimpleObjectPool;
import razerdp.friendcircle.widget.photoscontents.adapter.PhotoContentsBaseAdapter;
import razerdp.friendcircle.widget.photoscontents.adapter.observer.PhotoBaseDataObserver;


/**
 * Created by 大灯泡 on 2016/11/9.
 * <p>
 * 适用于朋友圈的九宫格图片显示(用于listview等)
 */

public class PhotoContents extends FlowLayout {

    private PhotoContentsBaseAdapter mAdapter;
    private PhotoImageAdapterObserver mAdapterObserver = new PhotoImageAdapterObserver();

    private InnerRecyclerHelper recycler;

    private int mItemCount;

    private boolean mDataChanged;

    private int itemMargin;

    private int multiChildSize;


    public PhotoContents(Context context) {
        super(context);
        init(context);
    }

    public PhotoContents(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PhotoContents(Context context, AttributeSet attrs, int defStyleAttr) {
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

        if (mDataChanged) {
            if (mAdapter == null || mItemCount == 0) {
                resetContainer();
                return;
            }
            final int childCount = getChildCount();
            final int oldChildCount = childCount;
            if (oldChildCount > 0) {
                if (oldChildCount == 1) {
                    recycler.addSingleCachedView((ImageView) getChildAt(0));
                } else {
                    for (int i = 0; i < oldChildCount; i++) {
                        View v = getChildAt(i);
                        recycler.addCachedView(i, (ImageView) v);
                    }
                }
            }

            updateItemCount();
            //清除旧的view
            detachAllViewsFromParent();

            int newChildCount = mItemCount;
            if (newChildCount > 0) {
                fillView(newChildCount);
            }
            mDataChanged = false;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void fillView(int childCount) {
        if (childCount == 1) {
            fillSingleView();
        } else if (childCount == 4) {
            fillFourViews();
        } else {
            for (int i = 0; i < childCount; i++) {
                final ImageView child = obtainView(i);
                setupViewAndAddView(i, child, false, false);
            }
        }
    }

    private void fillSingleView() {
        final ImageView singleChild = obtainView(0);
        singleChild.setAdjustViewBounds(true);
        setupViewAndAddView(0, singleChild, false, true);
    }

    private void fillFourViews() {
        for (int i = 0; i < 4; i++) {
            final ImageView child = obtainView(i);
            if (i == 2) {
                setupViewAndAddView(i, child, true, false);
            } else {
                setupViewAndAddView(i, child, false, false);
            }
        }
    }


    private void setupViewAndAddView(int position, @NonNull View v, boolean newLine, boolean isSingle) {
        setItemLayoutParams(v, newLine, isSingle);
        mAdapter.onBindData(position, (ImageView) v);
        if (v.isLayoutRequested()) {
            attachViewToParent(v, position, v.getLayoutParams());
            KLog.d("attachViewToParent");
        } else {
            addViewInLayout(v, position, v.getLayoutParams(), true);
            KLog.d("addViewInLayout");
        }

    }


    private void setItemLayoutParams(@NonNull View v, boolean needLine, boolean isSingle) {
        ViewGroup.LayoutParams p = v.getLayoutParams();
        if (p == null || !(p instanceof LayoutParams)) {
            LayoutParams childLP = generateDefaultMultiLayoutParams(isSingle);
            childLP.setNewLine(needLine);
            v.setLayoutParams(childLP);
        } else {
            ((LayoutParams) p).setNewLine(needLine);
        }
    }


    public void setAdapter(PhotoContentsBaseAdapter adapter) {
        if (mAdapter != null && mAdapterObserver != null) {
            mAdapter.unregisterDataSetObserver(mAdapterObserver);
        }

        resetContainer();

        mAdapter = adapter;
        mAdapterObserver = new PhotoImageAdapterObserver();
        mAdapter.registerDataSetObserver(mAdapterObserver);
        mDataChanged = true;
        requestLayout();
    }

    public PhotoContentsBaseAdapter getAdapter() {
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

        ImageView cachedView;
        ImageView child;
        if (mItemCount == 1) {
            cachedView = recycler.getSingleCachedView();
        } else {
            cachedView = recycler.getCachedView(position);
        }
        child = mAdapter.onCreateView(cachedView, this, position);

        if (child != cachedView) {
            if (mItemCount == 1) {
                recycler.addSingleCachedView(child);
            } else {
                recycler.addCachedView(position, child);
            }
        }
        return child;
    }

    protected LayoutParams generateDefaultMultiLayoutParams(boolean isSingle) {
        LayoutParams p;
        if (isSingle) {
            p = new PhotoContents.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            p = new PhotoContents.LayoutParams(multiChildSize, multiChildSize);
        }
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
        private SimpleObjectPool<ImageView> mSingleCachedViews;

        InnerRecyclerHelper() {
            mCachedViews = new SparseArray<>();
            mSingleCachedViews = new SimpleObjectPool<>(9);
        }

        ImageView getCachedView(int position) {
            final ImageView imageView = mCachedViews.get(position);
            if (imageView != null) {
                mCachedViews.remove(position);
                return imageView;
            }
            return null;
        }

        ImageView getSingleCachedView() {
            return mSingleCachedViews.get();
        }

        void addCachedView(int position, ImageView view) {
            mCachedViews.put(position, view);
        }

        void addSingleCachedView(ImageView imageView) {
            mSingleCachedViews.put(imageView);
        }

        void clearCache() {
            mCachedViews.clear();
            mSingleCachedViews.clearPool();
        }

    }

    private class PhotoImageAdapterObserver extends PhotoBaseDataObserver {

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
