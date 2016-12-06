package razerdp.friendcircle.app.net.request;

import android.view.TouchDelegate;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import razerdp.friendcircle.app.net.base.BaseRequestClient;
import razerdp.friendcircle.mvp.model.entity.MomentsInfo;
import razerdp.friendcircle.mvp.model.entity.UserInfo;

/**
 * Created by 大灯泡 on 2016/12/6.
 */

// FIXME: 2016/12/6 如果不怕嵌套地狱，可以考虑返回当前点赞列表，然而在下不干了。。。
public class AddLikeRequest extends BaseRequestClient<Boolean> {

    private String momentsId;
    private String userid;

    public AddLikeRequest(String momentsId, String userid) {
        this.momentsId = momentsId;
        this.userid = userid;
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
        info.setLikesBmobRelation(new BmobRelation(userInfo));
        info.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                onResponseSuccess(e == null, requestType);
            }
        });

    }
}
