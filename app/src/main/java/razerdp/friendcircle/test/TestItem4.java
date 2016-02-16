package razerdp.friendcircle.test;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import razerdp.friendcircle.R;
import razerdp.friendcircle.api.adapter.view.BaseItemDelegate;

/**
 * Created by 大灯泡 on 2016/2/16.
 * 测试布局4
 */
public class TestItem4 extends BaseItemDelegate<TestBean> {
    private Button btn_test;
    private TextView messasge;

    public TestItem4() {}

    public TestItem4(Activity context) {
        super(context);
    }

    @Override
    protected void bindData(int position, @NonNull View v, @NonNull TestBean data, int dynamicType) {
        btn_test.setTag(data);
        messasge.setText(data.testStr);
    }

    @Override
    public int getViewRes() {
        return R.layout.test_type_four;
    }

    @Override
    public void onFindView(@NonNull View parent) {
        btn_test = (Button) parent.findViewById(R.id.btn_test_4);
        messasge = (TextView) parent.findViewById(R.id.message);
        btn_test.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_test_4:
                TestBean bean = (TestBean) v.getTag();
                Toast.makeText(getActivityContext(), "你点的是类型  " + bean.type, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
