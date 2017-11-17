package razerdp.friendcircle.app.manager;

import android.app.Activity;
import android.graphics.Color;

import razerdp.basepopup.BasePopupWindow;
import razerdp.friendcircle.ui.widget.popup.UpdateInfoPopup;
import razerdp.github.com.lib.utils.StringUtil;

/**
 * Created by 大灯泡 on 2017/4/7.
 * <p>
 * 在这里填写升级信息
 */

public enum UpdateInfoManager {
    INSTANCE;
    private UpdateInfoPopup popup;
    private boolean hasShow;

    final String title = "开发日志(2017/10/27)";
    final String content = "  +   再次修复长文字折叠问题\n\n" +
            "  +   增加test分支（用于测试）\n\n" +
            "  +   修复相册扫描没有进度条的问题\n\n"+
            "  +   增加kotlin分支（用于学习并逐渐转换到kotlin）\n";


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
