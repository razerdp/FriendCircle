package razerdp.github.com.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import razerdp.github.com.baselibrary.imageloader.ImageLoadMnanger;
import razerdp.github.com.baselibrary.manager.localphoto.LocalPhotoManager;
import razerdp.github.com.baseuilib.baseadapter.BaseRecyclerViewAdapter;
import razerdp.github.com.baseuilib.baseadapter.BaseRecyclerViewHolder;
import razerdp.github.com.baseuilib.widget.imageview.CheckImageView;
import razerdp.github.com.photoselect.R;

/**
 * Created by 大灯泡 on 2017/3/24.
 * <p>
 * 图片选择adapter
 */

public class PhotoSelectAdapter extends BaseRecyclerViewAdapter<LocalPhotoManager.ImageInfo> {

    public PhotoSelectAdapter(@NonNull Context context, @NonNull List<LocalPhotoManager.ImageInfo> datas) {
        super(context, datas);
    }

    @Override
    protected int getViewType(int position, @NonNull LocalPhotoManager.ImageInfo data) {
        return 0;
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_photo_select;
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType) {
        return new InnerViewHolder(rootView, viewType);
    }


    private static class InnerViewHolder extends BaseRecyclerViewHolder<LocalPhotoManager.ImageInfo> {

        private CheckImageView checkImageView;

        public InnerViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
            checkImageView = (CheckImageView) findViewById(R.id.iv_photo_select);
        }

        @Override
        public void onBindData(LocalPhotoManager.ImageInfo data, int position) {
            ImageLoadMnanger.INSTANCE.loadImage(checkImageView, data.imagePath);
        }
    }
}
