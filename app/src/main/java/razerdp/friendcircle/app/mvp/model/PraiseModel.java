package razerdp.friendcircle.app.mvp.model;

import razerdp.friendcircle.app.https.request.RequestType;
import razerdp.friendcircle.app.mvp.model.entity.MomentsInfo;

/**
 * Created by 大灯泡 on 2016/3/17.
 * model - 点赞接口
 */
public interface PraiseModel {

    //点赞
    void addPraise(int currentDynamicPos, long userid, long dynamicid);

    // 取消点赞
    void cancelPraise(int currentDynamicPos, long userid, long dynamicid);
}
