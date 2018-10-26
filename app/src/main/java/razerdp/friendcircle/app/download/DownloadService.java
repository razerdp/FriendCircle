package razerdp.friendcircle.app.download;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.socks.library.KLog;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import razerdp.friendcircle.app.manager.EventBusManager;
import razerdp.friendcircle.app.mvp.model.event.DownloadEvent;
import razerdp.github.com.lib.helper.AppFileHelper;
import razerdp.github.com.lib.utils.EncryUtil;
import razerdp.github.com.lib.utils.StringUtil;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class DownloadService extends Service {
    private static final String TAG = "DownloadService";
    private static final String PARAM_URL = "url";
    private static final String PARAM_TYPE = "type";
    private static final String SAVE_PATH = "savePath";

    public static void download(Context context, String url) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(PARAM_URL, url);
        intent.putExtra(PARAM_TYPE, TYPE_DOWNLOAD);
        context.startService(intent);
    }

    public static void download(Context context, String url, String savePath) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(PARAM_URL, url);
        intent.putExtra(PARAM_TYPE, TYPE_DOWNLOAD);
        intent.putExtra(SAVE_PATH, savePath);
        context.startService(intent);
    }

    public static void cancel(Context context, String url) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(PARAM_URL, url);
        intent.putExtra(PARAM_TYPE, TYPE_CANCEL);
        context.startService(intent);
    }

    private static final Uri downloadUri = Uri.parse("content://downloads/my_downloads");
    private static final String MIME_TYPE_APK = "application/vnd.android.package-archive";
    private static final int TYPE_DOWNLOAD = 0;
    private static final int TYPE_CANCEL = 1;
    private Handler handler = new Handler();
    private DownloadManager downloadManager;
    private HashMap<String, Long> downloadMap;
    private DownloadChangeObserver downloadChangeObserver;
    private boolean registerObserver = false;
    private boolean isApk = false;
    private String savePath = null;

    class DownloadChangeObserver extends ContentObserver {

        DownloadChangeObserver() {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            updateView();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downloadMap = new HashMap<>();
        downloadChangeObserver = new DownloadChangeObserver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        reset();
        if (intent != null) {
            String url = intent.getStringExtra(PARAM_URL);
            if (StringUtil.noEmpty(url)) {
                isApk = url.endsWith(".apk") || url.startsWith("http://download.fir.im/");
            }
            this.savePath = intent.getStringExtra(SAVE_PATH);
            int type = intent.getIntExtra(PARAM_TYPE, TYPE_DOWNLOAD);
            if (type == TYPE_DOWNLOAD) {
                if (!TextUtils.isEmpty(url) && !downloadMap.containsKey(url)) {
                    startDownload(url);
                }
            } else if (type == TYPE_CANCEL) {
                if (!TextUtils.isEmpty(url) && downloadMap.containsKey(url)) {
                    cancelDownload(url);
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void reset() {
        isApk = false;
        savePath = null;
    }

    private synchronized void cancelDownload(String url) {
        long downloadId = downloadMap.get(url);
        downloadMap.remove(url);
        if (downloadId >= 0) {
            Downloader.remove(this, downloadId);
        }
        EventBusManager.post(new DownloadEvent(url, -1, 0));
    }

    private synchronized void startDownload(String url) {
        if (!registerObserver) {
            registerObserver = true;
            // 监听下载
            getContentResolver().registerContentObserver(downloadUri, true, downloadChangeObserver);
        }
        String name = EncryUtil.MD5(url.substring(url.lastIndexOf("/") + 1));
        String downSavePath;
        if (StringUtil.noEmpty(savePath)) {
            downSavePath = savePath;
        } else {
            downSavePath = AppFileHelper.getDownloadPath(url) + name;
        }
        KLog.i("保存地址 = " + downSavePath);
        long downloadId = Downloader.download(this, url, downSavePath, isApk ? MIME_TYPE_APK : "application/octet-stream");
        downloadMap.put(url, downloadId);
    }

    public void updateView() {
        Iterator iterator = downloadMap.entrySet().iterator();
        DownloadEvent downloadEvent;
        int status;
        long downloadId;
        String url;
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            downloadId = (Long) entry.getValue();
            url = String.valueOf(entry.getKey());
            downloadEvent = getDownloadState(downloadId, url);
            // 发送广播通知
            EventBusManager.post(downloadEvent);

            status = downloadEvent.getState();
            switch (status) {
                case DownloadManager.STATUS_SUCCESSFUL:
                    if (isApk) {
                        installApkonDownloadSuccess(downloadId, url);
                    } else {
                        downloadMap.remove(url);
                    }
                    break;
            }
        }
    }

    // remove from map and open file
    private void installApkonDownloadSuccess(long downloadId, String url) {
        downloadMap.remove(url);
        String file = getDownloadFile(downloadId);
        if (!TextUtils.isEmpty(file)) {
            KLog.i("文件 = " + file);
            Intent intent = new Intent();
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setAction(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri fileUri = FileProvider.getUriForFile(this, "com.zjzf.shoescircleandroid.FileProvider", new File(URI.create(file)));
                intent.setDataAndType(fileUri, MIME_TYPE_APK);
            } else {
                intent.setDataAndType(Uri.parse(file), MIME_TYPE_APK);
            }
            startActivity(intent);
        }
    }

    private DownloadEvent getDownloadState(long downloadId, String url) {
        int status = -1;
        int mProgress = 0;
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c != null) {
            if (c.moveToFirst()) {
                status = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    case DownloadManager.STATUS_PENDING:
                        mProgress = 0;
                        break;
                    case DownloadManager.STATUS_PAUSED:
                    case DownloadManager.STATUS_RUNNING:
                        int mDownload_so_far = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        int mDownload_all = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        mProgress = (mDownload_so_far * 99) / mDownload_all;
                        break;
                    case DownloadManager.STATUS_SUCCESSFUL:
                        mProgress = 100;
                        break;
                    case DownloadManager.STATUS_FAILED:
                        mProgress = 0;
                        break;
                }
            }
            c.close();
        }
        return new DownloadEvent(url, status, mProgress);
    }

    private String getDownloadFile(long downloadId) {
        String fileUri = null;
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c != null) {
            if (c.moveToFirst()) {
                fileUri = c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
                if (fileUri.startsWith("content")) {
                    String fileName = c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_FILENAME));
                    fileUri = Uri.parse("file://" + fileName).toString();
                }
            }
            c.close();
        }
        return fileUri;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (registerObserver) {
            getContentResolver().unregisterContentObserver(downloadChangeObserver);
        }
        handler.removeCallbacksAndMessages(null);
        downloadMap.clear();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
