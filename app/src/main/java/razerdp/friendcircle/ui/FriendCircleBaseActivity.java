package razerdp.friendcircle.ui;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import in.srain.cube.views.ptr.PtrFrameLayout;
import java.util.ArrayList;
import java.util.List;
import razerdp.friendcircle.api.data.model.MomentsInfo;
import razerdp.friendcircle.api.network.base.BaseResponse;
import razerdp.friendcircle.api.network.base.BaseResponseListener;
import razerdp.friendcircle.api.ptrwidget.OnLoadMoreRefreshListener;
import razerdp.friendcircle.api.ptrwidget.OnPullDownRefreshListener;
import razerdp.friendcircle.api.ptrwidget.PullMode;
import razerdp.friendcircle.utils.ToastUtils;
import razerdp.friendcircle.widget.ptrwidget.FriendCirclePtrListView;

/**
 * Created by 大灯泡 on 2016/2/25.
 * 封装activity
 */
public abstract class FriendCircleBaseActivity extends AppCompatActivity implements BaseResponseListener {

    protected FriendCirclePtrListView mListView;
    protected List<MomentsInfo> mMomentsInfos = new ArrayList<>();
    protected BaseAdapter mAdapter;

    public void bindListView(int listResId, View headerView, BaseAdapter adapter) {
        this.mAdapter = adapter;
        mListView = (FriendCirclePtrListView) findViewById(listResId);
        mListView.setRotateIcon(bindRefreshIcon());
        mListView.addHeaderView(headerView);
        mListView.setAdapter(adapter);

        mListView.setOnPullDownRefreshListener(new OnPullDownRefreshListener() {
            @Override
            public void onRefreshing(PtrFrameLayout frame) {
                onPullDownRefresh();
            }
        });
        mListView.setOnLoadMoreRefreshListener(new OnLoadMoreRefreshListener() {
            @Override
            public void onRefreshing(PtrFrameLayout frame) {
                onLoadMore();
            }
        });
    }

    @Override
    public void onStart(BaseResponse response) {

    }

    @Override
    public void onStop(BaseResponse response) {

    }

    @Override
    public void onFailure(BaseResponse response) {
        ToastUtils.ToastMessage(this, "网络出错。。。。");
        if (mListView != null) {
            mListView.refreshComplete();
        }
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response.getRequestType() == 0) {
            if (response.getStatus() == 200) {
                // FIXME: 2016/2/25 确保request没错。。。。
                List<MomentsInfo> momentsInfos = (List<MomentsInfo>) response.getData();
                if (mListView != null && mListView.getCurMode() == PullMode.FROM_START){
                    mMomentsInfos.clear();
                }
                mListView.setHasMore(response.getHasMore());
                mMomentsInfos.addAll(momentsInfos);
                mListView.refreshComplete();
                mAdapter.notifyDataSetChanged();
            }else {
                mListView.refreshComplete();
                ToastUtils.ToastMessage(this,response.getErrorMsg());
            }
        }
    }

    //=============================================================绑定刷新Logo
    public abstract ImageView bindRefreshIcon();

    //=============================================================下拉操作/底部加载
    public abstract void onPullDownRefresh();

    public abstract void onLoadMore();
}
