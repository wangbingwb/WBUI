package xyz.wbsite.wbsiteui.fragment.functions.listView;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;
import xyz.wbsite.wbsiteui.R;
import xyz.wbsite.wbsiteui.base.BaseSPAFragment;
import xyz.wbsite.wbsiteui.base.ui.list.BoundListView;
import xyz.wbsite.wbsiteui.base.ui.list.PaternalLayout;
import xyz.wbsite.wbsiteui.base.ui.list.WBUIListView;

public class WBUIFixedRefreshListViewFragment extends BaseSPAFragment {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.paternalLayout)
    WBUIListView paternalLayout;

    @BindView(R.id.listView)
    ListView listView;


    TextView textView1;
    TextView textView2;

    @Override
    protected int getFragmnetLayout() {
        return R.layout.fragment_wbui_listview;
    }

    @Override
    protected void initView() {
        textView1 = new TextView(getContext());
        textView2 = new TextView(getContext());
        paternalLayout.setPullViewBuilder(new WBUIListView.IPullViewBuilder() {
            @Override
            public View createView() {
                textView1.setText("AAAAAAAAAAAA");
                textView1.setBackgroundColor(getResources().getColor(R.color.app_color_theme_5));
                return textView1;
            }

            @Override
            public void onChange(View view, int currentHeight, int refreshHeight) {
                textView1.setText("--Top-->" + currentHeight);
            }

            @Override
            public void onAction(View view, int mRefreshHeight, WBUIListView.Notify notify) {

                notify.action();
            }

            @Override
            public void onFinish(View view, int mRefreshHeight, WBUIListView.Notify notify) {
                notify.finish();
            }
        });

        paternalLayout.setPushViewBuilder(new WBUIListView.IPushViewBuilder() {
            @Override
            public View createView() {
                textView2.setText("BBBBBBBBB");
                textView2.setBackgroundColor(getResources().getColor(R.color.app_color_theme_5));
                return textView2;
            }

            @Override
            public void onChange(View view, int currentHeight, int refreshHeight) {
                textView2.setText("--Bottom-->" + currentHeight);
            }

            @Override
            public void onAction(View view, int mRefreshHeight, WBUIListView.Notify notify) {

                notify.action();
            }

            @Override
            public void onFinish(View view, int mRefreshHeight, WBUIListView.Notify notify) {
                notify.finish();
            }
        });


        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            strings.add(i + "");
        }
        listView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, strings));
    }

    @Override
    protected boolean canDragBack() {
        return false;
    }
}
