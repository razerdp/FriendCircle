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

    final String title = "开发日志(2017/04/10)";
    final String content = "1 - 修复了Bmob后台查询记录只有100条的截断问题，原来默认是100条，最多查询1000条(太好了，不用重构后台的数据库表了哈哈^_^)"
            + '\n'
            + '\n'
            + "2 - LocalPhotoManager扫描出的系统媒体库似乎有点问题，具体表现在刚打开程序就去选择照片会有较大几率无法查看完整的“所有照片”，返回后回来又完整了。这个稍后修复"
            + '\n'
            + '\n'
            + "3 - 增加了Bmob查询缓存（反正本身就支持），至于能不能用呢？不知道诶，我也没测试哦"
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
            popup.setContent(StringUtil.highLightKeyWord("(太好了，不用重构后台的数据库表了哈哈^_^)", Color.RED, content));
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
