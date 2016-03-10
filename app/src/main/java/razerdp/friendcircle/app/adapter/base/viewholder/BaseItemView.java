package razerdp.friendcircle.app.adapter.base.viewholder;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import razerdp.friendcircle.app.controller.BaseDynamicController;

/**
 * Created by 大灯泡 on 2016/2/16.
 * 朋友圈item接口化
 */
public interface BaseItemView<T> {
    int getViewRes();
    void onFindView(@NonNull View parent);
    void onBindData(final int position, @NonNull View v, @NonNull T data,final int dynamicType);
    Activity getActivityContext();
    void setActivityContext(Activity context);
    void setController(BaseDynamicController controller);
    BaseDynamicController getController();

}
