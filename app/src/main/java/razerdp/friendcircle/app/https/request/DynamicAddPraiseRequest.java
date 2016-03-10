package razerdp.friendcircle.app.https.request;

import org.json.JSONException;
import org.json.JSONObject;
import razerdp.friendcircle.app.FriendCircleApp;
import razerdp.friendcircle.app.data.entity.MomentsInfo;
import razerdp.friendcircle.app.https.base.BaseHttpRequestClient;
import razerdp.friendcircle.app.https.base.BaseResponse;
import razerdp.friendcircle.utils.RequestUrlUtils;

/**
 * Created by 大灯泡 on 2016/3/7.
 * 点赞请求
 */
public class DynamicAddPraiseRequest extends BaseHttpRequestClient {

    public long userid;
    public long dynamicid;
    private MomentsInfo mInfo;

    public DynamicAddPraiseRequest(MomentsInfo info) {
        mInfo = info;
    }

    @Override
    public String setUrl() {
        return new RequestUrlUtils.Builder().setHost(FriendCircleApp.getRootUrl())
                                            .setPath("/dynamic/addpraise/")
                                            .addParam("userid", userid)
                                            .addParam("dyanmicid", dynamicid)
                                            .build();
    }

    @Override
    public void parseResponse(BaseResponse response, JSONObject json, int start, boolean hasMore) throws JSONException {
        if (response.getStatus()==200){
            response.setData(mInfo);
        }

    }
}
