package razerdp.friendcircle.ui.viewholder;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.razerdp.github.com.common.entity.MomentsInfo;
import com.razerdp.github.com.common.entity.PhotoBrowseInfo;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import razerdp.friendcircle.R;
import razerdp.friendcircle.activity.ActivityLauncher;
import razerdp.github.com.ui.imageloader.ImageLoadMnanger;
import razerdp.github.com.ui.widget.imageview.ForceClickImageView;
import razerdp.github.com.widget.PhotoContents;
import razerdp.github.com.widget.adapter.PhotoContentsBaseAdapter;

/**
 * Created by 大灯泡 on 2016/11/3.
 * <p>
 * 九宮格圖片的vh
 *
 * @see MomentsType
 */

public class MultiImageMomentsVH extends CircleBaseViewHolder implements PhotoContents.OnItemClickListener {


    private PhotoContents imageContainer;
    private InnerContainerAdapter adapter;

    public MultiImageMomentsVH(Context context, ViewGroup viewGroup, int layoutResId) {
        super(context, viewGroup, layoutResId);
    }

    @Override
    public void onFindView(@NonNull View rootView) {
        imageContainer = (PhotoContents) findView(imageContainer, R.id.circle_image_container);
        if (imageContainer.getmOnItemClickListener() == null) {
            imageContainer.setmOnItemClickListener(this);
        }
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

    @Override
    public void onItemClick(ImageView imageView, int i) {
        PhotoBrowseInfo info = PhotoBrowseInfo.create(adapter.datas, imageContainer.getContentViewsDrawableRects(), i);
        ActivityLauncher.startToPhotoBrosweActivity((Activity) getContext(), info);
    }


    private static class InnerContainerAdapter extends PhotoContentsBaseAdapter {


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

    }
}
