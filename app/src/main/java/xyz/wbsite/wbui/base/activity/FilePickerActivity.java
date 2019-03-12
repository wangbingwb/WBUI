package xyz.wbsite.wbui.base.activity;


import android.content.Intent;
import android.os.Bundle;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import xyz.wbsite.wbui.base.BaseSPAActivity;

public class FilePickerActivity extends BaseSPAActivity {

    private static final int REQUESTCODE_FROM_ACTIVITY = 0;


    @Override
    protected int getContextViewId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new LFilePicker()
                .withActivity(this)
                .withTitle("选择附件")
                .withBackIcon(Constant.BACKICON_STYLETHREE)
                .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(resultCode,data);
        finish();
    }
}
