package razerdp.friendcircle.api.data.controller;

import razerdp.friendcircle.api.network.request.RequestType;

/**
 * Created by 大灯泡 on 2016/3/9.
 * 控制器接口
 */
public interface BaseDynamicController {
    //点赞
    void addPraise(long userid, long dynamicid,Object obj, @RequestType.DynamicRequestType int requesttype);
}
