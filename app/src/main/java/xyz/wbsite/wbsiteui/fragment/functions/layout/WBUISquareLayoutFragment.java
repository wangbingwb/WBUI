package xyz.wbsite.wbsiteui.fragment.functions.layout;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindView;
import xyz.wbsite.wbsiteui.R;
import xyz.wbsite.wbsiteui.base.BaseSPAFragment;

public class WBUISquareLayoutFragment extends BaseSPAFragment {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;

    @Override
    protected int getFragmnetLayout() {
        return R.layout.fragment_wbuisquarelayout;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected boolean canDragBack() {
        return false;
    }
}
