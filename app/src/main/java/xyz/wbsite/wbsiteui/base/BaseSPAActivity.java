package xyz.wbsite.wbsiteui.base;

import android.content.Intent;
import android.os.Bundle;

import com.qmuiteam.qmui.arch.QMUIFragmentActivity;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.Hashtable;


public abstract class BaseSPAActivity extends QMUIFragmentActivity {

    protected QMUITipDialog loading;

    private Hashtable<Integer, IActivityResult> resultHashtable = new Hashtable<>();
    private int mRequestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading = new QMUITipDialog.Builder(this).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING).setTipWord("加载中...").create();
    }

    public void showLoading() {
        loading.show();
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

    public void startForResult(Intent intent, IActivityResult activityResult) {
        int requestCode = mRequestCode++;
        resultHashtable.put(requestCode, activityResult);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultHashtable.containsKey(requestCode)) {
            IActivityResult i = resultHashtable.remove(requestCode);
            if (i != null) {
                i.onResult(resultCode, data);
            }
        }
    }
}
