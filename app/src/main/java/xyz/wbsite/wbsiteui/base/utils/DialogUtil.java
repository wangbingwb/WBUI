package xyz.wbsite.wbsiteui.base.utils;

import android.content.Context;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;


public class DialogUtil {

    /**
     * 展示一个需要确认的消息提示
     *
     * @param context
     * @param title   标题
     * @param message 消息内容
     */
    public static void showConfirmMessage(Context context, String title, String message) {
        QMUIDialog qmuiDialog = new QMUIDialog.MessageDialogBuilder(context)
                .setMessage(message)
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .create(com.qmuiteam.qmui.R.style.QMUI_Dialog);
        qmuiDialog.show();
    }

    /**
     * 展示一个需要确认的消息提示(带回调)
     *
     * @param context
     * @param title   标题
     * @param message 消息内容
     */
    public static void showConfirmMessage(Context context, String title, String message, QMUIDialogAction.ActionListener actionListener) {
        QMUIDialog qmuiDialog = new QMUIDialog.MessageDialogBuilder(context)
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

    /**
     * 展示一个信息弹窗
     *
     * @param context
     * @param title   标题
     * @param message 消息内容
     */
    public static void showMessage(Context context, String title, String message) {
        new QMUIDialog.MessageDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .addAction("确认", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
    }
}