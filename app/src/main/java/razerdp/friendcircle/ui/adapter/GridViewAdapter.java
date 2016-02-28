package razerdp.friendcircle.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import java.util.List;
import razerdp.friendcircle.R;
import razerdp.friendcircle.api.adapter.MBaseAdapter;
import razerdp.friendcircle.api.adapter.MViewHolder;
import razerdp.friendcircle.widget.SuperImageView;

/**
 * Created by 大灯泡 on 2016/2/28.
 * gridview adapter
 */
public class GridViewAdapter extends MBaseAdapter<String,GridViewAdapter.ViewHolder> {

    public GridViewAdapter(Context context, List<String> datas) {
        super(context, datas);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_grid_image;
    }

    @Override
    public ViewHolder initViewHolder() {
        return new ViewHolder();
    }

    public void reSetData(List<String> newDatas){
        datas.clear();
        datas.addAll(newDatas);
        notifyDataSetChanged();
    }

    @Override
    public void onBindView(int position, String data, ViewHolder holder) {
        if (!TextUtils.isEmpty(data)){
            holder.mSuperImageView.loadImageNoFade(data,0);
        }
    }

    class ViewHolder implements MViewHolder {
        public SuperImageView mSuperImageView;

        @Override
        public void onInFlate(View v) {
            mSuperImageView= (SuperImageView) v.findViewById(R.id.img);
        }
    }
}
