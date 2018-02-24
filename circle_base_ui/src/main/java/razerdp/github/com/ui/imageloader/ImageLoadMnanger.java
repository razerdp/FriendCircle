package razerdp.github.com.ui.imageloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import razerdp.github.com.baseuilib.R;
import razerdp.github.com.lib.api.AppContext;


/**
 * Created by 大灯泡 on 2016/11/1.
 * <p>
 * 图片加载
 */

public enum ImageLoadMnanger {
    INSTANCE;

    private final GlideDispatcher DISPATCHER = new GlideDispatcher();

    public static RequestOptions OPTION_DEFAULT = new RequestOptions().placeholder(R.drawable.image_nophoto).error(R.drawable.image_nophoto);
    public static RequestOptions OPTION_TRANSLATE_PLACEHOLDER = new RequestOptions().placeholder(new ColorDrawable()).error(new ColorDrawable());


    public void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    public void loadImage(ImageView iv, Object o) {
        loadImage(iv, R.drawable.image_nophoto, o);
    }

    public void loadImage(ImageView iv, int placeHolder, Object o) {
        loadImage(iv, placeHolder, R.drawable.image_nophoto, o);
    }

    public void loadImage(ImageView iv, int placeHolder, int errorHolder, Object o) {
        DISPATCHER.getGlide(o, iv).apply(OPTION_DEFAULT.placeholder(placeHolder).error(errorHolder)).into(iv);
    }

    public void loadImageWithoutAnimate(ImageView iv, Object o) {
        DISPATCHER.getGlide(o, iv).apply(OPTION_DEFAULT.dontAnimate()).into(iv);
    }

    public RequestBuilder glide(ImageView iv, Object o) {
        return DISPATCHER.getGlide(o, iv);
    }

    private Context getImageContext(@Nullable ImageView imageView) {
        if (imageView == null) return AppContext.getAppContext();
        Context context = imageView.getContext();
        if (context instanceof Activity) {
            Activity act = ((Activity) context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (act.isDestroyed() || act.isFinishing()) return AppContext.getAppContext();
            } else {
                if (act.isFinishing()) return AppContext.getAppContext();
            }
            return act;
        }
        return context;
    }


    private class GlideDispatcher {

        RequestBuilder getGlide(Object o, ImageView iv) {
            RequestManager manager = Glide.with(getImageContext(iv));
            if (o instanceof String) {
                try {
                    int t = Integer.parseInt(String.valueOf(o));
                    return getGlideInteger(manager, t, iv);
                } catch (Exception e) {
                    return getGlideString(manager, (String) o, iv);
                }
            } else if (o instanceof Integer) {
                return getGlideInteger(manager, (Integer) o, iv);
            } else if (o instanceof Uri) {
                return getGlideUri(manager, (Uri) o, iv);
            } else if (o instanceof File) {
                return getGlideFile(manager, (File) o, iv);
            }
            return getGlideString(manager, "", iv);
        }

        private RequestBuilder getGlideString(RequestManager manager, String str, ImageView iv) {
            return manager.load(str);
        }

        private RequestBuilder getGlideInteger(RequestManager manager, int source, ImageView iv) {
            return manager.load(source);
        }

        private RequestBuilder getGlideUri(RequestManager manager, Uri uri, ImageView iv) {
            return manager.load(uri);
        }

        private RequestBuilder getGlideFile(RequestManager manager, File file, ImageView iv) {
            return manager.load(file);
        }
    }
}
