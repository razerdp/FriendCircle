package razerdp.github.com.ui.imageloader.glide;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.LibraryGlideModule;
import com.socks.library.KLog;

import java.io.IOException;
import java.util.WeakHashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import razerdp.github.com.lib.network.okhttp.DownLoadErrorException;
import razerdp.github.com.lib.network.okhttp.ProgressResponseBodyWrapper;
import razerdp.github.com.lib.utils.StringUtil;


/**
 * Created by 大灯泡 on 2018/2/24.
 * <p>
 * glide 4.5配置
 */
@GlideModule
public class ProgressGlideModule extends LibraryGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Response response = chain.proceed(request);

                        String url = String.valueOf(request.url());
                        GlideProgressLoaderListener listener = ProgressListenerDispatcher.findListener(url);
                        if (listener != null) {
                            KLog.d("Glide progress handler  >> " + url);
                            return response.newBuilder()
                                    .body(new ProgressResponseBodyWrapper(response.body(), listener))
                                    .build();
                        } else {
                            return response;
                        }
                    }
                }).build();
    }


    static class ProgressListenerDispatcher {
        private static WeakHashMap<String, GlideProgressLoaderListener> mListenerHashMap = new WeakHashMap<>();
        private static Handler mHandler = new Handler(Looper.getMainLooper());

        static GlideProgressLoaderListener findListener(String url) {
            GlideProgressLoaderListener mListener = mListenerHashMap.get(url);
            return mListener == null ? null : wrapDownloadListener(url, mListener);
        }

        static void addListener(String url, GlideProgressLoaderListener progressLoaderListener) {
            mListenerHashMap.put(clean(url), progressLoaderListener);
        }

        static void removeListener(String url) {
            mListenerHashMap.remove(url);
        }

        static String clean(String url) {
            String result = url;
            if (StringUtil.noEmpty(url)) {
                StringBuilder builder = new StringBuilder(url);
                int start = builder.indexOf(":");
                if (start > 0) {
                    start = builder.indexOf(":", ++start);
                    int end = builder.indexOf("/", start);
                    if (start >= 0 && end > 0) {
                        String port = builder.substring(start, end);
                        if (TextUtils.equals(port, ":80") || TextUtils.equals(port, ":443")) {
                            result = builder.replace(start, end, "").toString();
                        }
                    }
                }
            }
            return result;
        }

        static GlideProgressLoaderListener wrapDownloadListener(final String url, final GlideProgressLoaderListener target) {
            return new GlideProgressLoaderListener() {
                int mProgress = 0;
                private Runnable mRunnable;

                @Override
                public void onStart() {
                    if (target != null) {
                        target.onStart();
                    }
                }

                @Override
                public void onProgress(int progress) {
                    if (target != null) {
                        if (mRunnable == null) {
                            mRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    target.onProgress(mProgress);
                                }
                            };
                        }
                        if (mProgress != progress) {
                            mProgress = progress;
                            mHandler.post(mRunnable);
                        }
                    }
                    if (progress >= 100) {
                        removeListener(url);
                    }
                }

                @Override
                public void onFinish(String savePath) {
                    if (target != null) {
                        target.onFinish(savePath);
                    }
                    removeListener(url);
                }

                @Override
                public void onFailure(DownLoadErrorException e) {
                    if (target != null) {
                        target.onFailure(e);
                    }
                    removeListener(url);
                }

                @Override
                public void onCancel() {
                    if (target != null) {
                        target.onCancel();
                    }
                    removeListener(url);
                }
            };
        }

    }


    //
//    @Override
//    public void applyOptions(Context context, GlideBuilder builder) {
//        //磁盘缓存
//        builder.setDiskCache(new DiskLruCacheFactory(context.getCacheDir().getAbsolutePath(), 50 * 1024 * 1024));
//        KLog.d("Glide", "glide cache file path  >>>  " + context.getCacheDir().getAbsolutePath());
//        //内存缓存
//        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).build();
//        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
//        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
//        //设置比默认大小大1.5倍的缓存和图片池大小
//        int customMemoryCacheSize = (int) (1.5 * defaultMemoryCacheSize);
//        int customBitmapPoolSize = defaultBitmapPoolSize;
//
//        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
//        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
//
//
//        KLog.d("Glide", "bitmapPoolSize >>>>>   " +
//                formatFileSize(context, customBitmapPoolSize) +
//                " / memorySize>>>>>>>>   " +
//                formatFileSize(context, customMemoryCacheSize));
//
//        builder.setLogLevel(Log.ERROR);
//    }

}
