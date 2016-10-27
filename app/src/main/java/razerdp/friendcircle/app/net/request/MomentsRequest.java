package razerdp.friendcircle.app.net.request;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import razerdp.friendcircle.app.mvp.model.entity.MomentsInfo;
import razerdp.friendcircle.app.mvp.model.entity.UserInfo;
import razerdp.friendcircle.app.net.OnResponseListener;
import razerdp.friendcircle.app.net.base.BaseRequestClient;
import razerdp.friendcircle.utils.ToolUtil;

/**
 * Created by 大灯泡 on 2016/10/27.
 * <p>
 * 朋友圈时间线请求
 */

public class MomentsRequest extends BaseRequestClient<List<MomentsInfo>> {

    private int count = 10;

    public MomentsRequest() {
    }

    public MomentsRequest setCount(int count) {
        this.count = count;
        return this;
    }


    @Override
    protected void executeInternal(final int requestType, boolean showDialog) {
        BmobQuery<MomentsInfo> query = new BmobQuery<>();
        query.include("author,hostinfo");
        query.addWhereRelatedTo("likes",new BmobPointer());
        query.findObjects(new FindListener<MomentsInfo>() {
            @Override
            public void done(List<MomentsInfo> list, BmobException e) {
                if (e == null) {
                    onResponseSuccess(list, requestType);
                } else {
                    onResponseError(e, requestType);
                }
            }
        });

    }

}
