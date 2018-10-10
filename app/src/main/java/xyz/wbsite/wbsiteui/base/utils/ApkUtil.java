package xyz.wbsite.wbsiteui.base.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ApkUtil {

    /**
     * 安装assets中的APK文件
     *
     * @param context
     * @param fileName
     * @param tempDir
     */
    public static void installAssetsApk(Context context, String fileName, File tempDir) {

        boolean b = copyAssetsFile(context, fileName, tempDir);
        if (b) {
            File apkFile = new File(tempDir, fileName);
            AndPermission.with(context)
                    .install()
                    .file(apkFile)
                    .onGranted(new Action<File>() {
                        @Override
                        public void onAction(File data) {

                        }
                    })
                    .onDenied(new Action<File>() {
                        @Override
                        public void onAction(File data) {

                        }
                    })
                    .start();
        }
    }

    /**
     * 复制文件到SD卡
     * * @param context
     * * @param fileName 复制的文件名
     * * @param path  保存的目录路径
     * * @return
     */
    private static boolean copyAssetsFile(Context context, String fileName, File directory) {
        try {
            InputStream mInputStream = context.getAssets().open(fileName);
            if (!directory.exists()) {
                directory.mkdir();
            }
            File mFile = new File(directory, fileName);
            if (!mFile.exists()) mFile.createNewFile();
            FileOutputStream mFileOutputStream = new FileOutputStream(mFile);
            byte[] mbyte = new byte[1024];
            int i = 0;
            while ((i = mInputStream.read(mbyte)) > 0) {
                mFileOutputStream.write(mbyte, 0, i);
            }
            mInputStream.close();
            mFileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // 判断是否安装插件
    public static boolean hasInstallPlugin(Context context, String packageName) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            return (info != null);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }
}
