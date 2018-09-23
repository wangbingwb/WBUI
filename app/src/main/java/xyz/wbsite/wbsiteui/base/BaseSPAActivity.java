package xyz.wbsite.wbsiteui.base;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.qmuiteam.qmui.arch.QMUIFragmentActivity;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.Hashtable;

import xyz.wbsite.wbsiteui.WBUIApplication;
import xyz.wbsite.wbsiteui.utils.AnimationUtil;


public abstract class BaseSPAActivity extends QMUIFragmentActivity {
    protected Handler handler = new Handler();
    protected LinearLayout layout;
    protected FrameLayout content;

    private QMUITopBar topBar;
    protected QMUITipDialog loading;

    private Hashtable<Integer, IResultListerner> mResultListerner = new Hashtable<>();
    private int mRequestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading = new QMUITipDialog.Builder(this).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING).setTipWord("加载中...").create();
    }


    public void showLoading() {
        loading.show();
    }

    public Handler getHandler() {
        return handler;
    }

    public void dismissLoading() {
        loading.dismiss();
    }


    public synchronized void confirmDialog(String messgae, QMUIDialogAction.ActionListener actionListener) {
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("消息")
                .setMessage(messgae)
                .addAction("确定", actionListener)
                .setCanceledOnTouchOutside(false)
                .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
    }

    /**
     * 引起注意或聚焦的消息提示
     *
     * @param message
     * @param view
     */
    public void showError(String message, View view) {
        showToast(message);
        if (view != null) {
            view.requestFocus();
            view.startAnimation(AnimationUtil.getShakeAnimation(3));
        }
    }

    /**
     * 展示Toast消息。
     *
     * @param message 消息内容
     */
    public void showToast(String message) {
        WBUIApplication.getInstance().showToast(message);
    }

    public interface IResultListerner {
        void onResult(int resultCode, Intent data);
    }

    public void startForResult(Intent intent, IResultListerner listerner) {
        int requestCode = mRequestCode++;
        mResultListerner.put(requestCode, listerner);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mResultListerner.containsKey(requestCode)) {
            IResultListerner l = mResultListerner.remove(requestCode);
            if (l != null) {
                l.onResult(resultCode, data);
            }
        }
    }

    /**
     * 执行一个后台任务
     */
    public void execTask(Task task) {
        task.execute();
    }

    public static abstract class Task extends AsyncTask<Void, String, Boolean> {

        protected abstract boolean run();

        protected void post(boolean result) {
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                return run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            post(result == null ? false : result);
        }
    }
}
