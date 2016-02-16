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
 * 测试布局2
 */
public class TestItem2 extends BaseItemDelegate<TestBean> {
    private Button testBtn;
    private TextView message;

    public TestItem2(){}

    @Override
    protected void bindData(int position, @NonNull View v, @NonNull TestBean data, int dynamicType) {
        testBtn.setTag(data);
        message.setText(data.testStr);

    }

    @Override
    public int getViewRes() {
        return R.layout.test_type_two;
    }

    @Override
    public void onFindView(@NonNull View parent) {
        testBtn= (Button) parent.findViewById(R.id.btn_test_2);
        message= (TextView) parent.findViewById(R.id.message);
        testBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_test_2:
                TestBean bean = (TestBean) v.getTag();
                Toast.makeText(getActivityContext(), "你点的是类型  " + bean.type ,
                        Toast.LENGTH_SHORT).show();
                break;
            default:
                break;

        }
    }
}
