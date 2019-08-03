package com.razerdp.github.uilib.base.adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;

import com.razerdp.github.lib.utils.ToolUtil;
import com.razerdp.github.uilib.base.adapter.interfaces.OnItemClickListener;
import com.razerdp.github.uilib.base.adapter.interfaces.OnItemLongClickListener;
import com.razerdp.github.uilib.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 大灯泡 on 2019/8/2.
 * <p>
 * baseadapter，请注意，adapter内部的数据跟外部不是同一个对象（预防外部修改导致内部修改）
 */
@SuppressWarnings("all")
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder<T>> {
    protected final String TAG = getClass().getSimpleName();

    protected Context mContext;
    protected List<T> mDatas;

    private OnItemClickListener<T> mOnItemClickListener;
    private OnItemLongClickListener<T> mOnItemLongClickListener;

    public BaseRecyclerViewAdapter(Context context) {
        this(context, new ArrayList<T>());
    }

    public BaseRecyclerViewAdapter(Context context, List<T> datas) {
        mContext = context;
        mDatas = new ArrayList<>();
        if (!ToolUtil.isEmpty(datas)) {
            mDatas.addAll(datas);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        GestureDetectorCompat mGestureDetectorCompat = new GestureDetectorCompat(recyclerView.getContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        if (mOnItemClickListener == null) return false;
                        View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if (child == null) return false;
                        try {
                            RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(child);
                            int position = holder.getLayoutPosition();
                            mOnItemClickListener.onItemClick(child, position, mDatas.get(position));
                            return true;
                        } catch (Exception error) {
                            error.printStackTrace();
                        }
                        return false;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        if (mOnItemLongClickListener == null) return;
                        View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if (child == null) return;
                        try {
                            RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(child);
                            int position = holder.getLayoutPosition();
                            mOnItemLongClickListener.onItemLongClick(child, position, mDatas.get(position));
                        } catch (Exception error) {
                            error.printStackTrace();
                        }
                        return;
                    }
                });
        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (mGestureDetectorCompat.onTouchEvent(e)) {
                    return true;
                }
                return super.onInterceptTouchEvent(rv, e);
            }
        });
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder holder = null;
        int layout = holderLayout(viewType);
        if (layout != 0) {
            holder = createViewHolder(parent,
                    ViewUtil.inflate(layout, parent, false),
                    viewType);
            return holder;
        }
        return createEmptyHolder();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder<T> holder, int position) {
        T data = mDatas.get(position);
        holder.bindData(data, position);
        onBindData(holder, data, position);
    }

    protected void onBindData(@NonNull BaseRecyclerViewHolder<T> holder, T data, int position) {

    }


    @Override
    public int getItemViewType(int position) {
        return onGetViewType(mDatas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return ToolUtil.isEmpty(mDatas) ? 0 : mDatas.size();
    }


    public abstract int onGetViewType(T data, int position);

    @LayoutRes
    public abstract int holderLayout(int viewType);

    @NonNull
    public abstract BaseRecyclerViewHolder createViewHolder(@NonNull ViewGroup parent, @NonNull View itemView, int viewType);


    public BaseRecyclerViewAdapter<T> setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        return this;
    }

    public BaseRecyclerViewAdapter<T> setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
        return this;
    }

    public void updateData(List<T> datas) {
        updateData(-1, datas);
    }

    public void updateData(int position, List<T> datas) {
        if (ToolUtil.isEmpty(datas)) return;
        if (mDatas != datas) {
            if (!ToolUtil.isEmpty(datas)) {
                if (position == -1) {
                    mDatas.clear();
                    mDatas.addAll(datas);
                } else {
                    mDatas.addAll(position, datas);
                }
            } else {
                //清空
                mDatas.clear();
            }
        }
        notifyDataSetChanged();
    }

    public void addMore(List<T> datas) {
        if (!ToolUtil.isEmpty(datas)) {
            int index = mDatas.size();
            mDatas.addAll(datas);
            notifyItemRangeChanged(index, datas.size());
        }
    }

    public void remove(T data) {
        if (data != null) {
            int pos = mDatas.indexOf(data);
            if (pos < 0) return;
            if (mDatas.remove(data)) {
                notifyItemRemoved(pos);
            }
        }
    }

    public void remove(int position) {
        if (!ToolUtil.indexInCollection(mDatas, position)) return;
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public Context getContext() {
        return mContext;
    }

    protected BaseRecyclerViewHolder createEmptyHolder() {
        return new EmptyHolder(new Space(mContext));
    }

    private class EmptyHolder extends BaseRecyclerViewHolder {

        EmptyHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onBindData(Object data, int position) {

        }
    }
}
