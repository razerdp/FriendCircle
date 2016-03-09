package razerdp.friendcircle.api.network.request;

import org.json.JSONException;
import org.json.JSONObject;
import razerdp.friendcircle.api.FriendCircleApp;
import razerdp.friendcircle.api.network.base.BaseHttpRequestClient;
import razerdp.friendcircle.api.network.base.BaseResponse;
import razerdp.friendcircle.utils.RequestUrlUtils;

/**
 * Created by 大灯泡 on 2016/3/7.
 * 点赞
 */
public class DynamicAddPraiseRequest extends BaseHttpRequestClient {

    public long userid;
    public long dynamicid;

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

    }
}
