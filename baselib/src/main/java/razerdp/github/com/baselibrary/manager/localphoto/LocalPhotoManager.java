package razerdp.github.com.baselibrary.manager.localphoto;

import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import razerdp.github.com.baselibrary.base.AppContext;

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


    public synchronized void scanImg(OnScanListener listener) {
        if (isScaning) return;
        isScaning = true;
        callStart(listener);
        lastScanTime = System.currentTimeMillis();

        Cursor cursor = AppContext.getAppContext()
                                  .getContentResolver()
                                  .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                         STORE_IMGS,
                                         null,
                                         null,
                                         MediaStore.Images.ImageColumns.DATE_TAKEN + " ASC");
        if (cursor == null) {
            callError(listener, "cursor为空", null);
            isScaning = false;
            return;
        }

        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            int imgId = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
            String imgPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
            int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
            String bucketName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));
            long dateTaken = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN));

            // TODO: 2017/3/23 查询thumb

        }


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


    public interface OnScanListener {
        void onStart();

        void onFinish();

        void onError(LPException e);
    }

    static class ImageInfo {
        public final String imagePath;
        public final String thumbnailPath;
        public final long time;
        public final int orientation;

        public ImageInfo(String imagePath, String thumbnailPath, long time, int orientation) {
            this.imagePath = imagePath;
            this.thumbnailPath = thumbnailPath;
            this.time = time;
            this.orientation = orientation;
        }
    }


    static class AlbumInfo {
        public final String name;
        public final List<ImageInfo> images;

        public AlbumInfo(String name) {
            this.name = name;
            images = new ArrayList<>();
        }

        public List<ImageInfo> getImages() {
            return images;
        }

        public void addImages(ImageInfo info) {
            this.images.add(info);
        }

        public void setImages(List<ImageInfo> infos) {
            if (infos != null) {
                images.clear();
                images.addAll(infos);
            }

        }
    }

}
