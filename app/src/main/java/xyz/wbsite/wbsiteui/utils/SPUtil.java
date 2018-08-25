package xyz.wbsite.wbsiteui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by Administrator on 2018/3/11 0011.
 */

public class SPUtil {

    public static final String FILE_DEFAULT = "default";

    public static int getInt(String key) {
        SharedPreferences sharedPreferences = getSharePreferences(FILE_DEFAULT);
        return sharedPreferences.getInt(key, 0);
    }

    public static boolean getBoolean(String key) {
        SharedPreferences sharedPreferences = getSharePreferences(FILE_DEFAULT);
        return sharedPreferences.getBoolean(key, false);
    }

    public static float getFloat(String key) {
        SharedPreferences sharedPreferences = getSharePreferences(FILE_DEFAULT);
        return sharedPreferences.getFloat(key, 0);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        SharedPreferences sharedPreferences = getSharePreferences(FILE_DEFAULT);
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static String getString(String key) {
        SharedPreferences sharedPreferences = getSharePreferences(FILE_DEFAULT);
        return sharedPreferences.getString(key, "");
    }

    public static String getStringWithDefault(String key, String defStr) {
        SharedPreferences sharedPreferences = getSharePreferences(FILE_DEFAULT);
        return sharedPreferences.getString(key, defStr);
    }

    public static void putInt(String key, int value) {
        SharedPreferences.Editor editor = getEditor(getSharePreferences(FILE_DEFAULT));
        editor.putInt(key, value);
        editor.commit();
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(getSharePreferences(FILE_DEFAULT));
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = getEditor(getSharePreferences(FILE_DEFAULT));
        editor.putString(key, value);
        editor.commit();
    }

    public static void putFloat(String key, float value) {
        SharedPreferences.Editor editor = getEditor(getSharePreferences(FILE_DEFAULT));

        editor.putFloat(key, value);
        editor.commit();
    }

    public static void putLong(String key, long value) {
        SharedPreferences.Editor editor = getEditor(getSharePreferences(FILE_DEFAULT));

        editor.putLong(key, value);
        editor.commit();
    }


    public static long getLong(String key) {
        SharedPreferences sharedPreferences = getSharePreferences(FILE_DEFAULT);
        return sharedPreferences.getLong(key, 0);
    }

    public static int getInt(String key, int defValue) {
        SharedPreferences sharedPreferences = getSharePreferences(FILE_DEFAULT);
        return sharedPreferences.getInt(key, defValue);
    }

    public static int getInt(String key, String fileName) {

        SharedPreferences sharedPreferences = getSharePreferences(getFileName(fileName));
        return sharedPreferences.getInt(key, 0);
    }

    public static boolean getBoolean(String key, String fileName) {
        SharedPreferences sharedPreferences = getSharePreferences(getFileName(fileName));
        return sharedPreferences.getBoolean(key, false);
    }

    public static String getString(String key, String fileName) {
        SharedPreferences sharedPreferences = getSharePreferences(getFileName(fileName));
        return sharedPreferences.getString(key, null);
    }

    public static float getFloat(String key, String fileName) {
        SharedPreferences sharedPreferences = getSharePreferences(getFileName(fileName));
        return sharedPreferences.getFloat(key, 0);
    }

    public static long getLong(String key, String fileName) {
        SharedPreferences sharedPreferences = getSharePreferences(getFileName(fileName));
        return sharedPreferences.getLong(key, 0);
    }

    public static void putInt(String key, int value, String fileName) {
        SharedPreferences.Editor editor = getEditor(getSharePreferences(getFileName(fileName)));
        editor.putInt(key, value);
        editor.commit();

    }

    public static void putBoolean(String key, boolean value, String fileName) {
        SharedPreferences.Editor editor = getEditor(getSharePreferences(getFileName(fileName)));

        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void putString(String key, String value, String fileName) {
        SharedPreferences.Editor editor = getEditor(getSharePreferences(getFileName(fileName)));

        editor.putString(key, value);
        editor.commit();
    }

    public static void putFloat(String key, float value, String fileName) {
        SharedPreferences.Editor editor = getEditor(getSharePreferences(getFileName(fileName)));

        editor.putFloat(key, value);
        editor.commit();
    }

    public static void putLong(String key, long value, String fileName) {
        SharedPreferences.Editor editor = getEditor(getSharePreferences(getFileName(fileName)));

        editor.putLong(key, value);
        editor.commit();
    }

    private static SharedPreferences getSharePreferences(String fileName) {
        return App.getInstance().getSharedPreferences(fileName, Context.MODE_PRIVATE);

    }

    private static SharedPreferences.Editor getEditor(SharedPreferences sharedPreferences) {
        return sharedPreferences.edit();
    }

    public static String getFileName(String fileName) {
        if (!TextUtils.isEmpty(fileName)) {
            return fileName;
        } else {
            return FILE_DEFAULT;
        }
    }
}