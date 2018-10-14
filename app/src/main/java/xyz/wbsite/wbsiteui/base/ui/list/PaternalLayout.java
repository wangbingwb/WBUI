package xyz.wbsite.wbsiteui.base.ui.list;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

public class PaternalLayout extends LinearLayout implements AbsListView.OnScrollListener {
    @Override
    public void addView(View child) {
        super.addView(child);
    }

    public PaternalLayout(Context context) {
        super(context);
        init();
    }

    public PaternalLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void onViewAdded(View child) {
        if (child instanceof AbsListView){
            ((AbsListView)child).setOnScrollListener(this);
        }
        super.onViewAdded(child);
    }

    private void init() {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_IDLE:
                Log.d("------------", "SCROLL_STATE_IDLE");
                break;
            case SCROLL_STATE_FLING:
                Log.d("------------", "SCROLL_STATE_FLING");
                break;
            case SCROLL_STATE_TOUCH_SCROLL:
                Log.d("------------", "SCROLL_STATE_TOUCH_SCROLL");
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
