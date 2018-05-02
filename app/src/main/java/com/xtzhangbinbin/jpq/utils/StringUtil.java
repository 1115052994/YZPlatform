package com.xtzhangbinbin.jpq.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 关于字符串的帮助类
 */
public class StringUtil {

    private static final String SEP1 = "#";
    private static final String SEP2 = "|";
    private static final String SEP3 = "=";

    /**
     * 截取某字符之后的字符串
     *
     * @param s -
     * @return -
     */
    public static String getLaterString(String s, String later) {
        return s.substring(s.indexOf(later) + 1, s.length());
    }

    /**
     * 截取数字之前的字符串
     *
     * @param s
     * @return
     */
    public static String getAddress(String s) {
        String c = "1234567890";
        for (int i = 0; i < s.length(); i++) {
            String ch = String.valueOf(s.charAt(i));
            if (c.contains(ch)) {
                return getBeforeString(s, ch);
            }
        }
        return s;
    }


    /**
     * 截取之前的字符串
     *
     * @param s -
     * @return -
     */
    public static String getBeforeString(String s, String before) {
        return s.substring(0, s.indexOf(before));
    }

    public static boolean getChinaString(String s) {
        String str = s.substring(0, 2);
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");

        for (int i = 0; i < str.length(); i++) {
            Matcher m = p.matcher(String.valueOf(str.charAt(i)));
            if (m.matches()) {
                if (i == 1 && !String.valueOf(str.charAt(i)).equals("牌")) {
                    return true;
                }
            } else {
                break;
            }
        }
        return false;
    }

    /**
     * List转换String
     *
     * @param list
     *            :需要转换的List
     * @return String转换后的字符串
     */
    public static String listToString(List<String> list){
        if(list==null){
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for(String string :list) {
            if(first) {
                first=false;
            }else{
                result.append(",");
            }
            result.append(string);
        }
        return result.toString();
    }


    public static List<String> stringToList(String strs){
        String str[] = strs.split(",");
        return Arrays.asList(str);
    }






    /**
   * 验证手机格式是否正确
   */
    public static boolean isPhoneNum(String phone){
        /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String num = "[1][34578]\\d{9}";
        //matches():字符串是否在给定的正则表达式匹配
        return !TextUtils.isEmpty(phone) && phone.matches(num);
    }

    /**
     * MD5 摘要
     *
     * @param string 字符串
     * @return 处理后的字符串
     */
    public static String getStringMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * String是否为空
     *
     * @param str 字符串
     * @return true 为空
     */
    public static boolean isEmpty(String str) {
        return "".equals(str) || str == null;
    }

    /**
     * 去重(去除重复图片)
     *
     * @param list 要去重的集合
     * @return 返回一个集合
     */
    public static ArrayList<String> judgmentRepetition(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {  //外循环是循环的次数
            for (int j = list.size() - 1; j > i; j--) {  //内循环是 外循环一次比较的次数
                if (list.get(i).equals(list.get(j))) {
                    list.remove(j);
                }
            }
        }
        return list;
    }

    /**
     * 是否是字母
     */
    public static boolean isLetter(String s) {
        return (s.charAt(0) <= 'Z' && s.charAt(0) >= 'A') || (s.charAt(0) <= 'z' && s.charAt(0) >=
                'a');
    }

    /**
     * 是否是英文字母或数字
     *
     * @param str 需要判断的字符串
     * @return true--合格； false--不合格
     */
    public static boolean isLetterOrDigit(String str) {
        char[] chars = str.toCharArray();
        for (char ch : chars) {
            if (!(('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z') || ('0' <= ch && ch <= '9'))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 设置车牌号码显示格式
     *
     * @param licensePlate 车牌号 如【鲁A01234】
     * @return 处理后的车牌号  如【鲁  A  01234】
     */
    public static String getLicensePlateDispose(String licensePlate) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < licensePlate.toCharArray().length; i++) {
            builder.append(licensePlate.charAt(i));
            if (i == 0 || i == 1) {
                builder.append("\b\b");//两个半角空格
            }
        }
        return new String(builder);
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap Bytes2Bitmap(byte[] b) {
        if (b != null) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
}
