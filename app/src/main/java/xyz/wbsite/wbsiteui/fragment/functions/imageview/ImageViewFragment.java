package xyz.wbsite.wbsiteui.fragment.functions.imageview;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import xyz.wbsite.wbsiteui.R;
import xyz.wbsite.wbsiteui.base.BaseSPAFragment;

public class ImageViewFragment extends BaseSPAFragment {

    private boolean isFullScreen = false;

    @Override
    protected int getFragmnetLayout() {
        return R.layout.fragment_imageview;
    }

    @Override
    protected void initView() {
        QMUIDisplayHelper.setFullScreen(getActivity());
    }
}
