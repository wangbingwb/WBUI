package xyz.wbsite.wbsiteui;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;


/**
 * Application 入口。
 */
public class WBUIApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

    }
}
