package razerdp.friendcircle.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import razerdp.friendcircle.app.mvp.model.uimodel.PhotoBrowseInfo;
import razerdp.friendcircle.ui.gallery.PhotoBrowseActivity;
import razerdp.friendcircle.ui.publish.PublishActivity;
import razerdp.github.com.baseuilib.utils.SwitchActivityTransitionUtil;

/**
 * Created by 大灯泡 on 2017/3/1.
 * <p>
 * activity发射器~
 */

public class ActivityLauncher {

    /**
     * 发射到发布朋友圈页面
     *
     * @param act
     * @param mode
     * @param requestCode
     */
    public static void startToPublishActivityWithResult(Activity act, @PublishActivity.Mode int mode, int requestCode) {
        Intent intent = new Intent(act, PublishActivity.class);
        intent.putExtra(PublishActivity.TAG_MODE, mode);
        act.startActivityForResult(intent, requestCode);
        SwitchActivityTransitionUtil.transitionVerticalIn(act);
    }


    public static void startToPhotoBrosweActivity(Activity act, @NonNull PhotoBrowseInfo info) {
        if (info == null) return;
        PhotoBrowseActivity.startToPhotoBrowseActivity(act, info);
    }
}
