package xyz.wbsite.wbsiteui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import xyz.wbsite.wbsiteui.R;
import xyz.wbsite.wbsiteui.base.BaseFragment;

public class NextFragment extends BaseFragment {
    @Override
    protected int getFragmnetLayout() {
        return R.layout.fragment_next;
    }

//    @Override
//    protected View onCreateView() {
//        FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_next, null);
//        layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
//        return layout;
//    }


    @Override
    protected void initView() {
        topbar.addLeftBackImageButton();
        topbar.setTitle("aaaaaaaaaa");

    }
}
