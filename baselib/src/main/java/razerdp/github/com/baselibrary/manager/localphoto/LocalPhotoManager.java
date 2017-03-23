package razerdp.github.com.baselibrary.manager.localphoto;

import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import com.socks.library.KLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import razerdp.github.com.baselibrary.base.AppContext;
import razerdp.github.com.baselibrary.manager.ThreadPoolManager;
import razerdp.github.com.baselibrary.thirdpart.WeakHandler;

/**
 * Created by 大灯泡 on 2017/3/23.
 * <p>
 * 本地照片管理类(单例)
 */

public enum LocalPhotoManager {
    INSTANCE;

    boolean isScaning;
    long lastScanTime;


    //访问系统数据库的大图查询数据
    public static final String[] STORE_IMGS = {MediaStore.Images.ImageColumns._ID,
                                               MediaStore.Images.ImageColumns.DATA,
                                               MediaStore.Images.ImageColumns.ORIENTATION,
                                               MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                                               MediaStore.Images.ImageColumns.DATE_TAKEN};
    //访问系统数据库的小图查询数据
    public static final String[] THUMBNAIL_STORE_IMAGE = {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.DATA};

    private static final LinkedHashMap<String, List<ImageInfo>> sALBUM = new LinkedHashMap<>();

    private final ProgressRunnable progressRunnable = new ProgressRunnable();

    private boolean isAsync;


    public synchronized void scanImgAsync(@Nullable final OnScanListener listener) {
        ThreadPoolManager.execute(new Runnable() {
            @Override
            public void run() {
                isAsync = true;
                scanImg(listener);
            }
        });
    }

    public synchronized void scanImg(@Nullable OnScanListener listener) {
        if (isScaning) return;
        isScaning = true;
        callStart(listener);
        lastScanTime = System.currentTimeMillis();

        final boolean isProgressListener = listener instanceof OnScanProgresslistener;

        Cursor cursor = AppContext.getAppContext()
                                  .getContentResolver()
                                  .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                         STORE_IMGS,
                                         null,
                                         null,
                                         MediaStore.Images.ImageColumns.DATE_TAKEN.concat(" DESC"));
        if (cursor == null) {
            callError(listener, "cursor为空", null);
            isScaning = false;
            return;
        }
        //构造thumb的查询语句(where id = xxx)
        final String[] thumbWhereQuery = new String[1];
        List<ImageInfo> allImageInfoLists = new ArrayList<>();
        final int cursorCount = cursor.getCount();
        sALBUM.put("所有图片", allImageInfoLists);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            int imgId = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
            String imgPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
            int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
            String albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));
            long dateTaken = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN));

            if (!new File(imgPath).exists()) continue;

            // TODO: 2017/3/23 查询thumb
            thumbWhereQuery[0] = String.valueOf(imgId);
            String thumbImgPath = getThumbPath(thumbWhereQuery);

            List<ImageInfo> imageInfoList = sALBUM.get(albumName);
            ImageInfo imageInfo = new ImageInfo(imgPath, thumbImgPath, albumName, dateTaken, orientation);
            try {
                allImageInfoLists.add(imageInfo.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                KLog.e(e);
            }
            if (imageInfoList == null) {
                imageInfoList = new ArrayList<>();
                sALBUM.put(albumName, imageInfoList);
            } else {
                imageInfoList.add(imageInfo);
            }
            if (isProgressListener) {
                callProgress((OnScanProgresslistener) listener, isAsync, cursor.getPosition() / cursorCount);
            }
        }
        cursor.close();
        isScaning = false;
        progressRunnable.reset();
        isAsync = false;
        callFinish(listener);
    }

    private String getThumbPath(String[] whereQuery) {
        String result = null;
        //通过大图的id，并且构造cursor的查询语句来获取大图对应的小图
        Cursor cursor = AppContext.getAppContext()
                                  .getContentResolver()
                                  .query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                                         THUMBNAIL_STORE_IMAGE,
                                         MediaStore.Images.Thumbnails.IMAGE_ID.concat(" = ?"),
                                         whereQuery,
                                         null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
        }

        cursor.close();

        return result;
    }

    public LinkedHashMap<String, List<ImageInfo>> getLocalImages() {
        return new LinkedHashMap<>(sALBUM);
    }

    private void callStart(OnScanListener listener) {
        if (listener != null) {
            listener.onStart();
        }
    }

    private void callFinish(OnScanListener listener) {
        if (listener != null) {
            listener.onFinish();
        }
    }

    private void callError(OnScanListener listener, String message, Exception e) {
        if (listener != null) {
            listener.onError(new LPException(message, e));
        }
    }

    private void callProgress(OnScanProgresslistener listener, boolean async, int progress) {
        if (listener != null) {
            if (async) {
                if (progressRunnable.getListener() == null) {
                    progressRunnable.setListener(listener);
                }
                progressRunnable.setProgress(progress);
                handler.post(progressRunnable);
            } else {
                listener.onProgress(progress);
            }
        }
    }


    private WeakHandler handler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });


    public interface OnScanListener {
        void onStart();

        void onFinish();

        void onError(LPException e);
    }

    public abstract class OnScanProgresslistener implements OnScanListener {
        abstract void onProgress(int progress);
    }

    private static class ProgressRunnable implements Runnable {

        int progress;
        OnScanProgresslistener listener;

        public ProgressRunnable() {
            this(null);
        }

        public ProgressRunnable(OnScanProgresslistener listener) {
            progress = 0;
            this.listener = listener;
        }

        public void setListener(OnScanProgresslistener listener) {
            this.listener = listener;
        }

        public OnScanProgresslistener getListener() {
            return listener;
        }

        private void setProgress(int progress) {
            this.progress = progress;
        }

        private void reset() {
            this.progress = 0;
            this.listener = null;
        }


        @Override
        public void run() {
            if (listener != null) {
                listener.onProgress(progress);
            }
        }
    }

    public static class ImageInfo implements Serializable, Cloneable {
        public final String imagePath;
        public final String thumbnailPath;
        public final String albumName;
        public final long time;
        public final int orientation;

        public ImageInfo(String imagePath, String thumbnailPath, String albumName, long time, int orientation) {
            this.imagePath = imagePath;
            this.thumbnailPath = thumbnailPath;
            this.albumName = albumName;
            this.time = time;
            this.orientation = orientation;
        }

        @Override
        protected ImageInfo clone() throws CloneNotSupportedException {
            //因为这里只有一些基础数据，所以就浅复制足够了
            return (ImageInfo) super.clone();
        }

        //深复制，暂时不需要，另外利用流的方法的话，类需要实现Serializable接口
       /* public Object cloneDeepInternal() throws IOException, ClassNotFoundException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            return ois.readObject();
        }*/
    }

}
