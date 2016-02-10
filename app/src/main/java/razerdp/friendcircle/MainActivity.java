package razerdp.friendcircle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import razerdp.friendcircle.api.ptrwidget.OnPullDownRefreshListener;
import razerdp.friendcircle.widget.ptrwidget.FriendCirclePtrListView;

public class MainActivity extends AppCompatActivity {
    private FriendCirclePtrListView mFriendCirclePtrListView;
    private ImageView rotateIcon;
    private RelativeLayout actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar= (RelativeLayout) findViewById(R.id.action_bar);
        rotateIcon= (ImageView) findViewById(R.id.rotate_icon);
        mFriendCirclePtrListView= (FriendCirclePtrListView) findViewById(R.id.listview);
        mFriendCirclePtrListView.setRotateIcon(rotateIcon);

        mFriendCirclePtrListView.setOnPullDownRefreshListener(new OnPullDownRefreshListener() {
            @Override
            public void onRefreshing(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFriendCirclePtrListView.refreshComplete();
                    }
                },1800);
            }
        });


    }
}
