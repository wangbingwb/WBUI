package xyz.wbsite.wbui.base.ui.spinner;

import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Date;

public class WbSpinner extends TextView {
    private String[] arrs = {
            "AAAAAAAAAAAA", "BBBBBBBBBBB", "CCCCCCCCCCC", "DDDDDDDDDDD"
    };
    private float scale;
    private ListView optionList;
    private Context context;
    private int width;
    private boolean isShow = false;
    private PopupWindow popupWindow;
    private TextView itemTxtView;

    public WbSpinner(Context context) {
        super(context);
    }

    public WbSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getWidth();
    }

    private void init() {
        context = getContext();
        scale = getResources().getDisplayMetrics().density;
        this.setTextColor(Color.BLACK);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow) {
                    popupWindow.dismiss();
                    isShow = false;
                    return;
                }
                isShow = true;
                optionList = new ListView(context);
                optionList.setDivider(new ColorDrawable(Color.parseColor("#efefef")));
                optionList.setDividerHeight(1);
                optionList.setFadingEdgeLength(0);

                ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{0, 0, 0, 0, 5, 5, 5, 5}, new RectF((int) (1 * scale), (int) (1 * scale), (int) (1 * scale), (int) (1 * scale)), new float[]{0, 0, 0, 0, 5, 5, 5, 5}));
                shapeDrawable.getPaint().setColor(Color.parseColor("#efefef"));
                shapeDrawable.setPadding((int) (1 * scale), 0, (int) (1 * scale), (int) (1 * scale));

                ColorDrawable colorDrawable = new ColorDrawable(Color.WHITE);
                LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{colorDrawable, shapeDrawable});
                optionList.setBackground(layerDrawable);

                itemTxtView = new TextView(getContext());
                itemTxtView.setBackgroundColor(Color.BLUE);
                int id = (int) (new Date().getTime());
                itemTxtView.setId(id);

                optionList.setAdapter(new ArrayAdapter<String>(context, id, arrs));
                optionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        WbSpinner.this.setText(arrs[position]);
                        popupWindow.dismiss();
                        isShow = false;
                    }
                });

                popupWindow = new PopupWindow(width, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setContentView(optionList);
                popupWindow.showAsDropDown(WbSpinner.this, 0, 0);
            }
        });
    }
}
