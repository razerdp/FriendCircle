package razerdp.friendcircle.activity.circledemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import razerdp.friendcircle.R;
import razerdp.github.com.ui.base.BaseTitleBarActivity;

/**
 * Created by 大灯泡 on 2018/10/26.
 * <p>
 * 实际上就是mainActivity那套，针对Fragment用
 */
public class FriendCircleDemoFragmentActivity extends BaseTitleBarActivity {
    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_with_fragment);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new FriendCircleFragmentDemo())
                    .commit();
        }
    }

}
