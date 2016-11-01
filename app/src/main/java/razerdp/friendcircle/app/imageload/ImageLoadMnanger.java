package razerdp.friendcircle.app.imageload;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;

import razerdp.friendcircle.app.FriendCircleApp;

/**
 * Created by 大灯泡 on 2016/11/1.
 * <p>
 * 图片加载
 */

public enum ImageLoadMnanger {
    INSTANCE;

    public void clearMemory() {
        Glide.get(FriendCircleApp.getAppContext()).clearMemory();
    }

    public void loadImage(ImageView imageView, String imgUrl) {
        loadImageByNormalConfig(imageView, imgUrl).placeholder(new ColorDrawable(0xffc6c6c6))
                                                  .into(imageView);
    }

    public void loadImage(ImageView imageView, String imgUrl, int width, int height) {
        loadImageByNormalConfig(imageView, imgUrl).placeholder(new ColorDrawable(0xffc6c6c6))
                                                  .override(width, height)
                                                  .into(imageView);
    }


    private BitmapRequestBuilder loadImageByNormalConfig(ImageView imageView, String url) {
        return Glide.with(getImageContext(imageView)).load(url).asBitmap().thumbnail(0.5f);
    }

    private Context getImageContext(@Nullable ImageView imageView) {
        if (imageView == null) {
            return FriendCircleApp.getAppContext();
        }
        return imageView.getContext();
    }
}
