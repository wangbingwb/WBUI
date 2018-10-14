package xyz.wbsite.wbsiteui;

import android.Manifest;
import android.os.Bundle;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

import xyz.wbsite.wbsiteui.base.BaseSPAActivity;
import xyz.wbsite.wbsiteui.fragment.MainFragment;
import xyz.wbsite.wbsiteui.fragment.functions.listView.WBUIFixedRefreshListViewFragment;

public class WBUIMainActivity extends BaseSPAActivity {
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

        AndPermission.with(this).runtime().permission(Manifest.permission.READ_EXTERNAL_STORAGE).onGranted(new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                startFirstFragment();
            }
        }).onDenied(new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                startFirstFragment();
            }
        }).start();
    }

    private void startFirstFragment() {
        WBUIFixedRefreshListViewFragment mainFragment = new WBUIFixedRefreshListViewFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(getContextViewId(), mainFragment, mainFragment.getClass().getSimpleName())
                .addToBackStack(mainFragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

