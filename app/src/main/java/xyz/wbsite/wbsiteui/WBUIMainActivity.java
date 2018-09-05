package xyz.wbsite.wbsiteui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

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




//        if (savedInstanceState == null) {
//            BaseFragment fragment = getFirstFragment();
//
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .add(getContextViewId(), fragment, fragment.getClass().getSimpleName())
//                    .addToBackStack(fragment.getClass().getSimpleName())
//                    .commit();
//        }
        AndPermission.with(this).runtime().permission( Manifest.permission.READ_EXTERNAL_STORAGE).onGranted(new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                startFragment(getFirstFragment());
            }
        }).onDenied(new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                startFragment(getFirstFragment());
            }
        }).start();
    }

    private BaseFragment getFirstFragment() {
        return new HomeFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

