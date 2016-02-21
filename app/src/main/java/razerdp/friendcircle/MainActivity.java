package razerdp.friendcircle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import razerdp.friendcircle.api.data.model.PraiseInfo;
import razerdp.friendcircle.widget.praisewidget.PraiseWidget;

public class MainActivity extends AppCompatActivity {

    private PraiseWidget praise;
    private List<PraiseInfo> mPraiseInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        initView();
    }

    private void initView() {
        praise = (PraiseWidget) findViewById(R.id.praise);
        mPraiseInfos=new ArrayList<>();
        for (int i=0;i<50;i++){
            PraiseInfo info=new PraiseInfo();
            info.userNick="用户"+i;
            info.userId=i;
            mPraiseInfos.add(info);
        }
        praise.setDatas(mPraiseInfos);
    }
}

