package razerdp.friendcircle.ui.adapter.itemview;

import android.support.annotation.NonNull;
import android.view.View;
import razerdp.friendcircle.R;
import razerdp.friendcircle.api.adapter.view.BaseItemDelegate;
import razerdp.friendcircle.api.data.entity.MomentsInfo;
import razerdp.friendcircle.ui.adapter.GridViewAdapter;
import razerdp.friendcircle.widget.NoScrollGridView;

/**
 * Created by 大灯泡 on 2016/2/25.
 * 图文
 * type=13
 */
public class ItemWithImg extends BaseItemDelegate {
    private NoScrollGridView mNoScrollGridView;
    private GridViewAdapter mGridViewAdapter;

    public ItemWithImg() {}

    @Override
    public int getViewRes() {
        return R.layout.dynamic_item_with_img;
    }

    @Override
    public void onFindView(@NonNull View parent) {
        if (mNoScrollGridView == null) mNoScrollGridView = (NoScrollGridView) parent.findViewById(R.id.item_grid);
    }

    @Override
    protected void bindData(int position, @NonNull View v, @NonNull MomentsInfo data, int dynamicType) {
        if (data.content.imgurl==null||data.content.imgurl.size()==0||mNoScrollGridView==null)return;
        if (mNoScrollGridView.getAdapter()==null){
            mGridViewAdapter=new GridViewAdapter(context,data.content.imgurl);
            mNoScrollGridView.setAdapter(mGridViewAdapter);
        }
        if (data.content.imgurl.size()==4){
            mNoScrollGridView.setNumColumns(2);
        }else {
            mNoScrollGridView.setNumColumns(3);
        }
        mGridViewAdapter.reSetData(data.content.imgurl);
    }
}
