package xyz.wbsite.wbsiteui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import xyz.wbsite.wbsiteui.R;
import xyz.wbsite.wbsiteui.base.BaseSPAFragment;

public class FunctionFragment extends BaseSPAFragment {

    @BindView(R.id.gridMenu)
    GridView gridMenu;
    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;

    private List<Menu> data = new ArrayList<>();

    @Override
    protected boolean canDragBack() {
        return false;
    }

    @Override
    protected int getFragmnetLayout() {
        return R.layout.fragment_functions;
    }

    @Override
    protected void initView() {

        topbar.setTitle("功能列表");

        initFunctions();
        gridMenu.setAdapter(new Adapter());
        gridMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Menu menu = data.get(i);
                if ("TakePhoto".endsWith(menu.getTitile())) {
                    startFragment(new TakePhotoFragment());
                }
            }
        });
    }

    private void initFunctions() {
        {
            Menu menu = new Menu();
            menu.setTitile("Button");
            menu.setIcon(R.mipmap.icon_grid_button);
            data.add(menu);
        }

        {
            Menu menu = new Menu();
            menu.setTitile("Dialog");
            menu.setIcon(R.mipmap.icon_grid_dialog);
            data.add(menu);
        }

        {
            Menu menu = new Menu();
            menu.setTitile("TakePhoto");
            menu.setIcon(R.mipmap.icon_grid_dialog);
            data.add(menu);
        }

    }

    private class Adapter extends BaseAdapter {

        class ViewHolder {
            TextView txtTitle;
            ImageView imgIcon;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view_;
            Adapter.ViewHolder holder;
            Menu item = data.get(i);
            if (null == view) {
                view_ = LayoutInflater.from(FunctionFragment.this.getContext()).inflate(R.layout.item_grid_menu, null);
                holder = new Adapter.ViewHolder();
                holder.txtTitle = (TextView) view_.findViewById(R.id.txtTitle);
                holder.imgIcon = (ImageView) view_.findViewById(R.id.imgIcon);
                view_.setTag(holder);
            } else {
                view_ = view;
                holder = (Adapter.ViewHolder) view.getTag();
            }
            holder.txtTitle.setText(item.getTitile());
            holder.imgIcon.setImageResource(item.getIcon());
            return view_;
        }
    }

    private class Menu {
        private String titile;

        private int icon;

        public String getTitile() {
            return titile;
        }

        public void setTitile(String titile) {
            this.titile = titile;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }
    }
}
