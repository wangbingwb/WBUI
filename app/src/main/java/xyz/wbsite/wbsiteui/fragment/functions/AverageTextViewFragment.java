package xyz.wbsite.wbsiteui.fragment.functions;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindView;
import xyz.wbsite.wbsiteui.R;
import xyz.wbsite.wbsiteui.base.BaseSPAFragment;

public class AverageTextViewFragment extends BaseSPAFragment {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;

    @Override
    protected int getFragmnetLayout() {
        return R.layout.fragment_average_textview;
    }

    @Override
    protected void onViewInit() {

    }

    @Override
    protected boolean canDragBack() {
        return false;
    }
}
