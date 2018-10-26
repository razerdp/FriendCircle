package razerdp.github.com.lib.manager.compress;

import android.content.Context;
import android.graphics.Bitmap;

import com.socks.library.KLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import razerdp.github.com.lib.helper.AppFileHelper;
import razerdp.github.com.lib.manager.ThreadPoolManager;
import razerdp.github.com.lib.utils.BitmapUtil;
import razerdp.github.com.lib.utils.EncryUtil;
import razerdp.github.com.lib.utils.FileUtil;

/**
 * Created by 大灯泡 on 2018/1/10.
 */
class CompressTaskHelper extends BaseCompressTaskHelper<CompressOption> {

    private List<CompressResult> resultBucket;

    public CompressTaskHelper(Context context, CompressOption options, OnCompressListener compressListener) {
        super(context, options, compressListener);
        resultBucket = new ArrayList<>();
    }

    @Override
    public void start() {
        if (data == null) {
            callError("配置为空");
            return;
        }
        CompressOption option = data;
        int[] imageSize = BitmapUtil.getImageSize(option.originalImagePath);
        if (imageSize[0] <= -1 || imageSize[1] <= -1) {
            callError(">>>>>无法获取图片大小<<<<<");
            return;
        }
        File imageFile = new File(option.originalImagePath);
        if (!imageFile.exists()) {
            callError("图片不存在。。。退出");
            return;
        }
        long fileSize = 0;
        try {
            fileSize = FileUtil.getFileSize(new File(option.originalImagePath));
            KLog.i(TAG, "文件路径  >>>  " + option.originalImagePath + "\n文件大小  >>>  " + FileUtil.fileLengthFormat(fileSize));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fileSize <= 0 && option.compressType != CompressManager.SCALE) {
            callError("获取图片文件大小失败。。。。");
            return;
        }

        startTask(imageSize, fileSize, option);
    }

    private void startTask(final int[] imageSize, final long fileSize, final CompressOption option) {
        ThreadPoolManager.executeOnUploadPool(new Runnable() {
            @Override
            public void run() {
                try {
                    callCompress(0, 1);
                    startInternal(imageSize, fileSize, option);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void startInternal(int[] imageSize, long fileSize, CompressOption option) throws Exception {
        CompressResult result = null;
        switch (option.compressType) {
            case CompressManager.SCALE:
                result = compressOnScale(option.originalImagePath, imageSize, option);
                break;
            case CompressManager.SIZE:
                result = compressOnSize(option.originalImagePath, fileSize, option);
                break;
            case CompressManager.BOTH:
                String savePath = option.saveCompressImagePath;
                option.setSaveCompressImagePath(AppFileHelper.getAppTempPath().concat(EncryUtil.MD5(savePath) + option.suffix));
                CompressResult scaleResult = compressOnScale(option.originalImagePath, imageSize, option);
                if (scaleResult != null) {
                    option.setSaveCompressImagePath(savePath);
                    result = compressOnSize(scaleResult.getCompressedFilePath(), fileSize, option);
                }
                break;
        }
        if (result != null) {
            KLog.i(TAG, "压缩成功，图片地址  >>  " + result.getCompressedFilePath());
            callCompress(1, 1);
            resultBucket.add(result);
            callSuccess(resultBucket);
        } else {
            callError("压缩失败");
        }
    }

    private CompressResult compressOnScale(String imagePath, int[] imageSize, CompressOption option) throws Exception {
        CompressResult result = new CompressResult();
        result.setOriginWidth(imageSize[0])
                .setOriginHeight(imageSize[1]);
        KLog.i(TAG, "压缩前分辨率  >>  【" + imageSize[0] + "x" + imageSize[1] + "】");
        Bitmap bp = BitmapUtil.loadBitmap(mContext, option.originalImagePath, option.maxWidth, option.maxHeight);
        boolean success = BitmapUtil.saveBitmap(option.saveCompressImagePath, bp, option.suffix);
        bp.recycle();
        int[] resultSize = BitmapUtil.getImageSize(option.saveCompressImagePath);
        KLog.i(TAG, "压缩后分辨率  >>  【" + resultSize[0] + "x" + resultSize[1] + "】");
        result.setCompressedFilePath(option.saveCompressImagePath)
                .setCompressedWidth(resultSize[0])
                .setCompressedHeight(resultSize[1]);
        return success ? result : null;
    }

    private CompressResult compressOnSize(String originalImagePath, long fileSize, CompressOption option) {
        CompressResult result = new CompressResult();
        long sizeInKB = fileSize >> 10;
        int[] size = BitmapUtil.getImageSize(option.originalImagePath);
        result.setOriginWidth(size[0])
                .setOriginHeight(size[1]);
        KLog.i(TAG, "压缩前的文件大小  >>  " + FileUtil.fileLengthFormat(fileSize) + "\n当前设置最大文件大小  >>  " + option.sizeTarget + "kb");
        if (option.sizeTarget >= sizeInKB) {
            KLog.i(TAG, "小于理论大小，放弃压缩");
            FileUtil.moveFile(originalImagePath, option.saveCompressImagePath);
            result.setCompressedFilePath(option.saveCompressImagePath)
                    .setCompressedWidth(size[0])
                    .setCompressedHeight(size[1]);
            return result;
        }
        boolean success = BitmapUtil.compressBmpToFile(BitmapUtil.loadBitmap(mContext, originalImagePath),
                new File(option.saveCompressImagePath),
                option.sizeTarget,
                option.suffix);
        size = BitmapUtil.getImageSize(option.saveCompressImagePath);
        result.setCompressedWidth(size[0])
                .setCompressedHeight(size[1]);
        return success ? result : null;
    }
}
