package xyz.wbsite.wbui.base.utils;

import android.view.View;

import xyz.wbsite.wbui.WBUIApplication;

/**
 * 消息发生器
 */
public class Toaster {
    /**
     * 展示Toast消息。
     *
     * @param message 消息内容
     */
    public static void showToast(String message) {
        WBUIApplication.getInstance().showToast(message);
    }

    /**
     * 引起注意或聚焦的消息提示
     *
     * @param message
     * @param view
     */
    public static void showError(String message, View view) {
        showToast(message);
        if (view != null) {
            view.requestFocus();
            AnimationUtil.shake(view,3);
        }
    }
}
