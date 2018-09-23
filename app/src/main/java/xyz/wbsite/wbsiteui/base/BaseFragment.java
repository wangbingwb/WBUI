package xyz.wbsite.wbsiteui.base;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;

import xyz.wbsite.wbsiteui.WBUIApplication;
import xyz.wbsite.wbsiteui.utils.AnimationUtil;

public abstract class BaseFragment extends Fragment {

    protected Handler handler = new Handler();

    protected abstract void initView();


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
}
