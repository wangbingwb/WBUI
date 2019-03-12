package xyz.wbsite.wbui.fragment.functions.listView;

import android.view.LayoutInflater;
import android.view.View;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindView;
import xyz.wbsite.wbui.R;
import xyz.wbsite.wbui.base.BaseSPAFragment;
import xyz.wbsite.wbui.base.ui.layout.WBUIPaternalLayout;
import xyz.wbsite.wbui.base.ui.other.LoadingProgressBar;

public class WBUIPaternalLayoutFragment extends BaseSPAFragment {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.paternalLayout)
    WBUIPaternalLayout paternalLayout;


    @Override
    protected int getFragmnetLayout() {
        return R.layout.fragment_wbui_listview;
    }

    @Override
    protected void onViewInit() {
        paternalLayout.setPullViewBuilder(new WBUIPaternalLayout.IPullViewBuilder() {
            LoadingProgressBar progressBar;

            @Override
            public View createView() {
                View inflate = LayoutInflater.from(getContext()).inflate(R.layout.ui_layout_pull, null);
                progressBar = inflate.findViewById(R.id.progressBar);
                return inflate;
            }

            @Override
            public void onChange(View view, int currentHeight, int pullHeight, boolean isNearHeight) {
                if (currentHeight > pullHeight) {
                    progressBar.setProgress(1);
                } else {
                    progressBar.setProgress(currentHeight * 1.0f / pullHeight);
                }
            }

            @Override
            public void onAction(View view, int pullHeight, WBUIPaternalLayout layout) {
                progressBar.startAnimation();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        paternalLayout.finish();
                    }
                }, 2000);
            }

            @Override
            public void onFinish(View view) {
                progressBar.stopAnimation();
                progressBar.setProgress(0);
            }
        });
        paternalLayout.setPushViewBuilder(new WBUIPaternalLayout.IPushViewBuilder() {
            LoadingProgressBar progressBar;

            @Override
            public View createView() {
                View inflate = LayoutInflater.from(getContext()).inflate(R.layout.ui_layout_pull, null);
                progressBar = inflate.findViewById(R.id.progressBar);
                return inflate;
            }

            @Override
            public void onChange(View view, int currentHeight, int pullHeight, boolean isNearHeight) {
                if (currentHeight > pullHeight) {
                    progressBar.setProgress(1);
                } else {
                    progressBar.setProgress(currentHeight * 1.0f / pullHeight);
                }
            }

            @Override
            public void onAction(View view, int pullHeight, WBUIPaternalLayout layout) {
                progressBar.startAnimation();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        paternalLayout.finish();
                    }
                }, 2000);
            }

            @Override
            public void onFinish(View view) {
                progressBar.stopAnimation();
                progressBar.setProgress(0);
            }
        });


    }

    @Override
    protected boolean canDragBack() {
        return false;
    }
}
