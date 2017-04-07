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

    final String title = "开发日志(2017/04/07)";
    final String content = "1 - 修复了图片选择后进入预览图片页面第0张无法确定其选择状态的问题"
            + '\n'
            + '\n'
            + "2 - 目前发现的问题：因为Bmob的查询问题，导致后台数据库存在的评论在查询的时候无法查询，具体定位在MomentsRequest#queryCommentAndLikes()里面的commentQuery.addWhereContainedIn方法，本意是查询拥有该momentid的所有评论，然而似乎没法找全，这个问题我会继续探究（也许可能再一次重构bmob的后台的表），敬请留意github上的更新。";

    public void init(Activity act) {
        popup = new UpdateInfoPopup(act);
        popup.setOnDismissListener(onDismissListener);
        hasShow = false;
    }

    public void showUpdateInfo() {
        if (!hasShow) {
            popup.setTitle(title);
            popup.setContent(StringUtil.highLightKeyWord("MomentsRequest#queryCommentAndLikes()", Color.RED, content));
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
