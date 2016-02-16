package razerdp.friendcircle.test;

import android.app.Activity;
import java.util.ArrayList;
import java.util.List;
import razerdp.friendcircle.api.adapter.CircleBaseAdapter;
import razerdp.friendcircle.ui.FriendCircleAdapterTest;

/**
 * Created by 大灯泡 on 2016/2/16.
 * 测试数据
 */
public class TestDatas {
    private static String[] typeStrs = { "这是类型10，当前位置： ", "这是类型11，当前位置： ", "这是类型12，当前位置： ", "这是类型13，当前位置： " };
    private static int[] types = { 10, 11, 12, 13 };

    public static List<TestBean> getTestDatasList() {
        List<TestBean> testDatas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            TestBean bean = new TestBean();
            bean.testStr = typeStrs[i % typeStrs.length]+i;
            bean.type = types[i % types.length];
            testDatas.add(bean);
        }
        return testDatas;
    }

    public static FriendCircleAdapterTest<TestBean> getAdapter(Activity context) {
        return new FriendCircleAdapterTest<>(context,
                new CircleBaseAdapter.Builder(getTestDatasList()).addType(types[0], TestItem1.class)
                                                                 .addType(types[1], TestItem2.class)
                                                                 .addType(types[2], TestItem3.class)
                                                                 .addType(types[3], TestItem4.class)
                                                                 .build());
    }
}
