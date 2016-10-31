package razerdp.friendcircle.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import razerdp.friendcircle.R;
import razerdp.friendcircle.app.mvp.model.entity.MomentsInfo;
import razerdp.friendcircle.app.net.OnResponseListener;
import razerdp.friendcircle.app.net.request.MomentsRequest;
import razerdp.friendcircle.utils.bmob.BmobTestDatasHelper;
import razerdp.friendcircle.widget.pullrecyclerview.CircleRecyclerView;
import razerdp.friendcircle.widget.pullrecyclerview.interfaces.OnRefreshListener2;

/**
 * Created by 大灯泡 on 2016/10/26.
 */

public class FriendCircleDemoActivity extends AppCompatActivity implements OnRefreshListener2{

    private CircleRecyclerView circle_recyclervew;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

      /*  MomentsRequest momentsRequest=new MomentsRequest();
        momentsRequest.setOnResponseListener(new OnResponseListener<List<MomentsInfo>>() {
            @Override
            public void onStart(int requestType) {

            }

            @Override
            public void onSuccess(List<MomentsInfo> response, int requestType) {
                KLog.d();

            }

            @Override
            public void onError(BmobException e, int requestType) {
                KLog.d();

            }
        });
        momentsRequest.execute();*/

        //添加动态
     /*   final BmobInitHelper helper=new BmobInitHelper();
        helper.initUser(new SimpleResponseListener() {
            @Override
            public void onSuccess(Object response, int requestType) {
                KLog.d(response);
                helper.addMoments();
            }
        });*/

    }

    private void initView() {
        circle_recyclervew = (CircleRecyclerView) findViewById(R.id.recycler);
        circle_recyclervew.setOnRefreshListener(this);

//        测试添加头布局和尾布局
        circle_recyclervew.addHeaderView(LayoutInflater.from(this).inflate(R.layout.item_header,null));
        circle_recyclervew.addFooterView(LayoutInflater.from(this).inflate(R.layout.item_header,null));

        circle_recyclervew.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(new TextView(parent.getContext()));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView)((ViewHolder)holder).itemView).setText(BmobTestDatasHelper.getText(position));
            }

            @Override
            public int getItemCount() {
                return BmobTestDatasHelper.getTexts().length;
            }

            class ViewHolder extends RecyclerView.ViewHolder {

                public ViewHolder(View itemView) {
                    super(itemView);
                }
            }
        });

    }

    @Override
    public void onRefresh() {

        MomentsRequest momentsRequest=new MomentsRequest();
        momentsRequest.setOnResponseListener(new OnResponseListener<List<MomentsInfo>>() {
            @Override
            public void onStart(int requestType) {

            }

            @Override
            public void onSuccess(List<MomentsInfo> response, int requestType) {
                KLog.d();
                circle_recyclervew.compelete();
            }

            @Override
            public void onError(BmobException e, int requestType) {
                KLog.d();

            }
        });
        momentsRequest.execute();
    }

    @Override
    public void onLoadMore() {

    }
}
