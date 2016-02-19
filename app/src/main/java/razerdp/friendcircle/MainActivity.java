package razerdp.friendcircle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import razerdp.friendcircle.widget.ClickShowMoreLayout;

public class MainActivity extends AppCompatActivity {

    private ClickShowMoreLayout test_click_to_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        initView();
    }

    private void initView() {
        test_click_to_show = (ClickShowMoreLayout) findViewById(R.id.test_click_to_show);
        test_click_to_show.setText("Asdfasdfasdfasdfasdfasdfadsfasdfasdfasdfasdf");
    }
}

