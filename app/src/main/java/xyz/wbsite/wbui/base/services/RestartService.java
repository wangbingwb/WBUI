package xyz.wbsite.wbui.base.services;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class RestartService extends Service {

    /**
     * 重启整个APP
     *
     * @param context
     * @param Delayed 延迟多少毫秒
     */
    public static void restartAPP(Context context, long Delayed) {

        // 开启服务，用来重启
        Intent intent1 = new Intent(context, RestartService.class);
        intent1.putExtra("PackageName", context.getPackageName());
        intent1.putExtra("Delayed", Delayed);
        context.startService(intent1);

        // 杀死进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 应用关闭重启时间
     */
    private static long stopDelayed = 1000;

    private Handler handler;

    private String PackageName;

    public RestartService() {
        handler = new Handler();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        stopDelayed = intent.getLongExtra("Delayed", 1000);
        PackageName = intent.getStringExtra("PackageName");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(PackageName);
                startActivity(LaunchIntent);
                RestartService.this.stopSelf();
            }
        }, stopDelayed);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}