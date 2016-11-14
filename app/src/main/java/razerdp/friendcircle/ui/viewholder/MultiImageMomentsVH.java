package razerdp.friendcircle.ui.viewholder;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import razerdp.friendcircle.R;
import razerdp.friendcircle.app.imageload.ImageLoadMnanger;
import razerdp.friendcircle.mvp.model.entity.MomentsInfo;
import razerdp.friendcircle.ui.adapter.CircleBaseViewHolder;
import razerdp.friendcircle.widget.NoScrollGridView;
import razerdp.friendcircle.widget.circleimagecontainer.CircleImageContainer;
import razerdp.friendcircle.widget.circleimagecontainer.adapter.CircleBaseImageAdapter;
import razerdp.friendcircle.widget.imageview.ForceClickImageView;

/**
 * Created by 大灯泡 on 2016/11/3.
 * <p>
 * 九宮格圖片的vh
 *
 * @see razerdp.friendcircle.config.MomentsType
 */

public class MultiImageMomentsVH extends CircleBaseViewHolder {


    private CircleImageContainer imageContainer;
    private InnerContainerAdapter adapter;

    public MultiImageMomentsVH(Context context, ViewGroup viewGroup, int layoutResId) {
        super(context, viewGroup, layoutResId);
    }

    @Override
    public void onFindView(@NonNull View rootView) {
        imageContainer = (CircleImageContainer) findView(imageContainer, R.id.circle_image_container);
    }

    @Override
    public void onBindDataToView(@NonNull MomentsInfo data, int position, int viewType) {
        if (adapter == null) {
            adapter = new InnerContainerAdapter(getContext(), data.getContent().getPics());
            imageContainer.setAdapter(adapter);
        } else {
            KLog.i("update image" + data.getAuthor().getNick() + "     :  " + data.getContent().getPics().size());
            adapter.updateData(data.getContent().getPics());
        }
    }


    private static class InnerContainerAdapter extends CircleBaseImageAdapter {


        private Context context;
        private List<String> datas;

        InnerContainerAdapter(Context context, List<String> datas) {
            this.context = context;
            this.datas = new ArrayList<>();
            this.datas.addAll(datas);
        }

        @Override
        public ImageView onCreateView(ImageView convertView, ViewGroup parent, int position) {
            if (convertView == null) {
                convertView = new ForceClickImageView(context);
                convertView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                KLog.e("newInstance");
            }
            return convertView;
        }

        @Override
        public void onBindData(int position, @NonNull ImageView convertView) {
            ImageLoadMnanger.INSTANCE.loadImage(convertView, datas.get(position));
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        public void updateData(List<String> datas) {
            this.datas.clear();
            this.datas.addAll(datas);
            notifyDataChanged();
        }

        @Override
        public void onItemClick(View convertView, int position, Rect visibleRect, Rect[] allItemRects) {

        }
    }
}
