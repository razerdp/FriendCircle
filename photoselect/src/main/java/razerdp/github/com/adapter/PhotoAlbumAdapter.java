package razerdp.github.com.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import razerdp.github.com.baselibrary.imageloader.ImageLoadMnanger;
import razerdp.github.com.baseuilib.baseadapter.BaseRecyclerViewAdapter;
import razerdp.github.com.baseuilib.baseadapter.BaseRecyclerViewHolder;
import razerdp.github.com.model.AlbumInfo;
import razerdp.github.com.photoselect.R;

/**
 * Created by 大灯泡 on 2017/3/24.
 * <p>
 * 相册adapter
 */

public class PhotoAlbumAdapter extends BaseRecyclerViewAdapter<AlbumInfo> {
    private static final String TAG = "PhotoAlbumAdapter";

    public PhotoAlbumAdapter(@NonNull Context context, @NonNull List<AlbumInfo> datas) {
        super(context, datas);
    }


    @Override
    protected int getViewType(int position, @NonNull AlbumInfo data) {
        return 0;
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_photo_album;
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType) {
        return new InnerViewHolder(rootView, viewType);
    }

    private class InnerViewHolder extends BaseRecyclerViewHolder<AlbumInfo> {

        private ImageView albumThumb;
        private TextView albumName;
        private TextView photoCounts;



        public InnerViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
            albumThumb= (ImageView) findViewById(R.id.album_thumb);
            albumName= (TextView) findViewById(R.id.album_name);
            photoCounts= (TextView) findViewById(R.id.album_photo_counts);
        }

        @Override
        public void onBindData(AlbumInfo data, int position) {
            photoCounts.setText(String.format(Locale.getDefault(),"(%d)",data.getPhotoCounts()));
            albumName.setText(data.getAlbumName());
            ImageLoadMnanger.INSTANCE.loadImage(albumThumb,data.getFirstPhoto());
        }
    }

}
