package com.razerdp.github.com.common.entity;

import java.io.Serializable;

/**
 * Created by 大灯泡 on 2018/10/26.
 */
public class PhotoInfo implements Serializable {
    private String url;
    private float width;
    private float height;

    public String getUrl() {
        return url;
    }

    public PhotoInfo setUrl(String url) {
        this.url = url;
        return this;
    }

    public float getWidth() {
        return width;
    }

    public PhotoInfo setWidth(float width) {
        this.width = width;
        return this;
    }

    public float getHeight() {
        return height;
    }

    public PhotoInfo setHeight(float height) {
        this.height = height;
        return this;
    }
}
