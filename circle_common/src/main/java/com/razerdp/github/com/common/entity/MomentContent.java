package com.razerdp.github.com.common.entity;

import android.text.TextUtils;

import com.razerdp.github.com.common.MomentsType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import razerdp.github.com.lib.utils.StringUtil;
import razerdp.github.com.lib.utils.ToolUtil;

/**
 * Created by 大灯泡 on 2016/10/28.
 * <p>
 * 朋友圈内容
 */

public class MomentContent implements Serializable {
    //文字
    private String text;
    //图片
    private List<String> pics;
    //图片优化版
    private List<PhotoInfo> pics2;
    //web
    private String webUrl;
    //webTitle
    private String webTitle;
    //web封面
    private String webImage;

    public MomentContent() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<PhotoInfo> getPics() {
        if (ToolUtil.isListEmpty(pics2)) {
            pics2 = new ArrayList<>();
            if (!ToolUtil.isListEmpty(pics)) {
                for (String pic : pics) {
                    pics2.add(new PhotoInfo().setUrl(pic));
                }
            }
        }
        return pics2;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public String getWebImage() {
        return webImage;
    }

    public void setWebImage(String webImage) {
        this.webImage = webImage;
    }


    /**
     * 获取动态的类型
     *
     * @return
     * @see MomentsType
     */
    public int getMomentType() {
        int type = MomentsType.TEXT_ONLY;
        //图片列表为空，则只能是文字或者web
        if (ToolUtil.isListEmpty(pics)&&ToolUtil.isListEmpty(pics2)) {
            if (StringUtil.noEmpty(webUrl)) {
                type = MomentsType.WEB;
            } else {
                type = MomentsType.TEXT_ONLY;
            }
        } else {
            type = MomentsType.MULTI_IMAGES;
        }
        return type;
    }


    public MomentContent addText(String text) {
        this.text = text;
        return this;
    }

    public MomentContent addPicture(String pic) {
        if (pics == null) {
            pics = new ArrayList<>();
        }
        if (pics.size() < 9) {
            pics.add(pic);
        }
        return this;
    }

    public MomentContent addPicture(PhotoInfo info) {
        if (pics2 == null) {
            pics2 = new ArrayList<>();
        }
        if (pics2.size() < 9) {
            pics2.add(info);
        }
        return this;
    }

    public MomentContent addWebUrl(String webUrl) {
        this.webUrl = webUrl;
        return this;
    }

    public MomentContent addWebTitle(String webTitle) {
        this.webTitle = webTitle;
        return this;
    }

    public MomentContent addWebImage(String webImage) {
        this.webImage = webImage;
        return this;
    }

    public boolean isValided() {
        if (ToolUtil.isListEmpty(pics)) {
            if (TextUtils.isEmpty(text)) {
                if (!StringUtil.noEmpty(webUrl, webTitle)) return false;
            }
        }
        return true;

    }


}
