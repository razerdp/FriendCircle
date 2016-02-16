package razerdp.friendcircle.ui;

import android.app.Activity;
import android.util.Log;
import razerdp.friendcircle.api.adapter.CircleBaseAdapter;

/**
 * Created by 大灯泡 on 2016/2/16.
 * 朋友圈适配器（测试版）
 */
public class FriendCircleAdapterTest<TestBean> extends CircleBaseAdapter {
    private static final String TAG = "FriendCircleAdapterTest";

    public FriendCircleAdapterTest(Activity context, Builder mBuilder) {
        super(context, mBuilder);
    }

    @Override
    public int getItemViewType(int position) {
        razerdp.friendcircle.test.TestBean bean= (razerdp.friendcircle.test.TestBean) datas.get(position);
        Log.d(TAG,"当前type------- "+bean.type);
        return bean.type;
    }
}
