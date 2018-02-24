package razerdp.github.com.ui.imageloader.glide;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;
import com.socks.library.KLog;

import static android.text.format.Formatter.formatFileSize;


/**
 * Created by 大灯泡 on 2018/2/24.
 * <p>
 * glide 4.5配置
 */
@com.bumptech.glide.annotation.GlideModule
public class DefaultGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //磁盘缓存
        builder.setDiskCache(new DiskLruCacheFactory(context.getCacheDir().getAbsolutePath(), 50 * 1024 * 1024));
        KLog.d("Glide", "glide cache file path  >>>  " + context.getCacheDir().getAbsolutePath());
        //内存缓存
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).build();
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
        //设置比默认大小大1.5倍的缓存和图片池大小
        int customMemoryCacheSize = (int) (1.5 * defaultMemoryCacheSize);
        int customBitmapPoolSize = defaultBitmapPoolSize;

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));


        KLog.d("Glide", "bitmapPoolSize >>>>>   " +
                formatFileSize(context, customBitmapPoolSize) +
                " / memorySize>>>>>>>>   " +
                formatFileSize(context, customMemoryCacheSize));

        builder.setLogLevel(Log.ERROR);
    }
}
