package razerdp.github.com.lib.manager.compress;

import android.content.Context;
import android.graphics.Bitmap;

import com.socks.library.KLog;

import java.io.File;

import razerdp.github.com.lib.helper.AppFileHelper;
import razerdp.github.com.lib.manager.ThreadPoolManager;
import razerdp.github.com.lib.utils.BitmapUtil;
import razerdp.github.com.lib.utils.EncryUtil;
import razerdp.github.com.lib.utils.FileUtil;
import razerdp.github.com.lib.utils.StringUtil;

/**
 * Created by 大灯泡 on 2018/1/10.
 */
public class CompressTaskHelper extends BaseCompressTaskHelper<CompressOption> {


    public CompressTaskHelper(Context context, CompressOption options, BaseCompressListener baseCompressListener) {
        super(context, options, baseCompressListener);
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
        String targetImagePath = null;
        switch (option.compressType) {
            case CompressManager.SCALE:
                targetImagePath = compressOnScale(option.originalImagePath, imageSize, option);
                break;
            case CompressManager.SIZE:
                targetImagePath = compressOnSize(option.originalImagePath, fileSize, option);
                break;
            case CompressManager.BOTH:
                String savePath = option.saveCompressImagePath;
                option.setSaveCompressImagePath(AppFileHelper.getAppTempPath().concat(EncryUtil.MD5(savePath) + option.suffix));
                String scaleTargetPath = compressOnScale(option.originalImagePath, imageSize, option);
                if (StringUtil.noEmpty(scaleTargetPath)) {
                    option.setSaveCompressImagePath(savePath);
                    targetImagePath = compressOnSize(scaleTargetPath, fileSize, option);
                }
                break;
        }
        if (StringUtil.noEmpty(targetImagePath)) {
            KLog.i(TAG, "压缩成功，图片地址  >>  " + targetImagePath);
            callCompress(1, 1);
            callSuccess(targetImagePath);
        } else {
            callError("压缩失败");
        }
    }

    private String compressOnScale(String imagePath, int[] imageSize, CompressOption option) throws Exception {
        KLog.i(TAG, "压缩前分辨率  >>  【" + imageSize[0] + "x" + imageSize[1] + "】");
        Bitmap bp = BitmapUtil.loadBitmap(mContext, option.originalImagePath, option.maxWidth, option.maxHeight);
        boolean save = BitmapUtil.saveBitmap(option.saveCompressImagePath, bp, option.suffix);
        bp.recycle();
        int[] resultSize = BitmapUtil.getImageSize(option.saveCompressImagePath);
        KLog.i(TAG, "压缩后分辨率  >>  【" + resultSize[0] + "x" + resultSize[1] + "】");
        return save ? option.saveCompressImagePath : null;
    }

    private String compressOnSize(String originalImagePath, long fileSize, CompressOption option) {
        long sizeInKB = fileSize >> 10;
        KLog.i(TAG, "压缩前的文件大小  >>  " + FileUtil.fileLengthFormat(fileSize) + "\n当前设置最大文件大小  >>  " + option.sizeTarget + "kb");
        if (option.sizeTarget >= sizeInKB) {
            KLog.i(TAG, "小于理论大小，放弃压缩");
            FileUtil.moveFile(originalImagePath, option.saveCompressImagePath);
            return option.saveCompressImagePath;
        }
        boolean success = BitmapUtil.compressBmpToFile(BitmapUtil.loadBitmap(mContext, originalImagePath),
                new File(option.saveCompressImagePath),
                option.sizeTarget,
                option.suffix);
        return success ? option.saveCompressImagePath : null;
    }
}
