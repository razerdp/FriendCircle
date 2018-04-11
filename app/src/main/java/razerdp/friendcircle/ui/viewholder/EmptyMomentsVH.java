package razerdp.friendcircle.ui.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.razerdp.github.com.common.entity.MomentsInfo;

import razerdp.friendcircle.R;
import razerdp.github.com.ui.base.adapter.LayoutId;


/**
 * Created by 大灯泡 on 2016/11/3.
 * <p>
 * 空内容的vh
 *
 * @see MomentsType
 */

@LayoutId(id = R.layout.moments_empty_content)
public class EmptyMomentsVH extends CircleBaseViewHolder {


    public EmptyMomentsVH(View itemView, int viewType) {
        super(itemView, viewType);
    }

    @Override
    public void onFindView(@NonNull View rootView) {

    }

    @Override
    public void onBindDataToView(@NonNull MomentsInfo data, int position, int viewType) {

    }
}
