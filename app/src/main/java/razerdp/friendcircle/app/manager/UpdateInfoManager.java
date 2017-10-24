package razerdp.friendcircle.app.manager;

import android.app.Activity;
import android.graphics.Color;

import razerdp.basepopup.BasePopupWindow;
import razerdp.friendcircle.ui.widget.popup.UpdateInfoPopup;
import razerdp.github.com.baselibrary.utils.StringUtil;

/**
 * Created by 大灯泡 on 2017/4/7.
 * <p>
 * 在这里填写升级信息
 */

public enum UpdateInfoManager {
    INSTANCE;
    private UpdateInfoPopup popup;
    private boolean hasShow;

    final String title = "开发日志(2017/10/24)";
    final String content = "1 - 嗯，，，现在可以发布动态了\n\n2 - 部分modules进行了位置调整和整合\n\n3 - 增加删除动态功能";


    public void init(Activity act) {
        popup = new UpdateInfoPopup(act);
        popup.setOnDismissListener(onDismissListener);
        hasShow = false;
    }

    public void showUpdateInfo() {
        if (!hasShow) {
            popup.setTitle(title);
            popup.setContent(StringUtil.highLightKeyWord("(详见PhotoSelectAdapter#onUnSelectPhoto)", Color.RED, content));
            popup.showPopupWindow();
        }
    }


    private BasePopupWindow.OnDismissListener onDismissListener = new BasePopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            hasShow = true;
        }
    };

}
