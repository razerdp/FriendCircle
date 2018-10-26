package razerdp.friendcircle.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.razerdp.github.com.common.entity.PhotoBrowseInfo;
import com.razerdp.github.com.common.entity.other.ServiceInfo;
import com.razerdp.github.com.common.router.RouterList;

import java.util.ArrayList;
import java.util.List;

import razerdp.friendcircle.activity.circledemo.FriendCircleDemoFragmentActivity;
import razerdp.friendcircle.activity.gallery.PhotoBrowseActivity;
import razerdp.friendcircle.activity.other.ServiceInfoActivity;
import razerdp.github.com.lib.common.entity.ImageInfo;
import razerdp.github.com.photoselect.PhotoSelectActivity;
import razerdp.github.com.publish.PublishActivity;
import razerdp.github.com.ui.util.SwitchActivityTransitionUtil;

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
    public static void startToPublishActivityWithResult(Activity act, @RouterList.PublishActivity int mode, @Nullable List<ImageInfo> selectedPhotos, int requestCode) {
        Intent intent = new Intent(act, PublishActivity.class);
        intent.putExtra(RouterList.PublishActivity.key_mode, mode);
        if (selectedPhotos != null) {
            intent.putParcelableArrayListExtra(RouterList.PublishActivity.key_photoList, (ArrayList<? extends Parcelable>) selectedPhotos);
        }
        act.startActivityForResult(intent, requestCode);
        SwitchActivityTransitionUtil.transitionVerticalIn(act);
    }


    public static void startToPhotoBrosweActivity(Activity act, @NonNull PhotoBrowseInfo info) {
        if (info == null) return;
        PhotoBrowseActivity.startToPhotoBrowseActivity(act, info);
    }

    public static void startToFriendCircleFragmentDemoActivity(Context context) {
        context.startActivity(new Intent(context, FriendCircleDemoFragmentActivity.class));
    }

    /**
     * 发射到选择图片页面
     *
     * @param act
     */
    public static void startToPhotoSelectActivity(Activity act, int requestCode) {
        Intent intent = new Intent(act, PhotoSelectActivity.class);
        act.startActivityForResult(intent, requestCode);
        SwitchActivityTransitionUtil.transitionVerticalIn(act);
    }

    /**
     * 发射到服务器消息页面
     *
     * @param act
     */
    public static void startToServiceInfoActivity(Activity act, ServiceInfo info) {
        Intent intent = new Intent(act, ServiceInfoActivity.class);
        intent.putExtra("info", info);
        act.startActivity(intent);
        SwitchActivityTransitionUtil.transitionVerticalIn(act);
    }

}
