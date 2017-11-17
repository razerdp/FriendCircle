package com.razerdp.github.com.common.request;

import com.razerdp.github.com.common.entity.UserInfo;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import razerdp.github.com.lib.network.base.BaseRequestClient;
import razerdp.github.com.lib.utils.EncryUtil;
import razerdp.github.com.lib.utils.StringUtil;

/**
 * Created by 大灯泡 on 2016/12/14.
 */

public class RegisterRequest extends BaseRequestClient<UserInfo> {

    private String username;
    private String password;
    private String nick;

    public String getUsername() {
        return username;
    }

    public RegisterRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegisterRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getNick() {
        return nick;
    }

    public RegisterRequest setNick(String nick) {
        this.nick = nick;
        return this;
    }

    @Override
    protected void executeInternal(final int requestType, boolean showDialog) {
        if (!StringUtil.noEmpty(username,password,nick))return;
        final UserInfo userInfo=new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(EncryUtil.MD5(password));
        userInfo.setNick(nick);
        userInfo.setCover("http://d.hiphotos.baidu.com/zhidao/pic/item/bf096b63f6246b601ffeb44be9f81a4c510fa218.jpg");
        userInfo.setAvatar("http://upload.jianshu.io/users/upload_avatars/684042/bd1b2f796e3a.jpg?imageMogr/thumbnail/90x90/quality/100");
        userInfo.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (StringUtil.noEmpty(s)){
                    userInfo.setObjectId(s);
                    onResponseSuccess(userInfo,requestType);
                }else {
                    onResponseError(e,requestType);
                }
            }
        });
    }
}
