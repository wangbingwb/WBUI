package xyz.wbsite.wbsiteui.utils;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


public class App extends Application implements Thread.UncaughtExceptionHandler {
    private static App instance;
    private DataBaseUtils db;
    private Map<String, Object> map;
    private Toast toast;
    protected Handler handler = new Handler();

    public Handler getHandler() {
        return handler;
    }

    public static App getInstance() {
        return instance;
    }

    public static Map getMap() {
        return instance.map;
    }

    public DataBaseUtils getDb() {
        return db;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initExceptionHandler();
        initDB();
        initMap();
    }

    private void initDB() {
        db = new DataBaseUtils(this);
    }

    private void initMap() {
        map = new HashMap();
    }

    private void initExceptionHandler() {
        // 程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    // 判断是否安装插件
    public boolean hasInstallPlugin(String packageName) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(packageName, 0);
            return (info != null);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

    /**
     * 展示Toast消息。
     *
     * @param message 消息内容
     */
    public synchronized void showToast(final String message) {
        if (toast == null) {
            toast = Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT);
        }
        if (!TextUtils.isEmpty(message)) {
            toast.setText(message);
            toast.show();
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.e("error--->", "error : ", throwable);
    }
}

