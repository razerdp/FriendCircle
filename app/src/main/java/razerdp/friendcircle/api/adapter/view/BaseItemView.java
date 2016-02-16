package razerdp.friendcircle.api.adapter.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by 大灯泡 on 2016/2/16.
 * 朋友圈内容接口
 */
public interface BaseItemView<T> {
    int getViewRes();

    void onBindData(final int position, @NonNull View v, @NonNull T data,final int dynamicType);
    void onFindView(@NonNull View parent);

    Activity getActivityContext();

    void setActivityContext(Activity context);
}
