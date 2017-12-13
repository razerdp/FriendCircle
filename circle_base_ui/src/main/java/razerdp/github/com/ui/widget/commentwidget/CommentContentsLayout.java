package razerdp.github.com.ui.widget.commentwidget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.List;

import razerdp.github.com.baseuilib.R;
import razerdp.github.com.ui.util.UIHelper;

/**
 * Created by 大灯泡 on 2017/12/14.
 * <p>
 * 评论展示layout，容纳commentwidget的layout
 */

public class CommentContentsLayout extends LinearLayout implements ViewGroup.OnHierarchyChangeListener {
    private static final String TAG = "CommentContentsLayout";

    //评论区的view对象池
    private SimpleWeakObjectPool<CommentWidget> COMMENT_TEXT_POOL;

    private int commentLeftAndPaddintRight = UIHelper.dipToPx(8f);
    private int commentTopAndPaddintBottom = UIHelper.dipToPx(3f);

    private OnCommentItemClickListener onCommentItemClickListener;
    private OnCommentItemLongClickListener onCommentItemLongClickListener;
    private OnCommentWidgetItemClickListener onCommentWidgetItemClickListener;

    public CommentContentsLayout(Context context) {
        super(context);
        initView();
    }

    public CommentContentsLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CommentContentsLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
        COMMENT_TEXT_POOL = new SimpleWeakObjectPool<CommentWidget>(35);
        setOnHierarchyChangeListener(this);
    }

    /**
     * 添加评论
     *
     * @param datas
     * @return ture=显示评论，false=不显示评论
     */
    public boolean addComments(List<? extends IComment> datas) {
        if (isListEmpty(datas)) {
            return false;
        }
        if (getVisibility() != VISIBLE) setVisibility(VISIBLE);
        final int childCount = getChildCount();
        if (childCount < datas.size()) {
            //当前的view少于list的长度，则补充相差的view
            int subCount = datas.size() - childCount;
            for (int i = 0; i < subCount; i++) {
                CommentWidget commentWidget = COMMENT_TEXT_POOL.get();
                if (commentWidget == null) {
                    commentWidget = new CommentWidget(getContext());
                    commentWidget.setPadding(commentLeftAndPaddintRight, commentTopAndPaddintBottom, commentLeftAndPaddintRight, commentTopAndPaddintBottom);
                    commentWidget.setLineSpacing(4, 1);
                }
                commentWidget.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.common_selector));
                commentWidget.setOnClickListener(onCommentWidgetClickListener);
                commentWidget.setOnLongClickListener(onCommentLongClickListener);
                commentWidget.setOnCommentUserClickListener(mOnCommentUserClickListener);
                ViewGroup.LayoutParams params = commentWidget.getLayoutParams();
                if (params == null) {
                    params = generateDefaultLayoutParams();
                }
                addViewInLayout(commentWidget, i, params, true);
            }
        } else if (childCount > datas.size()) {
            //当前的view的数目比list的长度大，则减去对应的view
            removeViewsInLayout(datas.size(), childCount - datas.size());
        }
        //绑定数据
        for (int n = 0; n < datas.size(); n++) {
            CommentWidget commentWidget = (CommentWidget) getChildAt(n);
            if (commentWidget != null) commentWidget.setCommentText(datas.get(n));
        }
        requestLayout();
        return true;
    }

    boolean isListEmpty(List<?> datas) {
        return datas == null || datas.size() <= 0;
    }

    @Override
    public void onChildViewAdded(View parent, View child) {

    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        if (child instanceof CommentWidget) COMMENT_TEXT_POOL.put((CommentWidget) child);
    }

    public void clearCommentPool() {
        COMMENT_TEXT_POOL.clearPool();
    }

    final class SimpleWeakObjectPool<T> {

        private WeakReference<T>[] objsPool;
        private int size;
        private int curPointer = -1;


        public SimpleWeakObjectPool() {
            this(8);
        }

        public SimpleWeakObjectPool(int size) {
            this.size = size;
            objsPool = (WeakReference<T>[]) Array.newInstance(WeakReference.class, size);
        }

        public synchronized T get() {
            if (curPointer == -1 || curPointer > objsPool.length) return null;
            T obj = objsPool[curPointer].get();
            objsPool[curPointer] = null;
            curPointer--;
            return obj;
        }

        public synchronized boolean put(T t) {
            if (curPointer == -1 || curPointer < objsPool.length - 1) {
                curPointer++;
                objsPool[curPointer] = new WeakReference<T>(t);
                return true;
            }
            return false;
        }

        public void clearPool() {
            for (int i = 0; i < objsPool.length; i++) {
                objsPool[i].clear();
                objsPool[i] = null;
            }
            curPointer = -1;
        }
    }


    private OnClickListener onCommentWidgetClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!(v instanceof CommentWidget)) return;
            if (onCommentItemClickListener != null) {
                onCommentItemClickListener.onCommentWidgetClick((CommentWidget) v);
            }
        }
    };

    private View.OnLongClickListener onCommentLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            return v instanceof CommentWidget && onCommentLongClickListener != null && onCommentLongClickListener.onLongClick(v);
        }
    };

    private OnCommentUserClickListener mOnCommentUserClickListener = new OnCommentUserClickListener() {
        @Override
        public void onCommentClicked(@NonNull IComment comment) {
            if (onCommentWidgetItemClickListener != null) {
                onCommentWidgetItemClickListener.onCommentItemClicked(comment);
            }
        }
    };

    public OnCommentItemClickListener getOnCommentItemClickListener() {
        return onCommentItemClickListener;
    }

    public void setOnCommentItemClickListener(OnCommentItemClickListener onCommentItemClickListener) {
        this.onCommentItemClickListener = onCommentItemClickListener;
    }

    public OnCommentItemLongClickListener getOnCommentItemLongClickListener() {
        return onCommentItemLongClickListener;
    }

    public void setOnCommentItemLongClickListener(OnCommentItemLongClickListener onCommentItemLongClickListener) {
        this.onCommentItemLongClickListener = onCommentItemLongClickListener;
    }

    public OnCommentWidgetItemClickListener getOnCommentWidgetItemClickListener() {
        return onCommentWidgetItemClickListener;
    }

    public void setOnCommentWidgetItemClickListener(OnCommentWidgetItemClickListener onCommentWidgetItemClickListener) {
        this.onCommentWidgetItemClickListener = onCommentWidgetItemClickListener;
    }

    public interface OnCommentItemClickListener {
        void onCommentWidgetClick(@NonNull CommentWidget widget);
    }

    public interface OnCommentItemLongClickListener {
        boolean onCommentWidgetLongClick(@NonNull CommentWidget widget);
    }

    public interface OnCommentWidgetItemClickListener {
        void onCommentItemClicked(@NonNull IComment comment);
    }
}
