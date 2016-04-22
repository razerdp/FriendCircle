package razerdp.friendcircle.app.https.request;

import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import razerdp.friendcircle.app.FriendCircleApp;
import razerdp.friendcircle.app.https.base.BaseHttpRequestClient;
import razerdp.friendcircle.app.https.base.BaseResponse;
import razerdp.friendcircle.app.mvp.model.entity.CommentInfo;
import razerdp.friendcircle.utils.JSONUtil;
import razerdp.friendcircle.utils.RequestUrlUtils;

/**
 * Created by 大灯泡 on 2016/4/22.
 * 删除评论请求
 */
public class DynamicDeleteCommentRequest extends BaseHttpRequestClient{
    public long dynamicid;
    public long userid;
    public long commentid;

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
                                            .setPath("/dynamic/del_comment/")
                                            .addParam("dynamicid",dynamicid)
                                            .addParam("userid",userid)
                                            .addParam("commentid",commentid)
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
}
