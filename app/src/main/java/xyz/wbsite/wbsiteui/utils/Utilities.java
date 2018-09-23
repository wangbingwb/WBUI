
package xyz.wbsite.wbsiteui.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class Utilities {
    public static final int AT_NULL = -1;
    public static final int AT_QUERY = 0;
    public static final int AT_RETURN = 2;

    private static int sIconWidth = -1;
    private static int sIconHeight = -1;

    private static final Paint sPaint = new Paint();
    private static final Rect sBounds = new Rect();
    private static final Rect sOldBounds = new Rect();
    private static Canvas sCanvas = new Canvas();

    private static final String CIPHER_AES = "AES/CBC/NoPadding";
    private static final String STRING_ENC = "gb2312";
    private static String digits = "0123456789abcdef";

    private static final byte[] AES_KEY = new byte[]{
            (byte) 0x91, (byte) 0x9F, (byte) 0xD3, (byte) 0xA5, (byte) 0xDE, (byte) 0x0D, (byte) 0x78, (byte) 0x70,
            (byte) 0x58, (byte) 0xAE, (byte) 0x05, (byte) 0xD2, (byte) 0x96, (byte) 0xE7, (byte) 0x5A, (byte) 0x19,
            (byte) 0x88, (byte) 0xCE, (byte) 0x16, (byte) 0x95, (byte) 0x61, (byte) 0x9B, (byte) 0x12, (byte) 0xD2,
            (byte) 0x31, (byte) 0x80, (byte) 0xB0, (byte) 0x6A, (byte) 0x1D, (byte) 0x14, (byte) 0x68, (byte) 0x84
    };

    private static final byte[] AES_IV = new byte[]{
            (byte) 0x3A, (byte) 0xF9, (byte) 0x42, (byte) 0x77, (byte) 0xD4, (byte) 0xFF, (byte) 0x28, (byte) 0x9F,
            (byte) 0xD0, (byte) 0xA4, (byte) 0xFA, (byte) 0x6B, (byte) 0x7F, (byte) 0xA6, (byte) 0x00, (byte) 0x2C
    };

    static {
        sCanvas.setDrawFilter(new PaintFlagsDrawFilter(Paint.DITHER_FLAG, Paint.FILTER_BITMAP_FLAG));
    }


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

    public static byte[] readAesFile(final String strFileName) {
        if (!fileExists(strFileName)) return null;

        FileInputStream fis = null;
        try {
            File file = new File(strFileName);
            int nLen = (int) file.length();
            fis = new FileInputStream(strFileName);
            byte[] readData = new byte[nLen];
            fis.read(readData);
            fis.close();
            fis = null;

            SecretKeySpec key = new SecretKeySpec(AES_KEY, "AES");
            IvParameterSpec iv = new IvParameterSpec(AES_IV);
            Cipher cipherAes = Cipher.getInstance(CIPHER_AES, "BC");
            cipherAes.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] data = cipherAes.doFinal(readData);
            return data;
        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchProviderException e) {
        } catch (NoSuchPaddingException e) {
        } catch (InvalidKeyException e) {
        } catch (BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } catch (InvalidAlgorithmParameterException e) {
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (final IOException e) {
            }
        }
        return null;
    }

    public static boolean saveAesFile(final String strFileName, byte[] data) {
        ByteArrayOutputStream out = null;
        FileOutputStream fos = null;
        try {
            SecretKeySpec key = new SecretKeySpec(AES_KEY, "AES");
            IvParameterSpec iv = new IvParameterSpec(AES_IV);
            Cipher cipherAes = Cipher.getInstance(CIPHER_AES, "BC");
            cipherAes.init(Cipher.ENCRYPT_MODE, key, iv);

            out = new ByteArrayOutputStream();
            out.write(data);
            int nLeft = 16 - out.size() % 16;
            if (nLeft != 16) {
                while (nLeft != 0) {
                    out.write(0);
                    nLeft--;
                }
                out.flush();
            }
            byte[] savedata = cipherAes.doFinal(out.toByteArray());
            out.close();
            out = null;

            fos = new FileOutputStream(strFileName);
            fos.write(savedata);
            fos.flush();
            fos.close();
            fos = null;
            return true;
        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchProviderException e) {
        } catch (NoSuchPaddingException e) {
        } catch (InvalidKeyException e) {
        } catch (BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } catch (InvalidAlgorithmParameterException e) {
        } finally {
            try {
                if (out != null) out.close();
                if (fos != null) fos.close();
            } catch (final IOException e) {
            }
        }
        return false;
    }

    /**
     * AES 加密
     */
    public static byte[] aesEncrypt(byte[] data) {
        if (data == null || data.length == 0 || data.length % 16 != 0) {
            return null;
        }

        try {
            SecretKeySpec key = new SecretKeySpec(AES_KEY, "AES");
            IvParameterSpec iv = new IvParameterSpec(AES_IV);
            Cipher cipherAes = Cipher.getInstance(CIPHER_AES, "BC");
            cipherAes.init(Cipher.ENCRYPT_MODE, key, iv);
            return cipherAes.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchProviderException e) {
        } catch (NoSuchPaddingException e) {
        } catch (InvalidKeyException e) {
        } catch (BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (InvalidAlgorithmParameterException e) {
        }
        return null;
    }

    /**
     * AES 解密
     */
    public static byte[] aesDecrypt(byte[] data) {
        if (data == null || data.length == 0 || data.length % 16 != 0) {
            return null;
        }

        try {
            SecretKeySpec key = new SecretKeySpec(AES_KEY, "AES");
            IvParameterSpec iv = new IvParameterSpec(AES_IV);
            Cipher cipherAes = Cipher.getInstance(CIPHER_AES, "BC");
            cipherAes.init(Cipher.DECRYPT_MODE, key, iv);
            return cipherAes.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchProviderException e) {
        } catch (NoSuchPaddingException e) {
        } catch (InvalidKeyException e) {
        } catch (BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (InvalidAlgorithmParameterException e) {
        }
        return null;
    }

    /**
     * 判断SD卡是否存在
     */
    static public boolean sdCardExists() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断目录或文件是否存在
     */
    public static boolean fileExists(final String strFile) {
        try {
            final File filePath = new File(strFile);
            return filePath.exists();
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * 保证指定的目录存在
     */
    public static boolean ensurePathExists(final String strPath) {
        try {
            final File filePath = new File(strPath);
            if (!filePath.exists())
                filePath.mkdirs();
            return true;
        } catch (final Exception e) {
            return false;
        }
    }

    private static boolean checkDate(int nYear, int nMonth, int nDay) {
        if (nYear < 1800 || nMonth < 1 || nMonth > 12 || nDay < 1 || nDay > 31)
            return false;

        if (nMonth == 2) {
            if (nDay > 29) return false;
            if (nDay == 29 && !((nYear % 4 == 0 && nYear % 100 != 0) || nYear % 400 == 0))
                return false;
            return true;
        }

        if (nMonth == 4 || nMonth == 6 || nMonth == 9 || nMonth == 11) {
            if (nDay < 31) return true;
            else return false;
        }
        return true;
    }

    /**
     * 校验8位日期
     */
    public static boolean checkDate8(String date) {
        if (date.length() != 8) return false;
        try {
            int year = Integer.valueOf(date.substring(0, 4)).intValue();
            int month = Integer.valueOf(date.substring(4, 6)).intValue();
            int day = Integer.valueOf(date.substring(6, 8)).intValue();
            return checkDate(year, month, day);
        } catch (NumberFormatException e) {
        }
        return false;
    }

    private static boolean checkDate2(int nYear, int nMonth) {
        if (nYear < 1800 || nMonth < 1 || nMonth > 12)
            return false;
        return true;
    }

    /**
     * 校验6位日期
     */
    public static boolean checkDate6(String date) {
        if (date.length() != 6) return false;
        try {
            int year = Integer.valueOf(date.substring(0, 4)).intValue();
            int month = Integer.valueOf(date.substring(4, 6)).intValue();
            return checkDate2(year, month);
        } catch (NumberFormatException e) {
        }
        return false;
    }

    /**
     * 校验18位身份证号码
     */
    public static boolean isIDCard18(final String value) {
        if (value == null || value.length() != 18) return false;
        if (!value.matches("[\\d]+[X]?")) return false;

        String code = "10X98765432";
        int weight[] = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};

        int nSum = 0;
        for (int i = 0; i < 17; ++i) {
            nSum += (int) (value.charAt(i) - '0') * weight[i];
        }

        int nCheckNum = nSum % 11;
        char chrValue = value.charAt(17);
        char chrCode = code.charAt(nCheckNum);
        if (chrValue == chrCode) return true;
        if (nCheckNum == 2 && (chrValue + ('a' - 'A') == chrCode))
            return true;

        return false;
    }

    /**
     * 校验身份证的日期格式是否正确
     */
    public static boolean checkIdCardDate(String value) {
        if (value == null || !value.matches("[\\d]+[X]?")) {
            return false;
        }

        int nLen = value.length();
        if (nLen != 15 && nLen != 18) return false;

        String date = null;
        if (nLen == 15) {
            date = "19" + value.substring(6, 12);
        } else {
            date = value.substring(6, 14);
        }
        return checkDate8(date);
    }

    /**
     * 15>>18位
     *
     * @param fifteenIDCard
     * @return
     * @throws Exception
     */
    public static String getEighteenIDCard(String fifteenIDCard) {
        if (fifteenIDCard != null && fifteenIDCard.length() == 15) {
            StringBuilder sb = new StringBuilder();
            sb.append(fifteenIDCard.substring(0, 6)).append("19")
                    .append(fifteenIDCard.substring(6));

            char IDCard18;
            try {
                IDCard18 = getVerifyCode(sb.toString());
            } catch (Exception e) {
                return fifteenIDCard;
            }
            sb.append(IDCard18);
            return sb.toString();
        } else {
            return fifteenIDCard;
        }
    }

    /**
     * 补全最后位
     *
     * @param idCardNumber
     * @return
     * @throws Exception
     */
    public static char getVerifyCode(String idCardNumber) throws Exception {
        if (idCardNumber == null || idCardNumber.length() < 17) {
            throw new Exception("不合法的身份证号码");
        }
        char[] Ai = idCardNumber.toCharArray();
        int[] Wi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] verifyCode = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3',
                '2'};
        int S = 0;
        int Y;
        for (int i = 0; i < Wi.length; i++) {
            S += (Ai[i] - '0') * Wi[i];
        }
        Y = S % 11;
        return verifyCode[Y];
    }

    /**
     * 校验是否成年
     */
    public static boolean isAdult(final String value) {
        if (!checkDate8(value) || value.length() < 0) return false;
        Date curTime = Calendar.getInstance().getTime();
        int nYear = curTime.getYear() + 1900;
        int nMonth = curTime.getMonth();
        int nDay = curTime.getDay();

        int pYear = string2Int(value.substring(0, 4));
        int pMonth = string2Int(value.substring(4, 6));
        int pDay = string2Int(value.substring(6, 8));

        if (16 < nYear - pYear) {
            return true;
        }

        if (16 == nYear - pYear) {
            if (nMonth > pMonth) {
                return true;
            }

            if (nMonth == pMonth) {
                if (nDay > pDay) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 校验身份证号码
     */
    public static boolean isIDCardValid(final String value) {
        if (!checkIdCardDate(value)) return false;

        if (value.length() == 18 && !isIDCard18(value)) {
            return false;
        }
        return true;
    }

    /**
     * 判断字符串是否是整数
     */
    public static boolean isInteger(final String value) {
        if (value.length() == 0) return false;

        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是数字
     */
    public static boolean isDigital(final String value) {
        if (value == null || value.length() == 0) {
            return false;
        }
        return value.matches("[\\d]+");
    }

    /**
     * 判断字符串是否是浮点数
     */
    public static boolean isDouble(final String value) {
        if (value.length() == 0) return false;

        try {
            Double.parseDouble(value);
            if (value.contains("."))
                return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是数字
     */
    public static boolean isNumber(final String value) {
        return isInteger(value) || isDouble(value);
    }

    /**
     * 判断字符串是否是字母
     */
    public static boolean isAlpha(final String value) {
        if (value.length() == 0) return false;
        return value.matches("[a-zA-Z]+");
    }

    /**
     * 判断字符串是否是由字母和数据组成
     */
    public static boolean isAlphaNum(final String value) {
        if (value.length() == 0) return false;
        return value.matches("[a-zA-Z0-9]+");
    }

    /**
     * 判断字符串是否是由GBK组成
     */
    public static boolean checkGbk(String value) {
        if (value == null || value.length() == 0) return false;

        for (int i = 0; i < value.length(); ++i) {
            char chr = value.charAt(i);
            if ((chr < 0x4E00 || chr >= 0xFE30) && chr != '.')
                return false;
        }
        return true;
    }

    /**
     * 判断字符串是否包含全角字符
     */
    public static boolean containSBCcase(final String str) {
        if (str == null || str.length() == 0) return false;

        for (int i = 0; i < str.length(); ++i) {
            char chr = str.charAt(i);
            if (chr == 0x3000 || (chr >= 0xFF01 && chr <= 0xFF5E))
                return true;
        }
        return false;
    }

    /**
     * 整数转换成字符串
     */
    public static String int2String(int i) {
        return Integer.toString(i);
    }

    /**
     * 整数字符串转换成整数，转换失败返回‘0’
     */
    public static int string2Int(String text) {
        return string2Int(text, 0);
    }

    /**
     * 整数字符串转换成整数，转换失败返回defaultVal
     */
    public static int string2Int(String text, int defaultVal) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    /**
     * 浮点数字符串转换成浮点数
     */
    public static float string2Float(String strFloat) {
        try {
            return Float.valueOf(strFloat);
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    /**
     * 校验浮点数字符串格式是否正确
     */
    public static boolean isFloatString(String strFloat) {
        if (strFloat == null || strFloat.length() == 0) return false;

        // 判断由数字和小数点组成
        if (!strFloat.matches("[\\d]+[.]?[\\d]*")) return false;

        // 格式不能为"数字."
        if (strFloat.matches("[\\d]+[.]{1}")) return false;

        return true;
    }

    /**
     * 校验车牌号码格式是否正确
     */
    public static boolean checkHphm(String hphm) {
        if (hphm == null || hphm.length() == 0) return false;

        StringBuilder builder = new StringBuilder();
        builder.append("[桂闽豫湘川藏鲁京粤沪鄂冀苏津辽晋陕渝新吉云宁青琼蒙甘浙黑贵皖赣]");
        builder.append("[A-Z&&[^I]]{1}");
        builder.append("[0-9A-Z&&[^I]]{4,5}");
        return hphm.matches(builder.toString());
    }

    /**
     * 列举目录下指定扩展名的文件名（全路径），没有指定扩展名则列举所有文件
     */
    public static ArrayList<String> listFile(final String path, final String ext) {
        ArrayList<String> arrFiles = new ArrayList<String>();
        if (path == null || !fileExists(path)) {
            return arrFiles;
        }

        File filePath = new File(path);
        if (!filePath.canRead()) return arrFiles;

        FilenameFilter filter = null;
        if (ext != null && ext.length() > 0 && !ext.equalsIgnoreCase("*.*")) {
            String[] arrExt = ext.split("\\.");
            if (arrExt == null || arrExt.length == 0)
                return arrFiles;

            final String strExt = arrExt[arrExt.length - 1];
            filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    String[] arrName = filename.split("\\.");
                    if (arrName == null || arrName.length < 2)
                        return false;
                    return arrName[arrName.length - 1].equalsIgnoreCase(strExt);
                }
            };
        }

        String list[] = filePath.list(filter);
        if (list == null) return arrFiles;
        for (String str : list) {
            arrFiles.add(filePath.getPath() + "/" + str);
        }
        return arrFiles;
    }

    /**
     * 列举目录下指定扩展名的文件数
     */
    public static int getFileCount(final String path, final String ext) {
        int count = 0;
        if (path == null || !fileExists(path)) {
            return count;
        }

        File filePath = new File(path);
        if (!filePath.canRead()) return count;

        FilenameFilter filter = null;
        if (ext != null && ext.length() > 0) {
            String[] arrExt = ext.split("\\.");
            if (arrExt == null || arrExt.length == 0)
                return count;

            try {//删除过期数据,try 防止出现错误
                if (filePath.listFiles() != null) {
                    for (File f : filePath.listFiles()) {
                        Calendar instance = Calendar.getInstance();
                        instance.setTime(new Date());
                        instance.add(Calendar.MINUTE, -3);
                        if (f.getName().startsWith("GD") && f.getName().endsWith(ext) && f.getName().length() >= 20) {
                            String substring = "20" + f.getName().substring(8, 20);
                            try {
                                Date yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss").parse(substring);
                                if (yyyyMMddHHmmss.before(instance.getTime())) {//如果比当前系统时间向前推3分钟还没有上传文成，那么应该是后台出错，无法完成上传
                                    //将文件备份，并删除原文件
                                    f.renameTo(new File(f.getAbsoluteFile() + ".copy"));
                                    f.delete();
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (Exception e) {

            }

            final String strExt = arrExt[arrExt.length - 1];
            filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    String[] arrName = filename.split("\\.");
                    if (arrName == null || arrName.length < 2)
                        return false;
                    return arrName[arrName.length - 1].equalsIgnoreCase(strExt);
                }
            };
        }

        String list[] = filePath.list(filter);
        if (list == null) return count;
        return list.length;
    }

    /**
     * 清除目录下指定扩展名的文件，没有指定扩展名清除所有文件
     */
    public static void cleanDir(final String path, final String ext) {
        ArrayList<String> arr = listFile(path, ext);
        for (String str : arr) {
            delFile(str);
        }
        arr = null;
    }

    /**
     * 把源目录下指定扩展名的文件移到目标目录下，没有指定扩展名移动所有文件
     */
    public static boolean moveFile2Dir(final String src, final String des, final String ext) {
        if (!ensurePathExists(des)) return false;

        boolean ret = true;
        File newPath = new File(des);
        ArrayList<String> arr = listFile(src, ext);
        for (String str : arr) {
            File file = new File(str);
            File newfile = new File(newPath.getAbsolutePath() + "/" + file.getName());
            newfile.delete();                // 删除已经存在的文件
            ret = file.renameTo(newfile);
            if (!ret) break;
        }
        arr = null;
        return ret;
    }

    /**
     * 删除文件
     */
    public static boolean delFile(final String filename) {
        if (filename == null) return true;
        File file = new File(filename);

        if (!file.exists()) return true;
        if (!file.isFile()) return false;
        return file.delete();
    }

    /**
     * 移动文件
     */
    public static boolean moveFile(final String srcName, final String newName) {
        if (srcName == null || newName == null) return false;
        File file = new File(srcName);
        if (!file.exists() || !file.isFile() ||
                !file.canRead() || !file.canWrite()) {
            return false;
        }

        File newFile = new File(newName);
        return file.renameTo(newFile);
    }

    /**
     * 复制文件
     */
    public static boolean copyFile(final String srcName, final String desName) {
        if (srcName == null || desName == null) return false;

        File src = new File(srcName);
        if (!src.exists() || !src.isFile())
            return false;

        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(src);
            fos = new FileOutputStream(desName);

            byte[] data = new byte[8 * 1024];
            int len = fis.read(data);
            while (len != -1) {
                fos.write(data, 0, len);
                len = fis.read(data);
            }
            fis.close();
            fos.close();
            return true;
        } catch (Exception e) {
        } finally {
            try {
                if (fis != null) fis.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
        return false;
    }

    /**
     * 重命名文件
     */
    public static boolean renameFile(final String oldName, final String newName) {
        if (oldName == null || newName == null) return false;
        if (oldName.equalsIgnoreCase(newName)) return true;

        File file = new File(oldName);
        if (!file.exists() || !file.canRead() || !file.canWrite()) {
            return false;
        }

        File newFile = new File(newName);
        return file.renameTo(newFile);
    }

    public static int byte2Int(byte b1, byte b2, byte b3, byte b4) {
        int nRet = (b4 & 0xFf);
        nRet = ((nRet << 8) | (b3 & 0xff));
        nRet = ((nRet << 8) | (b2 & 0xff));
        nRet = ((nRet << 8) | (b1 & 0xff));
        return nRet;
    }


    public static void int2Byte(int value, byte b1, byte b2, byte b3, byte b4) {
        b1 = (byte) (0x000000FF & value);
        b2 = (byte) ((0x0000FF00 & value) >> 8);
        b3 = (byte) ((0x00FF0000 & value) >> 16);
        b4 = (byte) ((0xFF000000 & value) >> 24);
    }

    public static boolean setFocusView(final View view) {
        if (view != null) {
            return view.requestFocus();
        }
        return false;
    }

    public static String getStringAt(final ArrayList<String> arr, int index) {
        if (arr == null || index < 0 || index >= arr.size()) {
            return "";
        }

        return arr.get(index);
    }

    public static Date getDateTime(final Date d, final Date t) {
        return new Date(d.getYear(), d.getMonth(), d.getDate(),
                t.getHours(), t.getMinutes());
    }

    public static String getDateTime(final Date dateTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.format(dateTime);
    }

    public static String getDateTimeEs(final Date dateTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(dateTime);
    }

    public static String getDateTimeSSSS(final Date dateTime) {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(dateTime);
    }

    public static String getDateTimeYYMMddHHmmss(final Date dateTime) {
        DateFormat df = new SimpleDateFormat("yyMMddHHmmss");
        return df.format(dateTime);
    }

    public static String getDateTime1(final Date dateTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(dateTime);
    }

    public static String getDateTimeCN(final Date dateTime) {
        DateFormat df = new SimpleDateFormat("yyyy年M月d日H时m分");
        return df.format(dateTime);
    }

    public static String getDateCN(final Date date) {
        DateFormat df = new SimpleDateFormat("yyyy年M月d日");
        return df.format(date);
    }

    public static void repairCmt(final String cfdh, String tempPath, String donePath, String ext) {
        if (cfdh == null || cfdh.length() == 0 ||
                tempPath == null || tempPath.length() == 0 ||
                donePath == null || donePath.length() == 0 ||
                ext == null || ext.length() == 0 ||
                !ensurePathExists(tempPath) || !ensurePathExists(donePath)) {
            return;
        }

        ArrayList<String> fileList = listFile(tempPath, ext);
        if (fileList.size() == 0) return;
        for (String filename : fileList) {
            File file = new File(filename);
            String arr[] = file.getName().split("#");
            if (arr == null || arr.length != 4) continue;

            if (arr[1].equalsIgnoreCase(cfdh))
                file.delete();
            else {
                File newFile = new File(donePath + file.getName());
                file.renameTo(newFile);
            }
        }
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String text) {
        return (text == null || text.length() == 0);
    }

    /**
     * 关闭输入流
     */
    public static void closeInputStream(InputStream is) {
        try {
            is.close();
        } catch (IOException e) {
        }
    }

    /**
     * 关闭输出流
     */
    public static void closeOutputStream(OutputStream os) {
        try {
            os.close();
        } catch (IOException e) {
        }
    }

    // { 0x00, 0x01 } -> "00 01"
    public static String toHex(byte[] data) {
        int len = data.length;
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i != len; i++) {
            int v = data[i] & 0xff;
            buf.append(digits.charAt(v >> 4));
            buf.append(digits.charAt(v & 0xf));
        }
        return buf.toString();
    }

    // "00 01" -> { 0x00, 0x01 }
    public static byte[] hexToBytes(String s) {
        byte[] bytes = new byte[s.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    /**
     * 取本地时间
     */
    public static String getLocationTime(long milliseconds) {
        Date time = new Date(milliseconds);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(time);
    }

    /**
     * 取UTC时间
     */
    public static String getUTCTime(long milliseconds) {
        Date time = new Date(milliseconds);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(time);
    }


    public static int getValueByDpi(Context ctx, int value) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return (int) (value * density + 0.5f);
    }

    public static String getDateTimeSS(final Date dateTime) {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSSSSS");
        return df.format(dateTime);
    }

    public static ArrayList<String> getZjcx(String zjcx) {
        ArrayList<String> list = new ArrayList<String>();
        int count = -1;
        char[] charArray = zjcx.toCharArray();
        for (char cnow : charArray) {
            StringBuilder sb = new StringBuilder();
            count++;

            if (cnow >= '0' && cnow <= '9') {
                continue;
            }

            if (cnow >= 'A' && cnow <= 'Z') {
                sb.append(cnow);
            }

            if (charArray.length > count + 1) {
                char cnext = charArray[count + 1];
                if (cnext >= '0' && cnext <= '9') {
                    sb.append(cnext);
                }
            }
            list.add(sb.toString());
        }
        return list;
    }

    public static Date getDateTimeF(final String dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = sdf.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getDateTimeEx(final Date dateTime) {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(dateTime);
    }

    public static String getDate(final Date dateTime) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(dateTime);
    }

    /**
     * 根据byte数组，生成文件
     */
    public static void saveBytetoFile(byte[] bfile, String filePath, String fileName) {
        if (bfile == null) {
            return;
        }

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                // 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null && fos != null) {
                try {
                    bos.flush();
                    fos.flush();
                    bos.close();
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    // 删除文件到手机
    public static void deleteFile(Context context, String fileName) {
        try {
            context.deleteFile(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 保存文件到手机
    public static void saveFile(Context context, String fileName, String textContent) {
        FileOutputStream outStream = null;
        try {
            outStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outStream.write(textContent.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outStream != null) {
                try {
                    outStream.flush();
                    outStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 从手机读取文件
    public static String readFile(Context context, String fileName) {
        byte[] content = null;
        try {
            FileInputStream fis = context.openFileInput(fileName);
            byte[] b = new byte[1024];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (fis.read(b) != -1) {
                byteArrayOutputStream.write(b);
            }
            content = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (content != null) {
            return new String(content);
        }
        return "";
    }

    public static String getDateTimeSecond(final Date dateTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(dateTime);
    }

}
