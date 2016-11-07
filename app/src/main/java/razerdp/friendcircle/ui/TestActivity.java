package razerdp.friendcircle.ui;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import razerdp.friendcircle.R;
import razerdp.friendcircle.app.imageload.ImageLoadMnanger;
import razerdp.friendcircle.app.net.request.MomentsRequest;
import razerdp.friendcircle.app.net.request.SimpleResponseListener;
import razerdp.friendcircle.mvp.model.entity.MomentsInfo;
import razerdp.friendcircle.utils.ToolUtil;
import razerdp.friendcircle.utils.UIHelper;
import razerdp.friendcircle.widget.imagecontainer.BaseCircleImageAdapter;
import razerdp.friendcircle.widget.imagecontainer.CircleImageContainer;
import razerdp.friendcircle.widget.pullrecyclerview.interfaces.OnRefreshListener2;

/**
 * Created by 大灯泡 on 2016/10/26.
 * <p>
 * 朋友圈主界面
 */

public class TestActivity extends AppCompatActivity implements OnRefreshListener2 {

    private static final int REQUEST_REFRESH = 0x10;
    private static final int REQUEST_LOADMORE = 0x11;


    //request
    private MomentsRequest momentsRequest;
    private CircleImageContainer circle_image_container;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        momentsRequest = new MomentsRequest();
        initView();
        //添加动态,伪造数据用的哦~轻易不要取消注释哦
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
        onRefresh();
        circle_image_container = (CircleImageContainer) findViewById(R.id.circle_image_container);
    }

    @Override
    public void onRefresh() {
        momentsRequest.setOnResponseListener(momentsRequestCallBack);
        momentsRequest.setRequestType(REQUEST_REFRESH);
        momentsRequest.setCurPage(0);
        momentsRequest.execute();
    }

    @Override
    public void onLoadMore() {
        momentsRequest.setOnResponseListener(momentsRequestCallBack);
        momentsRequest.setRequestType(REQUEST_LOADMORE);
        momentsRequest.execute();
    }


    //call back block
    //==============================================
    private SimpleResponseListener<List<MomentsInfo>> momentsRequestCallBack = new SimpleResponseListener<List<MomentsInfo>>() {
        @Override
        public void onSuccess(List<MomentsInfo> response, int requestType) {
            switch (requestType) {
                case REQUEST_REFRESH:
                    if (!ToolUtil.isListEmpty(response)) {
                        InnerTestAdapter adapter=new InnerTestAdapter(TestActivity.this,response.get(0).getContent().getPics());
                        circle_image_container.setAdapter(adapter);
                    }
                    break;
                case REQUEST_LOADMORE:
                    break;
            }
        }

        @Override
        public void onError(BmobException e, int requestType) {
            super.onError(e, requestType);
        }
    };


    private static class InnerTestAdapter extends BaseCircleImageAdapter{

        private List<String> pics;
        private Context context;


        public InnerTestAdapter(Context context, List<String> pics) {
            this.context = context;
            this.pics = pics;
        }

        @Override
        public ImageView getView(ImageView convertView, ViewGroup parent, int position) {
            if (convertView==null){
                convertView=new ImageView(context);
                convertView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            ImageLoadMnanger.INSTANCE.loadImage(convertView,pics.get(position));
            return convertView;
        }

        @Override
        public int getCount() {
            return pics.size();
        }

        @Override
        public void onItemClick(View convertView, int position, Rect visibleRect, Rect[] allItemRects) {
            UIHelper.ToastMessage("click");
        }
    }
}
