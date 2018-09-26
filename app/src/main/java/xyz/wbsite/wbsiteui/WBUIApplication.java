package xyz.wbsite.wbsiteui;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.squareup.leakcanary.LeakCanary;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import xyz.wbsite.wbsiteui.base.utils.DataBaseUtil;


/**
 * Application 入口。
 */
public class WBUIApplication extends Application implements Thread.UncaughtExceptionHandler {

    private static WBUIApplication instance;
    private WeakReference<Activity> currentActivity;

    private DataBaseUtil db;
    private Map<String, Object> map;
    private Location mLocation;

    private Toast toast;
    protected Handler handler = new Handler();

    public Location getLocation() {
        return mLocation;
    }

    public Handler getHandler() {
        return handler;
    }

    public static WBUIApplication getInstance() {
        return instance;
    }

    public static Map getMap() {
        return instance.map;
    }

    public DataBaseUtil getDb() {
        return db;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        instance = this;
        initExceptionHandler();
        initMap();
        initLife();
    }

    public void lozyInit() {
        initDB();
    }

    private void initDB() {
        db = new DataBaseUtil(this);
    }

    private void initMap() {
        map = new HashMap();
    }

    private void initLife() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                currentActivity = new WeakReference<Activity>(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
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
            toast = new Toast(this);
            toast.setGravity(Gravity.CENTER, 0, 0);
            TextView textView = new TextView(this);
            textView.setTextColor(Color.parseColor("#ffffff"));
            int padding = QMUIDisplayHelper.dp2px(this, 20);
            textView.setPadding(padding, padding, padding, padding);
            textView.setBackgroundColor(Color.parseColor("#aa131313"));
            toast.setView(textView);
        }
        ((TextView) toast.getView()).setText(message);
        toast.show();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.e("error--->", "error : ", throwable);
        exit();
    }

    public void confirm(String message, QMUIDialogAction.ActionListener actionListener) {
        if (currentActivity != null) {
            Activity activity = currentActivity.get();
            QMUIDialog qmuiDialog = new QMUIDialog.MessageDialogBuilder(activity)
                    .setMessage(message)
                    .addAction("取消", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                        }
                    })
                    .addAction("确定", actionListener)
                    .create(com.qmuiteam.qmui.R.style.QMUI_Dialog);
            qmuiDialog.show();
        }
    }

    public void exit() {
        confirm("确认退出?", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

}
