package xyz.wbsite.wbsiteui.base.ui.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import xyz.wbsite.wbsiteui.base.utils.FastBlur;


public class BlurImage extends android.support.v7.widget.AppCompatImageView {
    public BlurImage(Context context) {
        super(context);
    }

    public BlurImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //获取当前显示的位图
        Bitmap bitMap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        super.onDraw(new Canvas(bitMap));
        //模糊处理
        FastBlur.doBlur(bitMap, 10, true);
        //绘图
        canvas.drawBitmap(bitMap, 0, 0, new Paint());
    }
}
