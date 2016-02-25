package razerdp.friendcircle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import razerdp.friendcircle.ui.FriendCircleBaseActivity;

public class MainActivity extends FriendCircleBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public ImageView bindRefreshIcon() {
        return null;
    }

    @Override
    public void onPullDownRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}

