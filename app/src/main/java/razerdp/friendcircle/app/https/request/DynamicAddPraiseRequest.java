package razerdp.friendcircle.app.https.request;

import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import razerdp.friendcircle.app.FriendCircleApp;
import razerdp.friendcircle.app.data.controllerentity.DynamicControllerEntity;
import razerdp.friendcircle.app.data.entity.MomentsInfo;
import razerdp.friendcircle.app.data.entity.UserInfo;
import razerdp.friendcircle.app.https.base.BaseHttpRequestClient;
import razerdp.friendcircle.app.https.base.BaseResponse;
import razerdp.friendcircle.utils.JSONUtil;
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

    public MomentsInfo getInfo() {
        return mInfo;
    }

    public void setInfo(MomentsInfo info) {
        mInfo = info;
    }

    @Override
    public String setUrl() {
        return new RequestUrlUtils.Builder().setHost(FriendCircleApp.getRootUrl())
                                            .setPath("/dynamic/addpraise/")
                                            .addParam("userid", userid)
                                            .addParam("dynamicid", dynamicid)
                                            .build();
    }

    @Override
    public void parseResponse(BaseResponse response, JSONObject json, int start, boolean hasMore) throws JSONException {
        if (response.getStatus()==200){
            DynamicControllerEntity<List<UserInfo>> entity=new DynamicControllerEntity();
            entity.setMomentsInfo(mInfo);
            List<UserInfo> praiseList= JSONUtil.toList(json.optString("data"),new TypeToken<ArrayList<UserInfo>>(){}
                    .getType
                    ());
            entity.setData(praiseList);
            response.setData(entity);
        }

    }
}
