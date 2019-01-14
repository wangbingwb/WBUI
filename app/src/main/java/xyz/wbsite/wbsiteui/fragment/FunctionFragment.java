package xyz.wbsite.wbsiteui.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import xyz.wbsite.wbsiteui.R;
import xyz.wbsite.wbsiteui.base.BaseSPAFragment;
import xyz.wbsite.wbsiteui.base.IActivityResult;
import xyz.wbsite.wbsiteui.base.activity.FilePickerActivity;
import xyz.wbsite.wbsiteui.base.activity.QRcodeActivity;
import xyz.wbsite.wbsiteui.base.utils.Toaster;
import xyz.wbsite.wbsiteui.fragment.functions.BackPhotoFragment;
import xyz.wbsite.wbsiteui.fragment.functions.TakePhotoFragment;
import xyz.wbsite.wbsiteui.fragment.functions.imageview.ImageViewFragment;
import xyz.wbsite.wbsiteui.fragment.functions.listView.MenuFragment;

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
                switch (menu.getIcon()) {
                    case R.mipmap.icon_fun_button:
                        break;
                    case R.mipmap.icon_fun_list:
                        startFragment(new MenuFragment());
                        break;
                    case R.mipmap.icon_fun_dialog:
                        break;
                    case R.mipmap.icon_fun_photo:
                        startFragment(new TakePhotoFragment());
                        break;
                    case R.mipmap.icon_fun_imgview:
                        startFragment(new ImageViewFragment());
                        break;
                    case R.mipmap.icon_scanning:
                        new IntentIntegrator(getActivity())
                                .setCaptureActivity(QRcodeActivity.class)
                                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)// 扫码的类型,可选：一维码，二维码，一/二维码
                                .setPrompt("请对准二维码")// 设置提示语
                                .setCameraId(0)// 选择摄像头,可使用前置或者后置
                                .setBeepEnabled(false)// 是否开启声音,扫完码之后会"哔"的一声
                                .setBarcodeImageEnabled(true)// 扫完码之后生成二维码的图片
                                .initiateScan();// 初始化扫码
                        break;
                    case R.mipmap.icon_fun_layout:
                        startFragment(new xyz.wbsite.wbsiteui.fragment.functions.layout.MenuFragment());
                    case R.mipmap.icon_backphoto:
                        startFragment(new BackPhotoFragment());
                        break;
                    case R.mipmap.icon_fun_file:
                        startForResult(new Intent(getActivity(), FilePickerActivity.class), new IActivityResult() {
                            @Override
                            public void onResult(int resultCode, Intent data) {
                                if (data != null){
                                    List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
                                    Toaster.showToast("选中了" + list.size() + "个文件");
                                }
                            }
                        });
                        break;
                }
            }
        });
    }

    private void initFunctions() {
        {
            Menu menu = new Menu();
            menu.setTitile("Button");
            menu.setIcon(R.mipmap.icon_fun_button);
            data.add(menu);
        }

        {
            Menu menu = new Menu();
            menu.setTitile("List");
            menu.setIcon(R.mipmap.icon_fun_list);
            data.add(menu);
        }

        {
            Menu menu = new Menu();
            menu.setTitile("Dialog");
            menu.setIcon(R.mipmap.icon_fun_dialog);
            data.add(menu);
        }

        {
            Menu menu = new Menu();
            menu.setTitile("TakePhoto");
            menu.setIcon(R.mipmap.icon_fun_photo);
            data.add(menu);
        }
        {
            Menu menu = new Menu();
            menu.setTitile("TakePhoto");
            menu.setIcon(R.mipmap.icon_scanning);
            data.add(menu);
        }

        {
            Menu menu = new Menu();
            menu.setTitile("Layout");
            menu.setIcon(R.mipmap.icon_fun_layout);
            data.add(menu);
        }

        {
            Menu menu = new Menu();
            menu.setTitile("BackPhoto");
            menu.setIcon(R.mipmap.icon_backphoto);
            data.add(menu);
        }

        {
            Menu menu = new Menu();
            menu.setTitile("ImageView");
            menu.setIcon(R.mipmap.icon_fun_imgview);
            data.add(menu);
        }
        {
            Menu menu = new Menu();
            menu.setTitile("File Select");
            menu.setIcon(R.mipmap.icon_fun_file);
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
