package xyz.wbsite.wbsiteui.fragment;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindView;
import xyz.wbsite.wbsiteui.R;
import xyz.wbsite.wbsiteui.base.BaseSPAFragment;

public class Fragment extends BaseSPAFragment {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;

    @Override
    protected int getFragmnetLayout() {
        return R.layout.fragment;
    }

    @Override
    protected void initView() {

    }
}
