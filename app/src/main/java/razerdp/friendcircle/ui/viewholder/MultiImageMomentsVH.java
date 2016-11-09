package razerdp.friendcircle.ui.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

import razerdp.friendcircle.R;
import razerdp.friendcircle.app.baseadapter.BaseAbsViewAdapter;
import razerdp.friendcircle.app.baseadapter.BaseAbsViewHolder;
import razerdp.friendcircle.app.imageload.ImageLoadMnanger;
import razerdp.friendcircle.mvp.model.entity.MomentsInfo;
import razerdp.friendcircle.ui.adapter.CircleBaseViewHolder;
import razerdp.friendcircle.utils.StringUtil;
import razerdp.friendcircle.widget.NoScrollGridView;
import razerdp.friendcircle.widget.imageview.ForceClickImageView;

/**
 * Created by 大灯泡 on 2016/11/3.
 * <p>
 * 九宮格圖片的vh
 *
 * @see razerdp.friendcircle.config.MomentsType
 */

public class MultiImageMomentsVH extends CircleBaseViewHolder implements AdapterView.OnItemClickListener {


    private NoScrollGridView noScrollGridView;
    private InnerGridVieAdapter adapter;

    public MultiImageMomentsVH(Context context, ViewGroup viewGroup, int layoutResId) {
        super(context, viewGroup, layoutResId);
    }

    @Override
    public void onFindView(@NonNull View rootView) {
        noScrollGridView = (NoScrollGridView) findView(noScrollGridView, R.id.item_grid);
        noScrollGridView.setOnItemClickListener(this);
    }

    @Override
    public void onBindDataToView(@NonNull MomentsInfo data, int position, int viewType) {
        if (adapter == null) {
            adapter = new InnerGridVieAdapter(getContext(), data.getContent().getPics());
            noScrollGridView.setAdapter(adapter);
        }
        adapter.updateData(data.getContent().getPics());
        if (data.getContent().getPics().size() == 4) {
            noScrollGridView.setNumColumns(2);
        } else {
            noScrollGridView.setNumColumns(3);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: 2016/11/9 图片展示
    }


    private static class InnerGridVieAdapter extends BaseAbsViewAdapter<String, InnerGridVieAdapter.InnerGridViewHolder> {


         InnerGridVieAdapter(Context context, List<String> datas) {
            super(context, datas);
        }

        @Override
        public int getLayoutRes() {
            return R.layout.item_grid_image;
        }

        @Override
        public InnerGridViewHolder initViewHolder() {
            return new InnerGridViewHolder();
        }

        @Override
        public void onBindData(int position, String data, InnerGridViewHolder holder) {
            if (StringUtil.noEmpty(data)) {
                ImageLoadMnanger.INSTANCE.loadImage(holder.mSuperImageView, data);
            }
        }

        static class InnerGridViewHolder implements BaseAbsViewHolder {

            ForceClickImageView mSuperImageView;

            @Override
            public void onInFlate(View v) {
                mSuperImageView = (ForceClickImageView) v.findViewById(R.id.img);
            }
        }
    }
}
