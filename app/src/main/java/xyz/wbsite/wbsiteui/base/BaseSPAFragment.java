package xyz.wbsite.wbsiteui.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.util.Hashtable;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseSPAFragment extends QMUIFragment {
    private Unbinder unbinder;
    protected Handler handler = new Handler();
    private Hashtable<Integer, IFragmentResultListerner> mResultListerner = new Hashtable<>();
    private int mRequestCode = 1;

    public interface IFragmentResultListerner {
        void onResult(int resultCode, Intent data);
    }

    public void startForResult(BaseSPAFragment intent, IFragmentResultListerner listerner) {
        int requestCode = mRequestCode++;
        mResultListerner.put(requestCode, listerner);
        startFragmentForResult(intent, requestCode);
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Intent data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (mResultListerner.containsKey(requestCode)) {
            IFragmentResultListerner l = mResultListerner.remove(requestCode);
            if (l != null) {
                l.onResult(resultCode, data);
            }
        }
    }

    @Override
    protected int backViewInitOffset() {
        return QMUIDisplayHelper.dp2px(getContext(), 100);
    }

    protected abstract int getFragmnetLayout();

    @Override
    protected View onCreateView() {
        View inflate = LayoutInflater.from(getActivity()).inflate(getFragmnetLayout(), null);
        unbinder = ButterKnife.bind(this, inflate);
        initView();
        if (getArguments() != null) {
            onDataRecovery(getArguments());
        }
        return inflate;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (getArguments() != null) {
            onDataRecovery(getArguments());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    protected abstract void initView();

    public void showLoading() {
        BaseSPAActivity activity = (BaseSPAActivity) getActivity();
        activity.showLoading();
    }

    public void closeLoading() {
        BaseSPAActivity activity = (BaseSPAActivity) getActivity();
        activity.dismissLoading();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        onDataSave(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getArguments() == null && !isStateSaved()) {
            setArguments(new Bundle());
        }
        onDataSave(getArguments());
    }

    public void startForResult(Intent intent, IActivityResult activityResult) {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseSPAActivity){
            BaseSPAActivity baseSPAActivity = (BaseSPAActivity) activity;
            baseSPAActivity.startForResult(intent,activityResult);
        }
    }

    @Override
    protected boolean canDragBack() {
        return false;
    }

    protected void onDataSave(Bundle data) {

    }

    protected void onDataRecovery(Bundle data) {

    }
}