package razerdp.github.com.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import razerdp.github.com.ui.imageloader.ImageLoadMnanger;
import razerdp.github.com.lib.interfaces.ClearMemoryObject;
import razerdp.github.com.lib.utils.SimpleObjectPool;
import razerdp.github.com.lib.common.entity.ImageInfo;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewEx;

/**
 * Created by 大灯泡 on 2017/4/1.
 * <p>
 * 图片预览的adapter
 */

public class PhotoBrowserAdapter extends PagerAdapter implements ClearMemoryObject {

    private List<ImageInfo> datas;
    private Context context;
    private SimpleObjectPool<PhotoViewEx> viewPool;
    private PhotoViewAttacher.OnViewTapListener onViewTapListener;

    public PhotoBrowserAdapter(Context context, List<ImageInfo> datas) {
        viewPool = new SimpleObjectPool<>(PhotoViewEx.class);
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    public ImageInfo getImageInfo(int pos) {
        try {
            return datas.get(pos);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final ImageInfo info = datas.get(position);
        PhotoViewEx photoView = viewPool.get();
        if (photoView == null) {
            photoView = new PhotoViewEx(context);
            photoView.setCacheInViewPager(true);
            photoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        ImageLoadMnanger.INSTANCE.glide(photoView, info.getImagePath())
                .apply(ImageLoadMnanger.OPTION_TRANSLATE_PLACEHOLDER.dontAnimate())
                .into(photoView);
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
