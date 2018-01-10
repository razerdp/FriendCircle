package razerdp.github.com.lib.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import com.socks.library.KLog;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.text.DecimalFormat;

import razerdp.github.com.lib.api.AppContext;

public class FileUtil {

    private static final int IO_BUFFER_SIZE = 1024;

    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断文件是否可读可写
     */
    public static boolean isFileCanReadAndWrite(String filePath) {
        if (null != filePath && filePath.length() > 0) {
            File f = new File(filePath);
            if (null != f && f.exists()) {
                return f.canRead() && f.canWrite();
            }
        }
        return false;
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }


    /**
     * 得到除去文件名部分的路径 实际上就是路径中的最后一个路径分隔符前的部分。
     */
    public static String getNameDelLastPath(String fileName) {
        int point = getPathLastIndex(fileName);
        if (point == -1) {
            return fileName;
        } else {
            return fileName.substring(0, point);
        }
    }

    /**
     * 得到路径分隔符在文件路径中最后出现的位置。 对于DOS或者UNIX风格的分隔符都可以。
     *
     * @param fileName 文件路径
     * @return 路径分隔符在路径中最后出现的位置，没有出现时返回-1。
     * @since 0.5
     */
    public static int getPathLastIndex(String fileName) {
        int point = fileName.lastIndexOf('/');
        if (point == -1) {
            point = fileName.lastIndexOf('\\');
        }
        return point;
    }

    /**
     * 如果文件末尾有了"/"则判断是否有多个"/"，是则保留一个，没有则添加
     *
     * @param path
     * @return
     */
    public static String checkFileSeparator(String path) {
        if (!TextUtils.isEmpty(path)) {
            if (!path.endsWith(File.separator)) {
                return path.concat(File.separator);
            } else {
                final int sourceStringLength = path.length();
                int index = sourceStringLength;
                if (index >= 0) {
                    while (index >= 0) {
                        index--;
                        if (path.charAt(index) != File.separatorChar) {
                            break;
                        }
                    }
                }
                if (index < sourceStringLength) {
                    path = path.substring(0, index + 1);
                    return path.concat(File.separator);
                }
            }
        }
        return path;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径
     * @param newPath String 复制后路径
     * @return boolean
     */
    public static boolean copyFile(String oldPath, String newPath) {
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[IO_BUFFER_SIZE];

                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        } catch (Exception e) {
            KLog.e(e);
            return false;
        }
        return true;
    }

    /**
     * 写入文件
     *
     * @param strFileName 文件名
     * @param ins         流
     */
    public static void writeToFile(String strFileName, InputStream ins) {
        try {
            File file = new File(strFileName);

            FileOutputStream fouts = new FileOutputStream(file);
            int len;
            int maxSize = 1024 * 1024;
            byte buf[] = new byte[maxSize];
            while ((len = ins.read(buf, 0, maxSize)) != -1) {
                fouts.write(buf, 0, len);
                fouts.flush();
            }

            fouts.close();
        } catch (IOException e) {
            KLog.e(e);
        }
    }

    /**
     * 写入文件
     *
     * @param strFileName 文件名
     * @param bytes       bytes
     */
    public static boolean writeToFile(String strFileName, byte[] bytes) {
        try {
            File file = new File(strFileName);

            FileOutputStream fouts = new FileOutputStream(file);
            fouts.write(bytes, 0, bytes.length);
            fouts.flush();
            fouts.close();
            return true;
        } catch (IOException e) {
            KLog.e(e);
        }
        return false;
    }

    /**
     * Prints some data to a file using a BufferedWriter
     */
    public static boolean writeToFile(String filename, String data) {
        BufferedWriter bufferedWriter = null;
        try {
            // Construct the BufferedWriter object
            bufferedWriter = new BufferedWriter(new FileWriter(filename));
            // Start writing to the output stream
            bufferedWriter.write(data);
            return true;
        } catch (FileNotFoundException e) {
            KLog.e(e);
        } catch (IOException e) {
            KLog.e(e);
        } finally {
            // Close the BufferedWriter
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                KLog.e(e);
            }
        }
        return false;
    }

    public static String Read(String fileName) {
        String res = "";
        try {
            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
        } catch (Exception e) {
            KLog.e(e);
        }
        return res;
    }

    public static void Write(String fileName, String message) {

        try {
            FileOutputStream outSTr = null;
            try {
                outSTr = new FileOutputStream(new File(fileName));
            } catch (FileNotFoundException e) {
                KLog.e(e);
            }
            BufferedOutputStream Buff = new BufferedOutputStream(outSTr);
            byte[] bs = message.getBytes();
            Buff.write(bs);
            Buff.flush();
            Buff.close();
        } catch (MalformedURLException e) {
            KLog.e(e);
        } catch (IOException e) {
            KLog.e(e);
        }
    }

    public static void Write(String fileName, String message, boolean append) {
        try {
            FileOutputStream outSTr = null;
            try {
                outSTr = new FileOutputStream(new File(fileName), append);
            } catch (FileNotFoundException e) {
                KLog.e(e);
            }
            BufferedOutputStream Buff = new BufferedOutputStream(outSTr);
            byte[] bs = message.getBytes();
            Buff.write(bs);
            Buff.flush();
            Buff.close();
        } catch (MalformedURLException e) {
            KLog.e(e);
        } catch (IOException e) {
            KLog.e(e);
        }
    }

    /**
     * 删除文件 删除文件夹里面的所有文件
     *
     * @param path String 路径
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {// 如果是文件，则删除文件
            file.delete();
            return;
        }
        File files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                deleteFile(files[i].getAbsolutePath());// 先删除文件夹里面的文件
            }
            files[i].delete();
        }
        file.delete();
    }

    /**
     * 删除文件 删除文件夹里面的所有文件
     * <p>
     * (此方法和deleteFile(String path)这个方法总体是一样的，只是删除代码部分用的是先改名再删除的方法删除的，为了避免EBUSY (Device or resource busy)的错误)
     *
     * @param path String 路径
     */
    public static void deleteFileSafely(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {// 如果是文件，则删除文件
            safelyDelete(file);
            return;
        }
        File files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                deleteFileSafely(files[i].getAbsolutePath());// 先删除文件夹里面的文件
            }
            safelyDelete(files[i]);
        }
        safelyDelete(file);
    }

    /**
     * 先改名，在删除（为了避免EBUSY (Device or resource busy)的错误）
     */
    private static void safelyDelete(File file) {
        if (file == null || !file.exists()) return;
        try {
            final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
            file.renameTo(to);
            to.delete();
        } catch (Exception e) {
            KLog.e(e);
        }
    }

    /**
     * 文件大小
     *
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (!file.exists()) {
            return size;
        }
        if (!file.isDirectory()) {
            size = file.length();
        } else {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFileSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        }
        return size;
    }

    /**
     * @return 文件的大小，带单位(MB、KB等)
     */
    public static String getFileLength(String filePath) {
        try {
            File file = new File(filePath);
            return fileLengthFormat(getFileSize(file));
        } catch (Exception e) {
            KLog.e(e);
            return "";
        }
    }

    /**
     * @return 文件的大小，带单位(MB、KB等)
     */
    public static String fileLengthFormat(long length) {
        String lenStr = "";
        DecimalFormat formater = new DecimalFormat("#0.##");
        if (length > 0 && length < 1024) {
            lenStr = formater.format(length) + " Byte";
        } else if (length < 1024 * 1024) {
            lenStr = formater.format(length / 1024.0f) + " KB";
        } else if (length < 1024 * 1024 * 1024) {
            lenStr = formater.format(length / (1024 * 1024.0f)) + " MB";
        } else {
            lenStr = formater.format(length / (1024 * 1024 * 1024.0f)) + " GB";
        }
        return lenStr;
    }

    /**
     * 得到文件的类型。 实际上就是得到文件名中最后一个“.”后面的部分。
     *
     * @param fileName 文件名
     * @return 文件名中的类型部分
     */
    public static String pathExtension(String fileName) {
        int point = fileName.lastIndexOf('.');
        int length = fileName.length();
        if (point == -1 || point == length - 1) {
            return "";
        } else {
            return fileName.substring(point, length);
        }
    }

    /**
     * 调用系统打开文件
     */
    public static boolean openFile(File file, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        // 获取文件file的MIME类型
        String type = getMIMEType(file);
        // 设置intent的data和Type属性。
        intent.setDataAndType(Uri.fromFile(file), type);
        // 跳转
        try {
            ((Activity) context).startActivity(intent);
        } catch (ActivityNotFoundException e) {
            KLog.e(e);
            return false;
        }
        return true;
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     */
    @SuppressLint("DefaultLocale")
    public static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") {
            return type;
        }
        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0])) {
                type = MIME_MapTable[i][1];
            }
        }
        return type;
    }

    public static void moveFile(String oldPath, String newPath) {
        copyFile(oldPath, newPath);
        delFile(oldPath);
    }

    public static void delFile(String filePathAndName) {
        try {
            File myDelFile = new File(filePathAndName);
            myDelFile.delete();
        } catch (Exception e) {
            System.out.println("删除文件操作出错");
            e.printStackTrace();
        }
    }

    private static final String[][] MIME_MapTable = {
            // {后缀名， MIME类型}
            {".doc", "application/msword"}, {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"}, {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".pdf", "application/pdf"}, {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"}, {".txt", "text/plain"},
            {".wps", "application/vnd.ms-works"}, {"", "*/*"}
    };


    /**
     * 删除指定目录下文件及目录
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) throws IOException {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);

            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFolderFile(files[i].getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {
                    file.delete();
                } else {
                    if (file.listFiles().length == 0) {
                        file.delete();
                    }
                }
            }
        }
    }

    public static String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader =
                    new InputStreamReader(AppContext.getResources().getAssets().open(fileName), "UTF-8");
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null) Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static InputStream getAssetsInputStream(String fileName) {
        try {
            return AppContext.getResources().getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 得到内置或外置SD卡的路径
     *
     * @param mContext
     * @param isExSD   true=外置SD卡
     * @return
     */
    public static String getStoragePath(Context mContext, boolean isExSD) {
        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (isExSD == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


}
