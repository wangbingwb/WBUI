package xyz.wbsite.wbsiteui.base.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;


public class DialogUtil {

    /**
     * 确认型对话框
     *
     * @param activity
     * @param title
     * @param message
     * @param positive
     */
    public static void showConfirm(Activity activity, String title, String message, QMUIDialogAction.ActionListener positive) {
        new QMUIDialog.MessageDialogBuilder(activity)
                .setTitle(title)
                .setMessage(message)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", positive)
                .create()
                .show();
    }

}
