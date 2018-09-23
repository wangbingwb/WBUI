package xyz.wbsite.wbsiteui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import xyz.wbsite.wbsiteui.R;
import xyz.wbsite.wbsiteui.base.BaseFragment;
import xyz.wbsite.wbsiteui.base.BaseSPAFragment;

public class HomeFragment extends BaseSPAFragment {

    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.tabs)
    QMUITabSegment tabs;

    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected int getFragmnetLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        initTabs();
        initPagers();
    }

    private void initTabs() {
        QMUITabSegment.Tab function = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_component),
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_component_selected),
                "功能", false
        );
        QMUITabSegment.Tab function1 = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_component),
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_component_selected),
                "H5", false
        );
        QMUITabSegment.Tab function2 = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_component),
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_component_selected),
                "关于", false
        );
        tabs.addTab(function)
                .addTab(function1)
                .addTab(function2);
    }

    private void initPagers() {
        fragments.add(new FunctionFragment());
        fragments.add(new WebViewFragment());

        pager.setAdapter(new BaseFragmentPagerAdapter(getActivity().getSupportFragmentManager()));
        tabs.setupWithViewPager(pager, false);
    }

    @Override
    protected boolean canDragBack() {
        return false;
    }

    private class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

        public BaseFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
