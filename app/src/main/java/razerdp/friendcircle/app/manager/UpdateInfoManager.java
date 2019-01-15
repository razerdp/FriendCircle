package razerdp.friendcircle.app.manager;

import android.app.Activity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import razerdp.basepopup.BasePopupWindow;
import razerdp.friendcircle.app.mvp.model.UpdateInfo;
import razerdp.friendcircle.ui.widget.popup.UpdateInfoPopup;
import razerdp.github.com.lib.interfaces.SimpleCallback;
import razerdp.github.com.lib.utils.ToolUtil;
import razerdp.github.com.lib.utils.VersionUtil;

/**
 * Created by 大灯泡 on 2017/4/7.
 * <p>
 * 在这里填写升级信息
 */

public enum UpdateInfoManager {
    INSTANCE;
    private UpdateInfoPopup popup;
    private boolean hasShow;
    private BasePopupWindow.OnDismissListener mOnDismissListener;


    final String title = "开发日志(2019/1/15)";
    final String content = "  * UI按照新的朋友圈修改\n" +
            "  * 修改下拉刷新的icon逻辑，去掉ViewOffsetHelper\n" +
            "  * 按计划恢复功能";


    public void init(Activity act, BasePopupWindow.OnDismissListener l) {
        popup = new UpdateInfoPopup(act);
        mOnDismissListener = l;
        popup.setOnDismissListener(onDismissListener);
        hasShow = false;
    }

    public void showUpdateInfo() {
        if (!hasShow) {
            popup.setTitle(title);
            popup.setContent(content);
            popup.showPopupWindow();
        }
    }


    private BasePopupWindow.OnDismissListener onDismissListener = new BasePopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            hasShow = true;
            if (mOnDismissListener != null) {
                mOnDismissListener.onDismiss();
            }
        }
    };

    public void checkUpdate(final SimpleCallback<UpdateInfo> cb) {
        BmobQuery<UpdateInfo> updateQuery = new BmobQuery<>("Update");
        updateQuery.findObjects(new FindListener<UpdateInfo>() {
            @Override
            public void done(List<UpdateInfo> list, BmobException e) {
                if (e == null && !ToolUtil.isListEmpty(list)) {
                    UpdateInfo updateInfo = list.get(0);
                    if (updateInfo.getFile() != null) {
                        int curVersionCode = VersionUtil.getAppVersionCode();
                        int targetCode = updateInfo.getBuildCode();
                        if (curVersionCode < targetCode && cb != null) {
                            cb.onCall(updateInfo);
                        }
                    }
                }
            }
        });

    }

}
