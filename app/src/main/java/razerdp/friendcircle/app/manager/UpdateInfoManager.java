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

    final String title = "开发日志(2017/04/13)";
    final String content = "1 - 修复了LocalPhotoManager扫描媒体库偶尔会出现不全的问题（原来我竟然在回调后没有return，从而导致一直扫描下去orz...这是个大问题额"
            + '\n'
            + '\n'
            + "2 - 修复了选择图片页面在进入预览里取消选择后，返回来再取消选择时数量不准确的问题(详见PhotoSelectAdapter#onUnSelectPhoto)"
            + '\n'
            + '\n'
            + "3 - 其实我偷偷的加了一个长按这个popup文字可以复制的功能（虽然没鸟用）"
            + '\n'
            + '\n'
            + "4 - 接下来会开发发布动态页面，发票圈的日子不远了，期待吗？~期待就打赏或star啊亲-V-";


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
