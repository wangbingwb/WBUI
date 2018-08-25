package xyz.wbsite.wbsiteui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import xyz.wbsite.wbsiteui.R;

public abstract class BaseFragment extends QMUIFragment {
    private Unbinder unbinder;

    @BindView(R.id.topbar)
    public QMUITopBarLayout topbar;

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
}
