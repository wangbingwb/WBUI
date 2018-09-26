package xyz.wbsite.wbsiteui.base;

import android.app.Activity;
import android.content.Intent;

import java.util.Hashtable;

public abstract class BaseActivity extends Activity {
    private Hashtable<Integer, IActivityResult> resultHashtable = new Hashtable<>();
    private int mRequestCode = 1;

    public void startForResult(Intent intent, IActivityResult activityResult) {
        int requestCode = mRequestCode++;
        resultHashtable.put(requestCode, activityResult);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultHashtable.containsKey(requestCode)) {
            IActivityResult i = resultHashtable.remove(requestCode);
            if (i != null) {
                i.onResult(resultCode, data);
            }
        }
    }
}
