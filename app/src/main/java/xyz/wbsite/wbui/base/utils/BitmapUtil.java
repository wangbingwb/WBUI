
package xyz.wbsite.wbui.base.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class BitmapUtil {


    public static Bitmap scaleBitmap(Bitmap src, int destWidth, int destHeigth) {
        if (src == null) return null;

        int width = src.getWidth();
        int height = src.getHeight();

        float scaleWidth = ((float) destWidth) / width;
        float scaleHeight = ((float) destHeigth) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
    }

    public static Bitmap scaleBitmap(Bitmap src, float scale) {
        if (src == null) return null;

        int width = src.getWidth();
        int height = src.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
    }

    public static Drawable getDrawableFromAssetFile(Resources res, String fileName) {
        InputStream is = null;
        Drawable icon = null;
        try {
            is = res.getAssets().open(fileName);
            TypedValue typedValue = new TypedValue();
            typedValue.density = TypedValue.DENSITY_DEFAULT;
            icon = Drawable.createFromResourceStream(res, typedValue, is, null);
        } catch (Exception e) {
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (final IOException e) {
            }
        }
        return icon;
    }

    public static Bitmap getBitmapFromAssetFile(Resources res, String fileName) {
        InputStream is = null;
        Bitmap bmp = null;
        try {
            is = res.getAssets().open(fileName);

            DisplayMetrics metrics = res.getDisplayMetrics();
            Options opts = new Options();
            opts.inDensity = (int) (metrics.densityDpi / metrics.scaledDensity);
            bmp = BitmapFactory.decodeStream(is, null, opts);
            is.close();
            is = null;
        } catch (Exception e) {
        } finally {
            try {
                if (is != null) is.close();
            } catch (final IOException e) {
            }
        }
        return bmp;
    }

    public static Drawable getDrawableFromFile(Resources res, String fileName) {
        FileInputStream fis = null;
        Drawable icon = null;
        try {
            fis = new FileInputStream(fileName);
            DisplayMetrics metrics = res.getDisplayMetrics();
            Options opts = new Options();
            opts.inDensity = (int) (metrics.densityDpi / metrics.scaledDensity);
            Bitmap bmp = BitmapFactory.decodeStream(fis, null, opts);

            fis.close();
            fis = null;
            if (bmp != null) {
                icon = new BitmapDrawable(bmp);
            }
        } catch (Exception e) {
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (final IOException e) {
            }
        }
        return icon;
    }

    public static Bitmap getBitmapFromFile(Resources res, String fileName) {
        FileInputStream fis = null;
        Bitmap bmp = null;
        try {
            fis = new FileInputStream(fileName);

            DisplayMetrics metrics = res.getDisplayMetrics();
            Options opts = new Options();
            opts.inDensity = (int) (metrics.densityDpi / metrics.scaledDensity);
            bmp = BitmapFactory.decodeStream(fis, null, opts);
            fis.close();
            fis = null;
        } catch (Exception e) {
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (final IOException e) {
            }
        }
        return bmp;
    }
}
