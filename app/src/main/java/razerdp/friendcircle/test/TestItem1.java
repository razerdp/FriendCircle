package razerdp.friendcircle.test;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import razerdp.friendcircle.R;
import razerdp.friendcircle.api.adapter.view.BaseItemDelegate;

/**
 * Created by 大灯泡 on 2016/2/16.
 * 测试布局1
 */
public class TestItem1 extends BaseItemDelegate<TestBean> {
    private TextView testTx;
    private Button testBtn;

    public TestItem1() {}

    @Override
    protected void bindData(int position, @NonNull View v, @NonNull TestBean data, int dynamicType) {
        testBtn.setTag(data);
        testTx.setText(data.testStr);
    }

    @Override
    public int getViewRes() {
        return R.layout.test_type_one;
    }

    @Override
    public void onFindView(@NonNull View parent) {
        testTx = (TextView) parent.findViewById(R.id.test_1);
        testBtn = (Button) parent.findViewById(R.id.btn_test_1);

        testTx.setOnClickListener(this);
        testBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.test_1:
                break;
            case R.id.btn_test_1:
                TestBean bean = (TestBean) v.getTag();
                Toast.makeText(getActivityContext(), "你点的是类型  " + bean.type, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
