package xyz.wbsite.wbsiteui.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.qmuiteam.qmui.arch.QMUIFragmentActivity;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Hashtable;

import xyz.wbsite.wbsiteui.R;
import xyz.wbsite.wbsiteui.utils.App;
import xyz.wbsite.wbsiteui.utils.PermissionUtil;


public abstract class BaseActivity extends QMUIFragmentActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
//    @Override
//    protected int getContextViewId() {
//        return R.layout.activity_main;
//    }

    //    protected Handler handler = new Handler();
//    protected PermissionUtil permissionUtil;
//    protected App app;
//    protected LinearLayout layout;
//    protected FrameLayout content;
//    private Toast toast;
//    private QMUITopBar topBar;
//    protected QMUITipDialog loading;
//    protected QMUITipDialog dialog;
//    private Hashtable<Integer, IResultListerner> mResultListerner = new Hashtable<>();
//    private int mRequestCode = 1;
//
//    protected void init() {
//    }
//
//    @Override
//    public void setContentView(View view) {
//        content.addView(view);
//        inject();
//        init();
//    }
//
//    @Override
//    public void setContentView(int layoutResID) {
//        View inflate = LayoutInflater.from(this).inflate(layoutResID, null);
//        setContentView(inflate);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //禁止横屏
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        //设置基本布局
//        layout = new LinearLayout(this);
//        layout.setOrientation(LinearLayout.VERTICAL);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        content = new FrameLayout(this);
//        layout.addView(content);
//
//        try {
//            this.setContentView(layout, layoutParams);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        app = App.getInstance();
//
//        loading = new QMUITipDialog.Builder(this).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING).setTipWord("加载中...").create();
//    }
//
//    private void initTopBar() {
//        if (topBar == null) {
//            topBar = new QMUITopBar(this);
//            topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    finish();
//                }
//            });
//            this.layout.addView(topBar, 0);
//        }
//    }
//
//    public void setTitle(String title) {
//        initTopBar();
//        topBar.setTitle(title);
//    }
//
//    public QMUITopBar getTopBar() {
//        initTopBar();
//        return topBar;
//    }
//
//    public void hideTopBar() {
//        if (topBar != null) {
//            topBar.setVisibility(View.INVISIBLE);
//        }
//    }
//
//    public void showLoading() {
//        loading.show();
//    }
//
//    public Handler getHandler() {
//        return handler;
//    }
//
//    public void dismissLoading() {
//        loading.dismiss();
//    }
//
//    /**
//     * 注入绑定注解
//     */
//    @Target(ElementType.FIELD)
//    @Retention(RetentionPolicy.RUNTIME)
//    protected @interface Bind {
//        int value() default -1;
//    }
//
//    /**
//     * 自动注入解析
//     */
//    private void inject() {
//        Class Class = this.getClass();
//        Field[] fields = Class.getDeclaredFields();
//        for (Field f : fields) {
//            if (f.isAnnotationPresent(Bind.class)) {
//                Bind bind = f.getAnnotation(Bind.class);
//                int id = bind.value();
//                if (id > 0) {
//                    f.setAccessible(true);
//                    try {
//                        f.set(this, this.content.findViewById(id));
//                    } catch (IllegalAccessException e) {
//                        Log.e("Bind", f.getName() + "注入失败!");
//                        e.printStackTrace();
//                    } finally {
//                        f.setAccessible(false);
//                    }
//                }
//            }
//        }
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (permissionUtil != null) {
//            permissionUtil.requestPermissionsResult(requestCode, permissions, grantResults);
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    /**
//     * 展示Toast消息。
//     *
//     * @param message 消息内容
//     */
//    public synchronized void showToast(String message) {
//        if (toast == null) {
//            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
//        }
//        if (!TextUtils.isEmpty(message)) {
//            toast.setText(message);
//            toast.show();
//        }
//    }
//
//    /**
//     * 引起注意或聚焦的消息提示
//     *
//     * @param message
//     * @param view
//     */
//    public void showError(String message, View view) {
//        showToast(message);
//        view.requestFocus();
//        view.startAnimation(shakeAnimation(3));
//    }
//
//    public interface IResultListerner {
//        void onResult(int resultCode, Intent data);
//    }
//
//    public void startForResult(Intent intent, IResultListerner listerner) {
//        int requestCode = mRequestCode++;
//        mResultListerner.put(requestCode, listerner);
//        startActivityForResult(intent, requestCode);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (mResultListerner.containsKey(requestCode)) {
//            IResultListerner l = mResultListerner.remove(requestCode);
//            if (l != null) {
//                l.onResult(resultCode, data);
//            }
//        }
//    }
//
//    /**
//     * 执行一个后台任务
//     */
//    public void execTask(Task task) {
//        task.execute();
//    }
//
//    public static abstract class Task extends AsyncTask<Void, String, Void> {
//
//        protected abstract void run();
//
//        protected void post() {
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try {
//                run();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void voids) {
//            post();
//        }
//    }
//
//    /**
//     * 晃动动画
//     *
//     * @param counts 半秒钟晃动多少下
//     * @return
//     */
//    public static Animation shakeAnimation(int counts) {
//        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
//        translateAnimation.setInterpolator(new CycleInterpolator(counts));
//        translateAnimation.setDuration(500);
//        return translateAnimation;
//    }
}
