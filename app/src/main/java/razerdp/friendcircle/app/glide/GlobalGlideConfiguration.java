package razerdp.friendcircle.app.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.ViewTarget;
import com.socks.library.KLog;

import razerdp.friendcircle.R;


/**
 * Created by 大灯泡 on 2016/11/1.
 * Glide全局配置文件
 */
public class GlobalGlideConfiguration implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //解决setTag问题
        ViewTarget.setTagId(R.id.glide_tag_id);
        //磁盘缓存
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        //内存缓存
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
        //设置比默认大小大1.5倍的缓存和图片池大小
        int customMemoryCacheSize = (int) (1.5 * defaultMemoryCacheSize);
        int customBitmapPoolSize = defaultBitmapPoolSize;

        KLog.i("poolSize",
               "bitmapPoolSize >>>>>   "
                       + android.text.format.Formatter.formatFileSize(context, customBitmapPoolSize)
                       + "          memorySize>>>>>>>>   " +
                       android.text.format.Formatter.formatFileSize(context, customMemoryCacheSize));

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
