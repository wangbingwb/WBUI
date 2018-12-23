package xyz.wbsite.wbsiteui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

import xyz.wbsite.wbsiteui.base.BaseSPAActivity;
import xyz.wbsite.wbsiteui.fragment.HomeFragment;
import xyz.wbsite.wbsiteui.fragment.LoginFragment;
import xyz.wbsite.wbsiteui.fragment.MainFragment;
import xyz.wbsite.wbsiteui.fragment.functions.AverageTextViewFragment;
import xyz.wbsite.wbsiteui.fragment.functions.listView.WBUIPaternalLayoutFragment;
import xyz.wbsite.wbsiteui.fragment.functions.other.OtherFragment;

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
        Fragment fragment = new MainFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(getContextViewId(), fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    public void onBackPressed() {
        QMUIFragment fragment = getCurrentFragment();
        if (fragment instanceof HomeFragment) {
            WBUIApplication.getInstance().exit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

