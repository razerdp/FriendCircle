package com.razerdp.github.com.common.request;

import android.text.TextUtils;

import com.razerdp.github.com.common.entity.MomentContent;
import com.razerdp.github.com.common.entity.MomentsInfo;
import com.razerdp.github.com.common.entity.PhotoInfo;
import com.razerdp.github.com.common.entity.UserInfo;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import razerdp.github.com.lib.network.base.BaseRequestClient;
import razerdp.github.com.lib.utils.ToolUtil;

/**
 * Created by 大灯泡 on 2016/10/28.
 * <p>
 * 添加动态(暂时不处理文件上传)
 */

public class AddMomentsRequest extends BaseRequestClient<String> {

    private String authId;
    private String hostId;
    private MomentContent momentContent;
    private List<UserInfo> likesUserId;

    public AddMomentsRequest() {
        momentContent = new MomentContent();
        likesUserId = new ArrayList<>();
    }

    public AddMomentsRequest setAuthId(String authId) {
        this.authId = authId;
        return this;
    }

    public AddMomentsRequest setHostId(String hostId) {
        this.hostId = hostId;
        return this;
    }

    @Override
    protected void executeInternal(final int requestType, boolean showDialog) {
        if (checkValided()) {
            MomentsInfo momentsInfo = new MomentsInfo();

            UserInfo author = new UserInfo();
            author.setObjectId(authId);
            momentsInfo.setAuthor(author);

            UserInfo host = new UserInfo();
            host.setObjectId(hostId);
            momentsInfo.setHostinfo(host);

            momentsInfo.setContent(momentContent);

            momentsInfo.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        if (ToolUtil.isListEmpty(likesUserId)) {
                            onResponseSuccess(s, requestType);
                        } else {
                            MomentsInfo resultMoment = new MomentsInfo();
                            resultMoment.setObjectId(s);

                            //关联点赞的
                            BmobRelation relation = new BmobRelation();
                            for (UserInfo user : likesUserId) {
                                relation.add(user);
                            }
                            resultMoment.setLikesBmobRelation(relation);
                            resultMoment.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        onResponseSuccess("添加成功", requestType);
                                    } else {
                                        onResponseError(e, requestType);
                                    }
                                }
                            });
                        }

                    }
                }
            });

        }
    }


    private boolean checkValided() {
        return !(TextUtils.isEmpty(authId) || TextUtils.isEmpty(hostId)) && momentContent.isValided();
    }

    public AddMomentsRequest addText(String text) {
        momentContent.addText(text);
        return this;
    }

    public AddMomentsRequest addPicture(PhotoInfo pic) {
        momentContent.addPicture(pic);
        return this;
    }


    public AddMomentsRequest setPictureBuckets(List<PhotoInfo> datas) {
        if (!ToolUtil.isListEmpty(datas)) {
            for (PhotoInfo data : datas) {
                momentContent.addPicture(data);
            }
        }
        return this;
    }

    public AddMomentsRequest addWebUrl(String webUrl) {
        momentContent.addWebUrl(webUrl);
        return this;
    }

    public AddMomentsRequest addWebTitle(String webTitle) {
        momentContent.addWebTitle(webTitle);
        return this;
    }

    public AddMomentsRequest addWebImage(String webImage) {
        momentContent.addWebImage(webImage);
        return this;
    }

    public AddMomentsRequest addLikes(UserInfo user) {
        likesUserId.add(user);
        return this;
    }
}
