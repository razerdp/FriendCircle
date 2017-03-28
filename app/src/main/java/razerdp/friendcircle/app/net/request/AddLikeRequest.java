package razerdp.friendcircle.app.net.request;

import com.socks.library.KLog;

import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import razerdp.friendcircle.app.manager.LocalHostManager;
import razerdp.friendcircle.app.mvp.model.entity.LikesInfo;
import razerdp.friendcircle.app.mvp.model.entity.MomentsInfo;
import razerdp.friendcircle.app.mvp.model.entity.UserInfo;
import razerdp.github.com.net.base.BaseRequestClient;

/**
 * Created by 大灯泡 on 2016/12/6.
 */

public class AddLikeRequest extends BaseRequestClient<String> {

    private String momentsId;
    private String userid;

    public AddLikeRequest(String momentsId) {
        this.momentsId = momentsId;
        this.userid = LocalHostManager.INSTANCE.getUserid();
    }

    public String getMomentsId() {
        return momentsId;
    }

    public AddLikeRequest setMomentsId(String momentsId) {
        this.momentsId = momentsId;
        return this;
    }

    public String getUserid() {
        return userid;
    }

    public AddLikeRequest setUserid(String userid) {
        this.userid = userid;
        return this;
    }

    @Override
    protected void executeInternal(final int requestType, boolean showDialog) {
        LikesInfo info = new LikesInfo();
        info.setMomentsid(momentsId);
        info.setUserid(userid);
        info.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                KLog.i(s);
                onResponseSuccess(s, requestType);
            }
        });

    }
}
