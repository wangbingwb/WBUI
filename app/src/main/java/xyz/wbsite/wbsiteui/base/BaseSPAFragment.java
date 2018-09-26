package xyz.wbsite.wbsiteui.base;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import butterknife.ButterKnife;
import butterknife.Unbinder;

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

}
