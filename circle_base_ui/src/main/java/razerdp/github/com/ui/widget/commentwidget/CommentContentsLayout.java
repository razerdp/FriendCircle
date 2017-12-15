package razerdp.github.com.ui.widget.commentwidget;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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
    private static final int DEFAULT_WRAP_COUNT = 10;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Mode.NORMAL, Mode.WRAP})
    public @interface Mode {
        int NORMAL = 0;
        int WRAP = 1;
    }

    @Mode
    private int mode = Mode.NORMAL;
    private int mWrapCount = DEFAULT_WRAP_COUNT;
    private boolean showMore = false;
    private boolean wrapAnimation = true;

    //评论区的view对象池
    private SimpleWeakObjectPool<CommentWidget> COMMENT_TEXT_POOL;

    private int commentLeftAndPaddintRight = UIHelper.dipToPx(8f);
    private int commentTopAndPaddintBottom = UIHelper.dipToPx(3f);

    private OnCommentItemClickListener onCommentItemClickListener;
    private OnCommentItemLongClickListener onCommentItemLongClickListener;
    private OnCommentWidgetItemClickListener onCommentWidgetItemClickListener;

    private TextView show;


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
        COMMENT_TEXT_POOL = new SimpleWeakObjectPool<CommentWidget>();
    }

    private void initShowTextView() {
        if (show == null) {
            show = new TextView(getContext());
            show.setText("更多评论↓");
            show.setTextSize(12);
            show.setTextColor(0xff1a1a1a);
            show.setPadding(32, 32, 32, 32);
        }
        show.setOnClickListener(onShowClickListener);
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

    public void setMode(@Mode int mode) {
        if (this.mode == mode) return;
        this.mode = mode;
        onModeChanged(mode);
    }

    private void onModeChanged(@Mode int mode) {
        switch (mode) {
            case Mode.NORMAL:
                if (show != null) {
                    removeView(show);
                }
                break;
            case Mode.WRAP:
                if (show == null) initShowTextView();
                ViewGroup.LayoutParams params = show.getLayoutParams();
                if (params == null || !(params instanceof LinearLayout.LayoutParams)) {
                    params = generateDefaultLayoutParams();
                }
                ((LayoutParams) params).gravity = Gravity.CENTER_HORIZONTAL;
                if (show.getParent() != null) {
                    addView(show, params);
                }
                break;
        }
    }

    @Override
    public void onChildViewAdded(View parent, View child) {

    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        if (child instanceof CommentWidget) {
            KLog.i(TAG, "捕获到一个评论removed，缓存池+1，当前缓存量  >>>  " + COMMENT_TEXT_POOL.size());
            COMMENT_TEXT_POOL.put((CommentWidget) child);
        }
    }

    public void clearCommentPool() {
        COMMENT_TEXT_POOL.clearPool();
    }

    private OnClickListener onShowClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mode != Mode.WRAP) return;
            showMore = !showMore;

        }
    };


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
            return v instanceof CommentWidget && onCommentItemLongClickListener != null && onCommentItemLongClickListener.onCommentWidgetLongClick((CommentWidget) v);
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


    final class SimpleWeakObjectPool<T> {

        private WeakReference<T>[] objsPool;
        private int size;
        private int curPointer = -1;


        public SimpleWeakObjectPool() {
            this(5);
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

        public int size() {
            return objsPool == null ? 0 : objsPool.length;
        }
    }

    final class InnerExpandableAnimation extends Animation {

        private int targetHeight;
        private boolean isOpen;

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}
