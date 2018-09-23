package xyz.wbsite.wbsiteui.utils;

import java.util.Calendar;
import java.util.Locale;

public class IdCardUtil {
    /**
     * 中国公民身份证号码最小长度。
     */
    public final int CHINA_ID_MIN_LENGTH = 15;

    /**
     * 中国公民身份证号码最大长度。
     */
    public final int CHINA_ID_MAX_LENGTH = 18;

    /**
     * 根据身份编号获取年龄
     *
     * @param idCard 身份编号
     * @return 年龄
     */
    public static int getAgeByIdCard(String idCard) {
        int iAge = 0;
        Calendar cal = Calendar.getInstance();
        String year = idCard.substring(6, 10);
        int iCurrYear = cal.get(Calendar.YEAR);
        iAge = iCurrYear - Integer.valueOf(year);
        return iAge;
    }

    /**
     * 根据身份编号获取生日
     *
     * @param idCard 身份编号
     * @return 生日(yyyyMMdd)
     */
    public static String getBirthByIdCard(String idCard) {
        return idCard.substring(6, 14);
    }

    /**
     * 根据身份编号获取生日年
     *
     * @param idCard 身份编号
     * @return 生日(yyyy)
     */
    public static String getYearByIdCard(String idCard) {
        return idCard.substring(6, 10);
    }

    public static String getYearString(String idCard) {
        return idCard.substring(6, 10);
    }

    /**
     * 根据身份编号获取生日月
     *
     * @param idCard 身份编号
     * @return 生日(MM)
     */
    public static String getMonthByIdCard(String idCard) {
        return idCard.substring(10, 12);
    }

    /**
     * 根据身份编号获取生日天
     *
     * @param idCard 身份编号
     * @return 生日(dd)
     */
    public static String getDateByIdCard(String idCard) {
        return idCard.substring(12, 14);
    }

    /**
     * 根据身份编号获取性别
     *
     * @param idCard 身份编号
     * @return 性别(M-男，F-女，N-未知)
     */
    public static String getGenderByIdCard(String idCard) {
        String sGender = "未知";

        String sCardNum = idCard.substring(16, 17);
        if (Integer.parseInt(sCardNum) % 2 != 0) {
            sGender = "1";//男
        } else {
            sGender = "2";//女
        }
        return sGender;
    }

    public static String getCSRQ(String idCard) {
        String nian = getYearByIdCard(idCard);
        String yue = getMonthByIdCard(idCard);
        String ri = getDateByIdCard(idCard);

        return String.format(Locale.CHINA, "%s%s%s", nian, yue, ri);
    }

    public static String getXzqh(String idCard) {
        return idCard.substring(0, 6);
    }


    public static void main(String[] a) {
        String idcard = "460200199209275127";
        String sex = getGenderByIdCard(idcard);
        System.out.println("性别:" + sex);
        int age = getAgeByIdCard(idcard);
        System.out.println("年龄:" + age);
        String nian = getYearByIdCard(idcard);
        String yue = getMonthByIdCard(idcard);
        String ri = getDateByIdCard(idcard);
        System.out.print(nian + "年" + yue + "月" + ri + "日");

        String sr = getBirthByIdCard(idcard);
        System.out.println("生日:" + sr);
    }

    private static int[] w = {7, 9, 10, 5, 8, 4, 2, 1, 6,
            3, 7, 9, 10, 5, 8, 4, 2};

    public static boolean isValidate(String id) {
        if (id == null || id.length() != 18) {
            return false;
        }
        char[] c = id.toCharArray();
        int sum = 0;
        for (int i = 0; i < w.length; i++) {
            sum += (c[i] - '0') * w[i];
        }
        System.out.println(sum);
        char[] verifyCode = "10X98765432".toCharArray();
        char ch = verifyCode[sum % 11];
        System.out.println(ch);
        return c[17] == ch;
    }

}