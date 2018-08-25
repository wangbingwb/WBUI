package xyz.wbsite.wbsiteui.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;


public class DialogUtil {


    /**
     * 展示一个需要确认的消息提示
     * @param activity
     * @param title 标题
     * @param message 消息内容
     */
    public static void showConfirmMessage(Activity activity, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确认",null);
        builder.show();
    }
    /**
     * 展示一个需要确认的消息提示(带回调)
     * @param activity
     * @param title 标题
     * @param message 消息内容
     */
    public static void showConfirmMessage(Activity activity, String title, String message, DialogInterface.OnClickListener onClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确认", onClickListener);
        builder.show();
    }
}
