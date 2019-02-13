package xyz.wbsite.wbsiteui.base.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

    // 判断是APP
    public static boolean hasInstall(Context context, String packageName) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            return (info != null);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

    /**
     * 通过包名启动第三方app
     *
     * @param context
     * @param packageName
     */
    public static void startThridApp(Context context, String packageName) {
        try {
            Intent minIntent = context.getPackageManager()
                    .getLaunchIntentForPackage(packageName);
            context.startActivity(minIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询手机内非系统应用
     *
     * @param context
     * @return
     */
    public static List<PackageInfo> getAllApps(Context context) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                // customs applications
                apps.add(pak);
            }
        }
        return apps;
    }

    /**
     * 查询手机内所有支持分享的应用
     *
     * @param context
     * @return
     */
    public static List<ResolveInfo> getShareApps(Context context) {
        List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("text/plain");
        PackageManager pManager = context.getPackageManager();
        mApps = pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);

        return mApps;
    }

    /**
     * 已知包名和类名启动应用程序
     *
     * @param context
     * @param packageName
     * @param className
     */
    public static void startThridApp(Context context, String packageName, String className) {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 已知第三方应用的包名和指定类的action启动，可以启动第三方应用的指定Activity，
     * 并且传递参数，指定Activity必须设置Action；
     *
     * @param context
     * @param packageName
     * @param action
     * @param type
     * @param count
     */
    public static void startThridApp(Context context, String packageName, String action, String type, int count) {
        try {
            Intent mIntent = new Intent();
            mIntent.setPackage(packageName);
            mIntent.setAction(action);//action
            mIntent.putExtra("a", type);
            mIntent.putExtra("c", count);
            context.startActivity(mIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
