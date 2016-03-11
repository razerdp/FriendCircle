package razerdp.friendcircle.app.data.controllerentity;

import razerdp.friendcircle.app.data.entity.MomentsInfo;

/**
 * Created by 大灯泡 on 2016/3/10.
 * 控制器实体类
 */
public class DynamicControllerEntity<T> {
    private MomentsInfo mMomentsInfo;
    private T data;

    public MomentsInfo getMomentsInfo() {
        return mMomentsInfo;
    }

    public void setMomentsInfo(MomentsInfo momentsInfo) {
        mMomentsInfo = momentsInfo;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
