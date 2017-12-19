package razerdp.github.com.ui.widget.commentwidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
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
import razerdp.github.com.ui.util.AnimUtils;
import razerdp.github.com.ui.util.UIHelper;

/**
 * Created by 大灯泡 on 2017/12/14.
 * <p>
 * 评论展示layout，容纳commentwidget的layout
 * 为了防止过多层layout（过度绘制），所以展开部分采取直接绘制
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
    private boolean wrapAnimation = true;
    private InnerExpandableAnimation mExpandableAnimation;

    //评论区的view对象池
    private SimpleWeakObjectPool<CommentWidget> COMMENT_TEXT_POOL;

    private int showMoreTextHeight = UIHelper.dipToPx(24f);
    private int commentLeftAndPaddintRight = UIHelper.dipToPx(8f);
    private int commentTopAndPaddintBottom = UIHelper.dipToPx(3f);

    private OnCommentItemClickListener onCommentItemClickListener;
    private OnCommentItemLongClickListener onCommentItemLongClickListener;
    private OnCommentWidgetItemClickListener onCommentWidgetItemClickListener;

    private Paint mTextPaint;
    private Rect mDrawRect = new Rect();
    private RectF mMoreButtonTouchRect = new RectF();


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
        mExpandableAnimation = new InnerExpandableAnimation();
        COMMENT_TEXT_POOL = new SimpleWeakObjectPool<CommentWidget>();
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
                setWillNotDraw(true);
                requestLayout();
                break;
            case Mode.WRAP:
                initPaint();
                setWillNotDraw(false);
                mExpandableAnimation.setOpenState(false, true);
                break;
        }
    }

    private void initPaint() {
        if (mTextPaint == null) {
            mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.setStyle(Paint.Style.FILL);
            mTextPaint.setColor(Color.parseColor("#4AA0E2"));
            mTextPaint.setTextSize(UIHelper.dipToPx(12));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int targetHeight = drawWrapButton() ? getMeasuredHeight() + showMoreTextHeight : getMeasuredHeight();
        setMeasuredDimension(widthMeasureSpec, targetHeight);
        Log.i(TAG, "onMeasure: taegetHeight  >>  " + targetHeight);
        if (drawWrapButton() && mExpandableAnimation.mDelayApplyOpenStateInfo != null) {
            mExpandableAnimation.setOriginalHeight(targetHeight);
            mExpandableAnimation.applyDelaySetOpenState();
        }
    }

    private boolean drawWrapButton() {
        return mode == Mode.WRAP && getChildCount() > mWrapCount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawWrapButton()) {
            getDrawingRect(mDrawRect);
            if (mDrawRect.isEmpty()) return;
            float textWidth = mTextPaint.measureText(mExpandableAnimation.isOpen ? "收起评论" : "展开评论");
            canvas.drawText(mExpandableAnimation.isOpen ? "收起评论" : "展开评论", (mDrawRect.width() - textWidth) / 2, mDrawRect.bottom - getPaddingBottom() - UIHelper.dipToPx(8), mTextPaint);
            mMoreButtonTouchRect.set(mDrawRect.left,
                    mDrawRect.bottom - getPaddingBottom() - UIHelper.dipToPx(16),
                    mDrawRect.right,
                    mDrawRect.bottom);
        }
    }

    float touchX = 0;
    float touchY = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX = ev.getX();
                touchY = ev.getY();
                if (mMoreButtonTouchRect.contains(touchX, touchY)) return true;
                break;
            case MotionEvent.ACTION_UP:
                if (mMoreButtonTouchRect.contains(touchX, touchY)) {
                    onShowMoreChanged();
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void onShowMoreChanged() {
        if (wrapAnimation) {
            mExpandableAnimation.toggleOpenState(false);
        } else {
            mExpandableAnimation.toggleOpenState(true);
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

        private int originalHeight;
        private int targetHeight;
        private boolean isOpen;
        private boolean isInAnimating;
        private DelayApplyOpenStateInfo mDelayApplyOpenStateInfo;

        InnerExpandableAnimation() {
            isOpen = false;
            setDuration(500);
            setInterpolator(new DecelerateInterpolator());
            setAnimationListener(new AnimUtils.SimpleAnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isInAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isInAnimating = false;
                }
            });
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            applyAnimation(interpolatedTime);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }

        public void setOriginalHeight(int height) {
            if (originalHeight == 0 && height > 0) originalHeight = height;
        }

        boolean toggleOpenState(boolean immediately) {
            setOpenState(!isOpen, immediately);
            return isOpen;
        }

        private void setOpenState(boolean wantOpen, boolean immediately) {
            if (originalHeight == 0) {
                createDelaySetOpenState(wantOpen, immediately);
                return;
            }
            this.isOpen = wantOpen;
            final int childCount = getChildCount();
            if (wantOpen) {
                targetHeight = originalHeight;
            } else {
                if (childCount > mWrapCount) {
                    targetHeight = 0;
                    for (int i = 0; i < mWrapCount; i++) {
                        targetHeight += getChildAt(i).getMeasuredHeight();
                    }
                    targetHeight += getPaddingTop() + getPaddingBottom() + showMoreTextHeight;
                }
            }
            if (immediately) {
                applyAnimation(-1);
            } else {
                if (!isInAnimating) {
                    startAnimation(this);
                }
            }
        }

        private void createDelaySetOpenState(boolean wantOpen, boolean immediately) {
            mDelayApplyOpenStateInfo = new DelayApplyOpenStateInfo(wantOpen, immediately);
        }

        private void applyDelaySetOpenState() {
            if (mDelayApplyOpenStateInfo != null) {
                setOpenState(mDelayApplyOpenStateInfo.wantOpen, mDelayApplyOpenStateInfo.immediately);
                mDelayApplyOpenStateInfo = null;
            }
        }

        /**
         * -1表示立刻执行
         *
         * @param timeSet
         */
        void applyAnimation(float timeSet) {
            LayoutParams p = (LayoutParams) getLayoutParams();
            float subHeight = targetHeight - originalHeight;
            if (timeSet > 0 && timeSet < 1) {
                p.height = originalHeight + (int) (subHeight * timeSet);
                Log.d(TAG, "动画中:  time  >>  " + timeSet + "  subHeight  >>  " + subHeight + "   targetHeight = " + targetHeight + "   originalHeight = " + originalHeight);
            } else if (timeSet <= -1) {
                p.height = targetHeight;
            }
            Log.i(TAG, "applyAnimation: >> targetHeight = " + targetHeight + "   originalHeight = " + originalHeight +
                    "  firstView  >>  " + ((getChildAt(0) instanceof TextView) ? ((TextView) getChildAt(0)).getText().toString() : "not textView"));
            requestLayout();
        }

        private class DelayApplyOpenStateInfo {
            final boolean wantOpen;
            final boolean immediately;

            public DelayApplyOpenStateInfo(boolean wantOpen, boolean immediately) {
                this.wantOpen = wantOpen;
                this.immediately = immediately;
            }
        }


    }
}
