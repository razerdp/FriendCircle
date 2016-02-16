package razerdp.friendcircle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import java.util.ArrayList;
import java.util.List;
import razerdp.friendcircle.api.ptrwidget.OnPullDownRefreshListener;
import razerdp.friendcircle.test.TestDatas;
import razerdp.friendcircle.widget.ptrwidget.FriendCirclePtrListView;

public class MainActivity extends AppCompatActivity {
    private FriendCirclePtrListView mFriendCirclePtrListView;
    private ImageView rotateIcon;
    private RelativeLayout actionBar;
    private List<String> test = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = (RelativeLayout) findViewById(R.id.action_bar);
        rotateIcon = (ImageView) findViewById(R.id.rotate_icon);
        mFriendCirclePtrListView = (FriendCirclePtrListView) findViewById(R.id.listview);
        mFriendCirclePtrListView.setRotateIcon(rotateIcon);

        View header = LayoutInflater.from(this).inflate(R.layout.item_header, null, false);
        mFriendCirclePtrListView.addHeaderView(header);

        mFriendCirclePtrListView.setAdapter(TestDatas.getAdapter(this));

        mFriendCirclePtrListView.setOnPullDownRefreshListener(new OnPullDownRefreshListener() {
            @Override
            public void onRefreshing(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFriendCirclePtrListView.refreshComplete();
                    }
                }, 1800);
            }
        });
        mFriendCirclePtrListView.setHasMore(true);
    }
}

