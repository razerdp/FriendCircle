package razerdp.friendcircle.app.download;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.socks.library.KLog;

import java.io.File;

import razerdp.github.com.lib.utils.FileUtil;


public class Downloader {
    public static long download(Context context, String url, String path, String mimeType) {
        KLog.i("url  >>  " + url + "\n paht  >>  " + path);
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(url));
        String fileName = FileUtil.getFileName(path);
        Uri uri = Uri.fromFile(new File(path));
        Log.i("downloader", "下载路径 = " + uri.getPath());
        req.setDestinationUri(uri);


        // 设置一些基本显示信息
        req.setTitle(fileName);
//        req.setDescription("下载完后请点击打开");
        req.setMimeType(mimeType);

        // Ok go!
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        return dm.enqueue(req);
    }

    public static int remove(Context context, long id) {
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        return dm.remove(id);
    }

    public interface DownloadListener {
        void onProgress(int progress);

        void onSuccess();

        void onFail(String message);
    }

    public static void downloadFile(Context context, String url, String path, DownloadListener listener) {
    }
}
