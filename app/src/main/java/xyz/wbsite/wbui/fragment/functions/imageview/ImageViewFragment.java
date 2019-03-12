package xyz.wbsite.wbui.fragment.functions.imageview;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import xyz.wbsite.wbui.R;
import xyz.wbsite.wbui.base.BaseSPAFragment;

public class ImageViewFragment extends BaseSPAFragment {

    @Override
    protected int getFragmnetLayout() {
        return R.layout.fragment_imageview;
    }

    @Override
    protected void onViewInit() {
        QMUIDisplayHelper.setFullScreen(getActivity());
    }
}
