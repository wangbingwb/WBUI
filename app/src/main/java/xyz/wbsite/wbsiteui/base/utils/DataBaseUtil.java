package xyz.wbsite.wbsiteui.base.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import xyz.wbsite.wbsiteui.WBUIApplication;
import xyz.wbsite.wbsiteui.base.Content;

public class DataBaseUtil {
    private String defaultPath = "";
    private String defaultDBName = "data.db";
    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase writableDatabase;
    private List<Class> entityList;

    private DataBaseUtil() {
    }

    public DataBaseUtil(Context context, Register register) {
        entityList = register.run();
        File ownCacheDirectory = StorageUtil.getOwnCacheDirectory(context, Content.DIR);
        defaultPath = ownCacheDirectory.getAbsolutePath();
        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, defaultDBName, null, 1);
        writableDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public <T> List<T> query(T t) {
        return query(t, 0, 0);
    }

    public <T> List<T> query(T t, int start, int size) {
        ArrayList<T> list = new ArrayList<>();
        try {
            Class a = findEntity(t);
            //1、获取表名
            String table = a.getSimpleName();
            //2、获取字段列表
            List<Field> fs = new ArrayList<>();
            for (Field f : a.getDeclaredFields()) {
                if (f.isAnnotationPresent(DBField.class)) {
                    fs.add(f);
                }
            }
            String[] columns = new String[fs.size()];
            int where = 0;
            for (int i = 0; i < columns.length; i++) {
                fs.get(i).setAccessible(true);
                columns[i] = fs.get(i).getName().toUpperCase();
                try {
                    Object o = fs.get(i).get(t);
                    if (o != null) {
                        where++;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
            //3、获取where条件
            StringBuilder selectionSB = new StringBuilder("");
            String[] selectionArgs = null;
            if (where > 0) {
                selectionArgs = new String[where];
                where = 0;
                for (int i = 0; i < columns.length; i++) {
                    columns[i] = fs.get(i).getName().toUpperCase();
                    try {
                        Object o = fs.get(i).get(t);
                        if (o != null) {
                            if ("".equals(selectionSB.toString())) {
                                selectionSB.append(" ");
                            } else {
                                selectionSB.append(" and ");
                            }
                            selectionSB.append(columns[i]).append(" like ?");
                            selectionArgs[where] = o.toString();
                            where++;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            //4、分页
            String limit = size != 0 ? (start + "," + size) : "";

            //5、其他暂未支持

            Cursor query = null;
            try {
                query = writableDatabase.query(table, columns, selectionSB.toString(), selectionArgs, null, null, "", limit);
            } catch (SQLiteException e) {
                e.printStackTrace();
                return null;
            }

            while (query.moveToNext()) {
                try {
                    T o = (T) a.newInstance();
                    for (int i = 0; i < fs.size(); i++) {
                        Field field = fs.get(i);
                        String string = query.getString(i);
                        field.set(o, string);
                    }
                    list.add(o);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public <T> void delete(T t) {
        try {
            Class aClass = findEntity(t);
            //1、获取表名
            String table = aClass.getSimpleName();
            //2、获取字段列表
            List<Field> fs = new ArrayList<>();
            for (Field f : aClass.getDeclaredFields()) {
                if (f.isAnnotationPresent(DBField.class)) {
                    fs.add(f);
                }
            }
            String[] columns = new String[fs.size()];
            int where = 0;
            for (int i = 0; i < columns.length; i++) {
                columns[i] = fs.get(i).getName().toUpperCase();
                try {
                    fs.get(i).setAccessible(true);
                    Object o = fs.get(i).get(t);
                    if (o != null) {
                        where++;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
            //3、获取where条件
            StringBuilder selectionSB = new StringBuilder("");
            String[] selectionArgs = null;
            if (where > 0) {
                selectionArgs = new String[where];
                where = 0;
                for (int i = 0; i < columns.length; i++) {
                    columns[i] = fs.get(i).getName().toUpperCase();
                    try {
                        fs.get(i).setAccessible(true);
                        Object o = fs.get(i).get(t);
                        if (o != null) {
                            if ("".equals(selectionSB.toString())) {
                                selectionSB.append(" ");
                            } else {
                                selectionSB.append(" and ");
                            }
                            selectionSB.append(columns[i]).append(" = ?");
                            selectionArgs[where] = o.toString();
                            where++;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            writableDatabase.delete(table, selectionSB.toString(), selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void insert(T t) {
        try {
            Class aClass = findEntity(t);
            StringBuffer sql = new StringBuffer();
            String tableName = aClass.getSimpleName();
            sql.append("INSERT INTO ");
            sql.append(tableName.toUpperCase());
            sql.append("(");

            ContentValues contentValues = new ContentValues();
            StringBuffer fieldsSql = new StringBuffer();
            StringBuffer valueSql = new StringBuffer();
            for (Field f : aClass.getDeclaredFields()) {
                if (f.isAnnotationPresent(DBField.class)) {

                    f.setAccessible(true);
                    try {
                        String value = "";
                        Object o = f.get(t);
                        if (o instanceof String) {
                            value = (String) o;
                            valueSql.append(value);
                        } else {
                            valueSql.append("null");
                        }
                        valueSql.append(",");
                        contentValues.put(f.getName().toUpperCase(), value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        valueSql.append("null,");
                    }

                    fieldsSql.append(f.getName().toUpperCase());
                    fieldsSql.append(",");
                }
            }

            if (",".equals(fieldsSql.substring(fieldsSql.length() - 1, fieldsSql.length()))) {
                fieldsSql.replace(fieldsSql.length() - 1, fieldsSql.length(), "");
            }
            if (",".equals(valueSql.substring(valueSql.length() - 1, valueSql.length()))) {
                valueSql.replace(valueSql.length() - 1, valueSql.length(), "");
            }
            sql.append(fieldsSql);
            sql.append(") VALUES (");
            sql.append(valueSql);
            sql.append(")");

            writableDatabase.insert(tableName, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void insertBitch(List<T> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        writableDatabase.beginTransaction();
        for (Object datum : data) {
            insert(datum);
        }
        writableDatabase.setTransactionSuccessful();
        writableDatabase.endTransaction();
    }

    /**
     * 查找注册的实体
     *
     * @param t
     * @param <T>
     * @return
     * @throws Exception
     */
    private <T> Class findEntity(T t) throws Exception {
        for (Class aClass : entityList) {
            if (t.getClass() == aClass) {
                return aClass;
            }
        }
        throw new Exception("can not find Entity:" + t.getClass().getName() + ", make sure it is registered");
    }

    /**
     * 数据库字段注解
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DBField {
        String value() default "";
    }

    /**
     * 利用字段注解拼接sql
     *
     * @param mClass
     * @param sql
     */
    private void fillSql(Class mClass, StringBuffer sql) {
        Field[] fields = mClass.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(DBField.class)) {
                DBField bind = f.getAnnotation(DBField.class);
                String type = bind.value();
                sql.append(f.getName().toUpperCase());
                sql.append(" ");
                sql.append(type);
                sql.append(",");
            }
        }
    }

    private class SQLiteOpenHelperImpl extends SQLiteOpenHelper {

        public SQLiteOpenHelperImpl(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(new DatabaseContext(context), name, factory, version);
        }

        public SQLiteOpenHelperImpl(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
            super(context, name, factory, version, errorHandler);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            for (Class object : entityList) {
                StringBuffer sql = new StringBuffer();
                String name = object.getSimpleName();

                sql.append("CREATE TABLE ");
                sql.append(name);
                sql.append(" (");
                fillSql(object, sql);

                sql.replace(sql.length() - 1, sql.length(), "");
                sql.append(")");
                sqLiteDatabase.execSQL(sql.toString());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }

    /**
     * 创建数据库辅助类,可以自定义数据库存放路径
     */
    class DatabaseContext extends ContextWrapper {

        public DatabaseContext(Context base) {
            super(base);
        }

        @Override
        public File getDatabasePath(String name) {
            if ("".equals(defaultPath)) {//defaultPath如果为""则放在程序默认位置
                defaultPath = getApplicationContext().getDatabasePath("database").getAbsolutePath();
            }

            File path = new File(defaultPath);
            if (!path.exists()) {
                path.mkdirs();
            }

            File dbfile = new File(path, defaultDBName);

            if (!dbfile.exists()) {
                try {
                    dbfile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return dbfile;
        }

        @Override
        public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                                   SQLiteDatabase.CursorFactory factory,
                                                   DatabaseErrorHandler errorHandler) {
            return openOrCreateDatabase(name, mode, factory);
        }

        @Override
        public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                                   SQLiteDatabase.CursorFactory factory) {
            SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(
                    getDatabasePath(name), null);

            return result;
        }
    }

    interface Register {
        List<Class> run();
    }

    /**
     * 以下为Demo
     * @param args
     */
    public static void main(String[] args) {
        //Demo
        DataBaseUtil db = new DataBaseUtil(WBUIApplication.getInstance(), new Register() {
            @Override
            public List<Class> run() {
                List<Class> objects = new ArrayList<>();
                objects.add(User.class);
                return objects;
            }
        });
        //插入数据
        db.insert(new User("Test"));
        //删除数据
        db.delete(new User("Test"));
        //查询数据(不分页)
        db.query(new User("Test"));
        //查询数据(分页)
        db.query(new User("Test"),0,10);
    }

    /**
     * Demo Entity
     */
    public static class User {
        @DataBaseUtil.DBField("VARCHAR(20)")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public User(String name) {
            this.name = name;
        }
    }
}
