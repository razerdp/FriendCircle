package razerdp.friendcircle.app.controller;

import razerdp.friendcircle.app.data.entity.MomentsInfo;
import razerdp.friendcircle.app.https.request.RequestType;

/**
 * Created by 大灯泡 on 2016/3/9.
 * 控制器接口化
 */
public interface BaseDynamicController {
    // 点赞
    void addPraise(long userid, long dynamicid, MomentsInfo info, @RequestType.DynamicRequestType int requesttype);
    // 取消点赞
    void cancelPraise(long userid, long dynamicid, MomentsInfo info, @RequestType.DynamicRequestType int requesttype);

}
