package razerdp.friendcircle.ui.adapter;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by 大灯泡 on 2016/11/1.
 *
 * 抽象出的vh接口
 */

public interface BaseMomentVH<T> {

    void onFindView(@NonNull View rootView);

    void onBindDataToView(@NonNull final T data,int position,int viewType);
}
