package xyz.wbsite.wbsiteui.fragment.functions.listView;

import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.ArrayList;

import butterknife.BindView;
import xyz.wbsite.wbsiteui.R;
import xyz.wbsite.wbsiteui.base.BaseSPAFragment;
import xyz.wbsite.wbsiteui.base.ui.list.PaternalLayout;

public class WBUIFixedRefreshListViewFragment extends BaseSPAFragment {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.paternalLayout)
    PaternalLayout paternalLayout;

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
        paternalLayout.setPullViewBuilder(new PaternalLayout.IPullViewBuilder() {
            @Override
            public View createView() {
                textView1.setText("AAAAAAAAAAAA");
                textView1.setGravity(Gravity.CENTER);
                textView1.setBackgroundColor(getResources().getColor(R.color.app_color_theme_5));
                return textView1;
            }

            @Override
            public void onChange(View view, int currentHeight, int refreshHeight) {
                textView1.setText("--Top-->" + currentHeight);
            }

            @Override
            public void onAction(View view, int mRefreshHeight, final PaternalLayout.Notify notify) {
                TextView textView = (TextView) view;
                textView.setText("正在刷新");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notify.finish();
                    }
                }, 1000);
            }
        });

        paternalLayout.setPushViewBuilder(new PaternalLayout.IPushViewBuilder() {
            @Override
            public View createView() {
                textView2.setText("BBBBBBBBB");
                textView2.setGravity(Gravity.CENTER);
                textView2.setBackgroundColor(getResources().getColor(R.color.app_color_theme_5));
                return textView2;
            }

            @Override
            public void onChange(View view, int currentHeight, int refreshHeight) {
                textView2.setText("--Bottom-->" + currentHeight);
            }

            @Override
            public void onAction(View view, int mRefreshHeight, final PaternalLayout.Notify notify) {
                TextView textView = (TextView) view;
                textView.setText("正在加载");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notify.finish();
                    }
                }, 1000);
            }

        });

        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            strings.add(i + "");
        }
        listView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, strings));
    }

    @Override
    protected boolean canDragBack() {
        return false;
    }
}
