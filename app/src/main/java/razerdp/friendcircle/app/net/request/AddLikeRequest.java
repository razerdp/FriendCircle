package razerdp.friendcircle.app.net.request;

import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import razerdp.friendcircle.app.manager.LocalHostManager;
import razerdp.friendcircle.app.net.base.BaseRequestClient;
import razerdp.friendcircle.mvp.model.entity.MomentsInfo;
import razerdp.friendcircle.mvp.model.entity.UserInfo;

/**
 * Created by 大灯泡 on 2016/12/6.
 */

public class AddLikeRequest extends BaseRequestClient<Boolean> {

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
        MomentsInfo info = new MomentsInfo();
        info.setObjectId(momentsId);
        UserInfo userInfo = new UserInfo();
        userInfo.setObjectId(userid);
        BmobRelation bmobRelation = new BmobRelation();
        bmobRelation.add(userInfo);
        info.setLikesBmobRelation(bmobRelation);
        info.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                onResponseSuccess(e == null, requestType);
            }
        });

    }
}
