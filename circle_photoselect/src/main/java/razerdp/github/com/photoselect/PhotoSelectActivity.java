package razerdp.github.com.photoselect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import razerdp.github.com.lib.base.BaseFragment;
import razerdp.github.com.lib.manager.localphoto.LocalPhotoManager;
import razerdp.github.com.lib.utils.ToolUtil;
import razerdp.github.com.ui.util.SwitchActivityTransitionUtil;
import razerdp.github.com.ui.base.BaseTitleBarActivity;
import razerdp.github.com.ui.widget.common.TitleBar;
import razerdp.github.com.bus.EventSelectAlbum;
import razerdp.github.com.lib.common.entity.ImageInfo;
import com.razerdp.github.com.common.router.RouterList;
import razerdp.github.com.photoselect.fragment.PhotoAlbumFragement;
import razerdp.github.com.photoselect.fragment.PhotoGridFragement;

/**
 * Created by 大灯泡 on 2017/3/22.
 * <p>
 * 图片选择器
 */

@Route(path = RouterList.PhotoSelectActivity.path)
public class PhotoSelectActivity extends BaseTitleBarActivity {
    private static final String TAG = "PhotoSelectActivity";


    @Autowired(name = RouterList.PhotoSelectActivity.key_maxSelectCount)
    int maxCount;

    private PhotoGridFragement gridFragement;
    private PhotoAlbumFragement albumFragement;

    private BaseFragment currentFragment;


    @Subscribe
    public void onEventMainThread(EventSelectAlbum event) {
        if (event == null || TextUtils.isEmpty(event.getAlbumName())) return;
        if (!TextUtils.equals(getBarTitle(), event.getAlbumName())) {
            gridFragement.changeAlbum(event.getAlbumName());
        }
        setTitleMode(TitleBar.MODE_BOTH);
        setTitle(event.getAlbumName());
        changeFragment(currentFragment, gridFragement, true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoselect);
        init();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onHandleIntent(Intent intent) {

    }

    private void init() {
        initTitle();
        initFrag();
    }

    private void initTitle() {
        setTitle(LocalPhotoManager.INSTANCE.getAllPhotoTitle());
        setTitleMode(TitleBar.MODE_BOTH);
        setTitleLeftText("相册");
        setTitleRightText("取消");
        setTitleRightIcon(0);
    }

    private void initFrag() {
        if (gridFragement == null) {
            KLog.i(TAG, "maxCount = " + maxCount);
            gridFragement = PhotoGridFragement.newInstance(maxCount);
        }
        if (albumFragement == null) {
            albumFragement = new PhotoAlbumFragement();
        }
        changeFragment(currentFragment, gridFragement, false);
    }

    private void changeFragment(BaseFragment from, BaseFragment to, boolean needAnima) {
        if (to == null) return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (needAnima) {
            setUpAnima(transaction, from, to);
        }
        if (!to.isAdded()) {
            transaction.add(R.id.photo_select_content, to);
        } else {
            if (from != null) {
                transaction.hide(from).show(to);
            }
        }
        currentFragment = to;
        transaction.commitAllowingStateLoss();
    }

    private void setUpAnima(FragmentTransaction transaction, BaseFragment from, BaseFragment to) {
        if (to == albumFragement) {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RouterList.PhotoMultiBrowserActivity.requestCode) {
            if (resultCode == RESULT_OK) {
                if (gridFragement != null && data != null && data.hasExtra(RouterList.PhotoMultiBrowserActivity.key_result)) {
                    List<ImageInfo> newDatas = data.getParcelableArrayListExtra(RouterList.PhotoMultiBrowserActivity.key_result);
                    gridFragement.updateSelectList(newDatas);
                }
            }
        }
    }

    @Override
    public void onTitleLeftClick() {
        setTitleMode(TitleBar.MODE_RIGHT);
        setTitle("相册");
        changeFragment(gridFragement, albumFragement, true);
    }

    @Override
    public void onTitleRightClick() {
        finish();
    }

    public void finish(List<ImageInfo> selectedPhoto) {
        if (!ToolUtil.isListEmpty(selectedPhoto)) {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(RouterList.PhotoSelectActivity.key_result, (ArrayList<? extends Parcelable>) selectedPhoto);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        SwitchActivityTransitionUtil.transitionVerticalOnFinish(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalPhotoManager.INSTANCE.writeToLocal();
        EventBus.getDefault().unregister(this);
    }
}
