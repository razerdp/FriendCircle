package razerdp.github.com.photoselect;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import razerdp.github.com.baseuilib.base.BaseTitleBarActivity;

/**
 * Created by 大灯泡 on 2017/3/22.
 *
 * 图片选择器
 */

public class PhotoSelectActivity extends BaseTitleBarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoselect);
    }

    @Override
    public void onHandleIntent(Intent intent) {

    }
}
