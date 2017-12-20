package razerdp.friendcircle.app.manager;

import android.support.annotation.Nullable;

import com.razerdp.github.com.common.entity.other.ServiceInfo;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import razerdp.github.com.lib.utils.ToolUtil;

/**
 * Created by 大灯泡 on 2017/12/20.
 * <p>
 * 服务器信息
 */

public enum ServiceInfoManager {
    INSTANCE;

    public void check(final OnCheckServiceInfoListener onCheckServiceInfoListener) {
        BmobQuery<ServiceInfo> serviceInfoBmobQuery = new BmobQuery<>();
        serviceInfoBmobQuery.findObjects(new FindListener<ServiceInfo>() {
            @Override
            public void done(List<ServiceInfo> list, BmobException e) {
                if (e == null && !ToolUtil.isListEmpty(list)) {
                    if (onCheckServiceInfoListener != null) {
                        onCheckServiceInfoListener.onCheckFinish(list.get(0));
                    }
                }
            }
        });
    }

    public interface OnCheckServiceInfoListener {
        void onCheckFinish(@Nullable ServiceInfo serviceInfo);
    }

}
