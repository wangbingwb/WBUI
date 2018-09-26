package xyz.wbsite.wbsiteui.base;

import android.os.Handler;
import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {

    protected Handler handler = new Handler();

    protected abstract void initView();
}
