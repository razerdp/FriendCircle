package razerdp.friendcircle.app.https.request;

import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import razerdp.friendcircle.app.FriendCircleApp;
import razerdp.friendcircle.app.https.base.BaseHttpRequestClient;
import razerdp.friendcircle.app.https.base.BaseResponse;
import razerdp.friendcircle.app.mvp.model.entity.CommentInfo;
import razerdp.friendcircle.utils.JSONUtil;
import razerdp.friendcircle.utils.RequestUrlUtils;

/**
 * Created by 大灯泡 on 2016/4/19.
 * 添加评论请求
 */
public class DynamicAddCommentRequest extends BaseHttpRequestClient {
    public long dynamicid;
    public long userid;
    public long replyid;
    public String content;

    public int currentDynamicPos;
    public int getCurrentDynamicPos() {
        return currentDynamicPos;
    }

    public void setCurrentDynamicPos(int currentDynamicPos) {
        this.currentDynamicPos = currentDynamicPos;
    }

    @Override
    public String setUrl() {
        return new RequestUrlUtils.Builder().setHost(FriendCircleApp.getRootUrl())
                                            .setPath("/dynamic/add_comment/")
                                            .build();
    }

    @Override
    public void parseResponse(BaseResponse response, JSONObject json, int start, boolean hasMore) throws JSONException {
        if (response.getStatus() == 200) {
            List<CommentInfo> commentList = JSONUtil.toList(json.optString("data"),
                    new TypeToken<ArrayList<CommentInfo>>() {}.getType());
            response.setData(currentDynamicPos);
            response.setDatas(commentList);
        }
    }

    @Override
    public void postValue(Map<String, String> keyValue) {
        keyValue.put("dynamicid", "" + dynamicid);
        keyValue.put("userid", "" + userid);
        keyValue.put("replyid", "" + replyid);
        keyValue.put("content", content);
    }
}
