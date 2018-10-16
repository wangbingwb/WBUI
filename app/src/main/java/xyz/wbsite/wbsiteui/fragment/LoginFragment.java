package xyz.wbsite.wbsiteui.fragment;

import xyz.wbsite.wbsiteui.R;
import xyz.wbsite.wbsiteui.base.BaseSPAFragment;

public class LoginFragment extends BaseSPAFragment {

    @Override
    protected int getFragmnetLayout() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected boolean canDragBack() {
        return false;
    }
}
