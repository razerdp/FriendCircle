package com.razerdp.github.uilib.base.adapter;

/**
 * Created by 大灯泡 on 2019/8/3.
 */
public class SimpleMultiType implements MultiType {

    int type;

    public SimpleMultiType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
