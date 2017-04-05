package razerdp.github.com.baselibrary.imageloader;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;

import razerdp.github.com.baselibrary.R;
import razerdp.github.com.baselibrary.base.AppContext;


/**
 * Created by 大灯泡 on 2016/11/1.
 * <p>
 * 图片加载
 */

public enum ImageLoadMnanger {
    INSTANCE;

    public void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    public void loadImage(ImageView imageView, String imgUrl) {
        loadImageByNormalConfig(imageView, imgUrl).placeholder(R.drawable.image_nophoto).into(imageView);
    }

    public void loadImage(ImageView imageView, int placeHolderColor, String imgUrl) {
        loadImageByNormalConfig(imageView, imgUrl).placeholder(new ColorDrawable(placeHolderColor)).into(imageView);
    }

    public void loadImageDontAnimate(ImageView imageView, String imgUrl) {
        loadImageByNormalConfig(imageView, imgUrl).dontAnimate()
                                                  .into(imageView);
    }

    public void loadImage(ImageView imageView, String imgUrl, int width, int height) {
        loadImageByNormalConfig(imageView, imgUrl).placeholder(R.drawable.image_nophoto)
                                                  .override(width, height)
                                                  .into(imageView);
    }


    private BitmapRequestBuilder loadImageByNormalConfig(ImageView imageView, String url) {
        return Glide.with(getImageContext(imageView)).load(url).asBitmap();
    }

    private Context getImageContext(@Nullable ImageView imageView) {
        if (imageView == null) return AppContext.getAppContext();
        return imageView.getContext();
    }
}
