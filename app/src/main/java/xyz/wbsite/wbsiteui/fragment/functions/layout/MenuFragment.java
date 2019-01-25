package xyz.wbsite.wbsiteui.fragment.functions.layout;

import android.view.View;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import butterknife.BindView;
import xyz.wbsite.wbsiteui.R;
import xyz.wbsite.wbsiteui.base.BaseSPAFragment;
import xyz.wbsite.wbsiteui.fragment.functions.listView.WBUIPaternalLayoutFragment;

public class MenuFragment extends BaseSPAFragment {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.groupListView)
    QMUIGroupListView groupListView;

    @Override
    protected int getFragmnetLayout() {
        return R.layout.fragment_group_list;
    }

    @Override
    protected void onViewInit() {
        initGroupListView();
    }

    private void initGroupListView() {
        QMUICommonListItemView itemWithChevron = groupListView.createItemView("WBUISquareLayout");
        itemWithChevron.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {
                    CharSequence text = ((QMUICommonListItemView) v).getText();

                    if ("WBUISquareLayout".equals(text)) {
                        startFragment(new WBUISquareLayoutFragment());
                    }
                }
            }
        };

        QMUIGroupListView.newSection(getContext())
                .addItemView(itemWithChevron, onClickListener)
                .addTo(groupListView);
    }
}
