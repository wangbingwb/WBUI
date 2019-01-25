package xyz.wbsite.wbsiteui.fragment;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindView;
import xyz.wbsite.wbsiteui.R;
import xyz.wbsite.wbsiteui.base.BaseSPAFragment;

public class HomeFragment extends BaseSPAFragment {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;

    @Override
    protected int getFragmnetLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onViewInit() {
        topbar.setTitle("主页");
    }

    @Override
    protected boolean canDragBack() {
        return false;
    }
}
