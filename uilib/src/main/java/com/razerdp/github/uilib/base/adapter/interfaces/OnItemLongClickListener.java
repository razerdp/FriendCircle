package com.razerdp.github.uilib.base.adapter.interfaces;

import android.view.View;

/**
 * Created by 大灯泡 on 2019/8/3.
 */
public interface OnItemLongClickListener<T> {

    void onItemLongClick(View v, int position, T data);
}
