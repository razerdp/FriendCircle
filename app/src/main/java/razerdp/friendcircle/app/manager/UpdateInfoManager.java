package razerdp.friendcircle.app.manager;

import android.app.Activity;

import razerdp.basepopup.BasePopupWindow;
import razerdp.friendcircle.ui.widget.popup.UpdateInfoPopup;

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


    final String title = "开发日志(2018/03/23)";
    final String content = "  * 修复评论的问题。";


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

}
