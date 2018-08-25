package xyz.wbsite.wbsiteui;

import android.content.Intent;
import android.os.Bundle;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import xyz.wbsite.wbsiteui.base.BaseActivity;
import xyz.wbsite.wbsiteui.base.BaseFragment;
import xyz.wbsite.wbsiteui.fragment.HomeFragment;

public class WBUIMainActivity extends BaseActivity {
    private static final String KEY_FRAGMENT = "key_fragment";
    private static final int VALUE_FRAGMENT_HOME = 0;
    private static final int VALUE_FRAGMENT_NOTCH_HELPER = 1;

    @Override
    protected int getContextViewId() {
        return R.id.main_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        startFragment(getFirstFragment());

//        if (savedInstanceState == null) {
//            BaseFragment fragment = getFirstFragment();
//
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .add(getContextViewId(), fragment, fragment.getClass().getSimpleName())
//                    .addToBackStack(fragment.getClass().getSimpleName())
//                    .commit();
//        }
    }

    private BaseFragment getFirstFragment() {
        return new HomeFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

