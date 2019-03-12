package xyz.wbsite.wbui.base.ui.textview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by bingwang on 2016/6/4.
 */
public class WbTextView1 extends TextView {
    private int r = 250;

    public void setR(int r) {
        this.r = r;
    }

    public WbTextView1(Context context) {
        super(context);
    }

    public WbTextView1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int meshWidth = 5;
        int meshHeight = 5;

        //获得原始位图
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        super.onDraw(new Canvas(bitmap));

        //初始化网格坐标
        float[] vers = new float[(meshWidth + 1) * (meshHeight + 1) * 2];
        for (int y = 0; y <= meshHeight; y++) {
            for (int x = 0; x <= meshWidth; x++) {
                vers[2 * x + y * (2 * (meshWidth + 1))] = (float) width * x / meshWidth;
                vers[2 * x + y * (2 * (meshWidth + 1)) + 1] = (float) height * y / meshWidth;
            }
        }

        //扭曲坐标
        for (int i = 0; i < vers.length; i += 2) {
            if (vers[i] != width / 2) {
                //获取周长
                float l = Math.abs(vers[i] - width / 2);
                //获取角度
                double v = Math.toRadians((l / (2 * Math.PI * r)) * 360);
                //转换后的长度
                double v1 = Math.sin(v) * r;
                //转成实际坐标
                vers[i] = (float) ((Math.abs(vers[i] - width / 2) / (vers[i] - width / 2)) * v1 + width / 2);
            }
        }

        canvas.drawBitmapMesh(bitmap, meshWidth, meshHeight, vers, 0, null, 0, null);
    }

}
