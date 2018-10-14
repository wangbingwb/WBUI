package xyz.wbsite.wbsiteui.base.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by YiSD on 2015/11/23.
 */
public class CustomDialog extends Dialog {
    public CustomDialog(Context context) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().getDecorView().setPadding(0, 0, 0, 0);
//        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (params.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            params.width = this.getWindow().getContext().getResources().getDisplayMetrics().widthPixels;
        }
        if (params.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            params.height = this.getWindow().getContext().getResources().getDisplayMetrics().heightPixels;
        }

        super.setContentView(view, params);
    }

    public void setContentView(View view, ViewGroup.LayoutParams params, double widthScale, double heightScale) {
        if (widthScale < 0) {
            widthScale = 0;
        }
        if (heightScale > 1) {
            heightScale = 1;
        }
        if (params.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            params.width = (int) (widthScale * this.getWindow().getContext().getResources().getDisplayMetrics().widthPixels);
        }
        if (params.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            params.height = (int) (heightScale * this.getWindow().getContext().getResources().getDisplayMetrics().heightPixels);
        }
        super.setContentView(view, params);
    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View inflate = inflater.inflate(layoutResID, null);
        if (inflater == null) {
            throw new AssertionError("layout not find");
        }
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(inflate, layoutParams);
    }

    public void setContentView(int layoutResID, double widthScale, double heightScale) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View inflate = inflater.inflate(layoutResID, null);
        if (inflater == null) {
            throw new AssertionError("layout not find");
        }
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setContentView(inflate, layoutParams, widthScale, heightScale);
    }

    /**
     * 对齐方式
     *
     * @param gravity
     */
    public void setGravity(int gravity) {
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.gravity = gravity;
        getWindow().setAttributes(attributes);
    }

    /**
     * 设置背景屏幕透明度
     */
    public void setDimAmount(float amount) {
        this.getWindow().setDimAmount(amount);
    }

    /**
     * 自定义按钮点击事件
     *
     * @param viewId        自定义Dialog viewId
     * @param clickListener 点击事件监听器
     * @param dismiss       是否消失
     */
    public void addOnclickListener(int viewId, final View.OnClickListener clickListener, final boolean dismiss) {
        this.findViewById(viewId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v);
                if (dismiss) {
                    dismiss();
                }
            }
        });
    }

    /**
     * 自定义按钮点击事件
     *
     * @param view          自定义Dialog view
     * @param clickListener 点击事件监听器
     * @param dismiss       是否消失
     */
    public void addOnclickListener(View view, final View.OnClickListener clickListener, final boolean dismiss) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v);
                if (dismiss) {
                    dismiss();
                }
            }
        });
    }
}
