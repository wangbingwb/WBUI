package xyz.wbsite.wbsiteui.base;

import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import xyz.wbsite.wbsiteui.WBUIApplication;
import xyz.wbsite.wbsiteui.utils.AnimationUtil;

public abstract class BaseSPAFragment extends QMUIFragment {
    private Unbinder unbinder;
    protected Handler handler = new Handler();

    @Override
    protected int backViewInitOffset() {
        return QMUIDisplayHelper.dp2px(getContext(), 100);
    }

    protected abstract int getFragmnetLayout();

    @Override
    protected View onCreateView() {
        View inflate = LayoutInflater.from(getActivity()).inflate(getFragmnetLayout(), null);
        unbinder = ButterKnife.bind(this, inflate);
        initView();
        return inflate;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    protected abstract void initView();

    public void showLoading() {
        BaseSPAActivity activity = (BaseSPAActivity) getActivity();
        activity.showLoading();
    }

    public void closeLoading() {
        BaseSPAActivity activity = (BaseSPAActivity) getActivity();
        activity.dismissLoading();
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
