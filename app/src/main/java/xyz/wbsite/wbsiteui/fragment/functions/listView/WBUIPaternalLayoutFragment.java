package xyz.wbsite.wbsiteui.fragment.functions.listView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindView;
import xyz.wbsite.wbsiteui.R;
import xyz.wbsite.wbsiteui.base.BaseSPAFragment;
import xyz.wbsite.wbsiteui.base.ui.layout.WBUIPaternalLayout;

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
    protected void initView() {
        paternalLayout.setPullViewBuilder(new WBUIPaternalLayout.IPullViewBuilder() {
            ImageView imgIcon;
            TextView txtText;

            @Override
            public View createView() {
                View inflate = LayoutInflater.from(getContext()).inflate(R.layout.ui_layout_pull, null);
                imgIcon = inflate.findViewById(R.id.imgIcon);
                txtText = inflate.findViewById(R.id.txtText);
                return inflate;
            }

            @Override
            public void onChange(View view, int currentHeight, int pullHeight, boolean isNearHeight) {
                if (!isNearHeight) {
                    imgIcon.setImageResource(R.drawable.ui_icon_down);
                    txtText.setText("下拉刷新");
                } else {
                    imgIcon.setImageResource(R.drawable.ui_icon_up);
                    txtText.setText("松开刷新");
                }
            }

            @Override
            public void onAction(View view, int mRefreshHeight, WBUIPaternalLayout layout) {
                txtText.setText("正在刷新");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        paternalLayout.finish();
                    }
                },1000);
            }
        });

//        ArrayList<String> strings = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            strings.add(i + "");
//        }
//        listView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, strings));
    }

    @Override
    protected boolean canDragBack() {
        return false;
    }
}
