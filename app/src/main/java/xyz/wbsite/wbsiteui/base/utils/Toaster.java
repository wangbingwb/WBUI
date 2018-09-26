package xyz.wbsite.wbsiteui.base.utils;

import android.view.View;

import xyz.wbsite.wbsiteui.WBUIApplication;

/**
 * 消息发生器
 */
public class Toaster {
    /**
     * 展示Toast消息。
     *
     * @param message 消息内容
     */
    public void showToast(String message) {
        WBUIApplication.getInstance().showToast(message);
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
}
