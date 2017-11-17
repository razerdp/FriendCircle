package razerdp.github.com.photoselect.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import razerdp.github.com.adapter.PhotoAlbumAdapter;
import razerdp.github.com.lib.base.BaseFragment;
import razerdp.github.com.lib.manager.localphoto.LocalPhotoManager;
import razerdp.github.com.lib.utils.ToolUtil;
import razerdp.github.com.ui.base.adapter.OnRecyclerViewItemClickListener;
import razerdp.github.com.bus.EventSelectAlbum;
import razerdp.github.com.lib.common.entity.ImageInfo;
import com.razerdp.github.com.common.entity.photo.AlbumInfo;
import razerdp.github.com.photoselect.R;

/**
 * Created by 大灯泡 on 2017/3/29.
 * <p>
 * 相册浏览fragment
 */

public class PhotoAlbumFragement extends BaseFragment {
    private static final String TAG = "PhotoGridFragement";

    private RecyclerView mPhotoContent;
    private PhotoAlbumAdapter adapter;

    private List<AlbumInfo> datas;

    @Override
    public int getLayoutResId() {
        return R.layout.frag_photo_album;
    }

    @Override
    protected void onInitData() {
        if (!LocalPhotoManager.INSTANCE.hasData()) return;
        findAndSetDatas();
    }


    @Override
    protected void onInitView(View rootView) {
        mPhotoContent = findView(R.id.album_content);
        adapter = new PhotoAlbumAdapter(getActivity(), datas);
        mPhotoContent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<AlbumInfo>() {
            @Override
            public void onItemClick(View v, int position, AlbumInfo data) {
                EventBus.getDefault().post(new EventSelectAlbum(data.getAlbumName()));
            }
        });
        mPhotoContent.setAdapter(adapter);
    }

    private void findAndSetDatas() {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        LinkedHashMap<String, List<ImageInfo>> map = LocalPhotoManager.INSTANCE.getLocalImagesMap();
        Iterator iterator = map.entrySet().iterator();
        datas.clear();
        while (iterator.hasNext()) {
            Map.Entry<String, List<ImageInfo>> entry = (Map.Entry<String, List<ImageInfo>>) iterator.next();
            String albumName = entry.getKey();
            List<ImageInfo> photos = entry.getValue();
            AlbumInfo info = new AlbumInfo();
            info.setAlbumName(albumName);
            info.setPhotoCounts(photos.size());
            if (!ToolUtil.isListEmpty(photos)) {
                ImageInfo lastInfo = photos.get(photos.size() - 1);
                String firstPhoto = TextUtils.isEmpty(lastInfo.thumbnailPath) ? lastInfo.imagePath : lastInfo.thumbnailPath;
                info.setFirstPhoto(firstPhoto);
            }
            datas.add(info);
        }
    }


}
