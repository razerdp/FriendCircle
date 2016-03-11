package razerdp.friendcircle.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import java.util.Collection;
import java.util.List;
import razerdp.friendcircle.R;
import razerdp.friendcircle.app.config.CommonValue;
import razerdp.friendcircle.app.config.LocalHostInfo;
import razerdp.friendcircle.app.controller.DynamicController;
import razerdp.friendcircle.app.data.controllerentity.DynamicControllerEntity;
import razerdp.friendcircle.app.data.entity.MomentsInfo;
import razerdp.friendcircle.app.data.entity.UserInfo;
import razerdp.friendcircle.app.https.base.BaseResponse;
import razerdp.friendcircle.app.https.request.FriendCircleRequest;
import razerdp.friendcircle.app.https.request.RequestType;
import razerdp.friendcircle.ui.activity.base.FriendCircleBaseActivity;
import razerdp.friendcircle.utils.FriendCircleAdapterUtil;

/**
 * Created by 大灯泡 on 2016/2/25.
 * 朋友圈demo窗口
 */
public class FriendCircleDemoActivity extends FriendCircleBaseActivity implements DynamicController.CallBack {
    private FriendCircleRequest mCircleRequest;

    private DynamicController mDynamicController;

    // 方案二，预留
 /*   @Override
    protected void onEventMainThread(Events events) {
        if (events == null || events.getEvent() == null) return;
        if (events.getEvent() instanceof Events.CallToRefresh) {
            if (((Events.CallToRefresh) events.getEvent()).needRefresh) mCircleRequest.execute();
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDynamicController = new DynamicController(this, this);
        View header = LayoutInflater.from(this).inflate(R.layout.item_header, null, false);
        bindListView(R.id.listview, header,
                FriendCircleAdapterUtil.getAdapter(this, mMomentsInfos, mDynamicController));
        initReq();
        //mListView.manualRefresh();
    }

    private void initReq() {
        mCircleRequest = new FriendCircleRequest(1001, 0, 8);
        mCircleRequest.setOnResponseListener(this);
    }

    @Override
    public ImageView bindRefreshIcon() {
        return (ImageView) findViewById(R.id.rotate_icon);
    }

    @Override
    public void onPullDownRefresh() {
        mCircleRequest.setStart(0);
        mCircleRequest.execute();
    }

    @Override
    public void onLoadMore() {
        mCircleRequest.execute();
    }

    @Override
    public void onSuccess(BaseResponse response) {
        super.onSuccess(response);
    }

    @Override
    public void onResultCallBack(BaseResponse response) {
        // 通知更新
        switch (response.getRequestType()) {
            case RequestType.ADD_PRAISE:
                DynamicControllerEntity<List<UserInfo>> entity
                        = (DynamicControllerEntity<List<UserInfo>>) response.getData();
                MomentsInfo info = entity.getMomentsInfo();
                info.dynamicInfo.praiseState=CommonValue.HAS_PRAISE;
                if (info != null) {
                    if (info.praiseList != null) {
                        info.praiseList.clear();
                        info.praiseList.addAll(entity.getData());
                    }else {
                        info.praiseList=entity.getData();
                    }
                }
                mAdapter.notifyDataSetChanged();
                break;
            case RequestType.CANCEL_PRAISE:
                DynamicControllerEntity<List<UserInfo>> cancelEntity
                        = (DynamicControllerEntity<List<UserInfo>>) response.getData();
                MomentsInfo mInfo = cancelEntity.getMomentsInfo();
                mInfo.dynamicInfo.praiseState=CommonValue.NOT_PRAISE;
                if (mInfo != null) {
                    if (mInfo.praiseList != null) {
                        mInfo.praiseList.clear();
                        mInfo.praiseList.addAll(cancelEntity.getData());
                    }else {
                        mInfo.praiseList=cancelEntity.getData();
                    }
                }
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDynamicController.destroyController();
        mDynamicController = null;
    }
}

