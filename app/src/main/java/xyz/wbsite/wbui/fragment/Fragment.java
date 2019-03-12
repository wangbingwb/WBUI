package xyz.wbsite.wbui.fragment;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindView;
import xyz.wbsite.wbui.R;
import xyz.wbsite.wbui.base.BaseSPAFragment;

public class Fragment extends BaseSPAFragment {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;

    @Override
    protected int getFragmnetLayout() {
        return R.layout.fragment;
    }

    @Override
    protected void onViewInit() {

    }
}
