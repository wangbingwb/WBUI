package xyz.wbsite.wbsiteui.base;

import android.view.LayoutInflater;
import android.view.View;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import xyz.wbsite.wbsiteui.R;

public abstract class BaseTitleFragment extends QMUIFragment {
    private Unbinder unbinder;

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;


    protected abstract void initTitle(QMUITopBarLayout topbar);


    @Override
    protected int backViewInitOffset() {
        return QMUIDisplayHelper.dp2px(getContext(), 100);
    }

    protected abstract int getFragmnetLayout();

    @Override
    protected View onCreateView() {
        View inflate = LayoutInflater.from(getActivity()).inflate(getFragmnetLayout(), null);
        unbinder = ButterKnife.bind(this, inflate);
        initTitle(topbar);
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
