package razerdp.friendcircle.api.adapter.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by 大灯泡 on 2016/2/16.
 * 基本item封装
 */
public abstract class BaseItemDelegate<T> implements BaseItemView<T>, View.OnClickListener {
    protected Activity context;

    public BaseItemDelegate() {
    }

    public BaseItemDelegate(Activity context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBindData(int position, @NonNull View v, @NonNull T data, final int dynamicType) {
        // TODO: 2016/2/16 初始化共用部分
        bindData(position, v, data, dynamicType);
    }

    @Override
    public Activity getActivityContext() {
        return context;
    }

    @Override
    public void setActivityContext(Activity context) {
        this.context=context;
    }

    protected abstract void bindData(int position, @NonNull View v, @NonNull T data, final int dynamicType);
}
