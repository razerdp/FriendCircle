package razerdp.friendcircle;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
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

    }
}
