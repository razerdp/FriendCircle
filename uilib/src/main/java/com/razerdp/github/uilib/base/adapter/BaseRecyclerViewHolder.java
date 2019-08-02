package com.razerdp.github.uilib.base.adapter;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 大灯泡 on 2019/8/2.
 */
public abstract class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder {
    private boolean isRecycled = false;

    public BaseRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    final void bindData(T data, int position) {
        isRecycled = false;
        onBindData(data, position);
    }

    public abstract void onBindData(T data, int position);

    public final <V extends View> V findViewById(@IdRes int id) {
        return itemView.findViewById(id);
    }

    void recycle() {
        isRecycled = true;
        onRecycled();
    }

    protected void onRecycled() {

    }
}
