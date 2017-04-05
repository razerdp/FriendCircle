package razerdp.github.com.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import razerdp.github.com.baselibrary.imageloader.ImageLoadMnanger;
import razerdp.github.com.baselibrary.interfaces.ClearMemoryObject;
import razerdp.github.com.baselibrary.manager.localphoto.LocalPhotoManager;
import razerdp.github.com.baselibrary.utils.SimpleObjectPool;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewEx;

/**
 * Created by 大灯泡 on 2017/4/1.
 * <p>
 * 图片预览的adapter
 */

public class PhotoBrowserAdapter extends PagerAdapter implements ClearMemoryObject {

    private List<LocalPhotoManager.ImageInfo> datas;
    private Context context;
    private SimpleObjectPool<PhotoViewEx> viewPool;
    private PhotoViewAttacher.OnViewTapListener onViewTapListener;

    public PhotoBrowserAdapter(Context context, List<LocalPhotoManager.ImageInfo> datas) {
        viewPool = new SimpleObjectPool<>();
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    public LocalPhotoManager.ImageInfo getImageInfo(int pos) {
        try {
            return datas.get(pos);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final LocalPhotoManager.ImageInfo info = datas.get(position);
        PhotoViewEx photoView = viewPool.get();
        if (photoView == null) {
            photoView = new PhotoViewEx(context);
            photoView.setCacheInViewPager(true);
            photoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        ImageLoadMnanger.INSTANCE.loadImage(photoView, info.getImagePath());
        container.addView(photoView);
        if (photoView.getOnViewTapListener() == null) {
            photoView.setOnViewTapListener(onViewTapListener);
        }
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof PhotoViewEx) {
//            ((PhotoViewEx) object).setImageBitmap(null);
            container.removeView((View) object);
            viewPool.put((PhotoViewEx) object);
        }
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public PhotoViewAttacher.OnViewTapListener getOnViewTapListener() {
        return onViewTapListener;
    }

    public void setOnViewTapListener(PhotoViewAttacher.OnViewTapListener onViewTapListener) {
        this.onViewTapListener = onViewTapListener;
    }

    @Override
    public void clearMemroy(boolean setNull) {

    }

}
