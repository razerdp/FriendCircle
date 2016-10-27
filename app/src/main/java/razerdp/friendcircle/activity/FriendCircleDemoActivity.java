package razerdp.friendcircle.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.socks.library.KLog;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import razerdp.friendcircle.R;
import razerdp.friendcircle.app.mvp.model.entity.MomentsInfo;
import razerdp.friendcircle.app.net.OnResponseListener;
import razerdp.friendcircle.app.net.request.MomentsRequest;

/**
 * Created by 大灯泡 on 2016/10/26.
 */

public class FriendCircleDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MomentsRequest momentsRequest=new MomentsRequest();
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
        momentsRequest.execute();
    }
}
