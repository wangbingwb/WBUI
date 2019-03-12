package xyz.wbsite.wbui.base.ui.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundImageView extends ImageView {
    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //获取原图
        int width = getWidth();
        int height = getHeight();
        Bitmap srcBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        super.onDraw(new Canvas(srcBitmap));

        //绘制原图
        {
            Bitmap shapeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas shapeCanvas = new Canvas(shapeBitmap);
            shapeCanvas.drawCircle(width / 2, height / 2, Math.min(width, height) / 2 - 4, new Paint());

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            shapeCanvas.drawBitmap(srcBitmap, 0, 0, paint);

            canvas.drawBitmap(shapeBitmap, 0, 0, new Paint());
        }

        //绘制边框
        {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.GREEN);
            paint.setStrokeWidth(4);
            paint.setAntiAlias(true);
            canvas.drawCircle(width / 2, height / 2, Math.min(width, height) / 2 - 4, paint);
        }
    }
}
