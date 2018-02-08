package cn.faury.android.library.common.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;


/**
 * 字符处理类
 */
public class StringUtils {

    private final static String QQ_REGULAR_EXPRESSION = "[1-9][0-9]{4,14}";
    private final static String EMAIL_REGULAR_EXPRESSION = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
    private final static String PHONE_REGULAR_EXPRESSION = "^(1)\\d{10}$";
    private final static String ZIPCODE_REGULAR_EXPRESSION = "[1-9]\\d{5}(?!\\d)";// 验证邮编
    private final static String EMPTY = "";

    /**
     * 判断对象是否为空
     *
     * @param input 输入参数
     * @return 是否空字符串
     */
    public static boolean isEmpty(Object input) {
        return input == null || input.toString().trim().length() == 0;
    }

    /**
     * 判断字符串是否为QQ号
     *
     * @param qq 输入参数
     * @return 是否QQ
     */
    public static boolean isQQ(String qq) {
        return isPattern(qq, QQ_REGULAR_EXPRESSION);
    }

    /**
     * 判断字符串是否为email
     *
     * @param email 输入参数
     * @return 是否Email
     */
    public static boolean isEmail(String email) {
        return isPattern(email, EMAIL_REGULAR_EXPRESSION);
    }

    /**
     * 判断字符串是否为手机号
     *
     * @param phone 输入参数
     * @return 是否手机号
     */
    public static boolean isPhoneNumber(String phone) {
        return isPattern(phone, PHONE_REGULAR_EXPRESSION);
    }

    /**
     * 判断字符串是否为邮政编码
     *
     * @param zipCode 输入参数
     * @return 是否邮政编码
     */
    public static boolean isZipCode(String zipCode) {
        return isPattern(zipCode, ZIPCODE_REGULAR_EXPRESSION);
    }

    /**
     * 转换为字符串，为空时返回""
     *
     * @param str 输入参数
     * @return 转换为字符串
     */
    public static String clean(Object str) {
        return isEmpty(str) ? EMPTY : str.toString();
    }

    /**
     * 使用UTF8编码
     *
     * @param str 输入参数
     * @return 转换为UTF8字符串
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * str compare to md5Str.
     *
     * @param org    输入参数
     * @param md5Str md5密文
     * @return 是否相同
     */
    public static boolean md5StrCompare(Object org, String md5Str) {
        String md5_org = MD5Utils.getMD5Str(org);
        return md5_org.equals(md5Str);
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source 输入字符串
     * @return 是否包含Emoji
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) {
                // 如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return 是否Emoji
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    private static boolean isPattern(String string, String pattern) {
        if (isEmpty(string) || isEmpty(pattern)) {
            return false;
        }
        Pattern emailer = Pattern.compile(pattern);
        return emailer.matcher(string).matches();
    }


    /**
     * 判断对象是否为空
     *
     * @param input 待判断的字符串
     * @return 对象是否为空布尔值
     */
    public static boolean isEmpty(String input) {
        return input == null || input.trim().length() == 0
                || "null".equals(input.trim());
    }

    /**
     * 判断对象是否为空
     *
     * @param input 待判断的字符串
     * @return 对象是否为空布尔值
     */
    public static boolean isNotEmpty(String input) {
        return !isEmpty(input);
    }

    /**
     * 判断字符串是否为JSON字符串
     *
     * @param input 传入判断的字符串
     * @return 字符串是否为JSON字符串
     */
    public static boolean isJson(String input) {
        boolean isJson = false;

        if (isJSONObject(input) || isJSONArray(input)) {
            isJson = true;
        }
        return isJson;
    }

    /**
     * 判断字符串是否为JSONObject
     *
     * @param input 传入判断的字符串
     * @return 字符串是否为JSONObject
     */
    private static boolean isJSONObject(String input) {
        boolean isJSONObject = false;
        if (input.startsWith("{") && input.endsWith("}")) {
            try {
                JSONObject json = new JSONObject(input);
                isJSONObject = true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return isJSONObject;
    }

    /**
     * 判断字符串是否为JSONArray
     *
     * @param input 传入判断的字符串
     * @return 字符串是否为JSONArray
     */
    private static boolean isJSONArray(String input) {
        boolean isJSONArray = false;
        if (input.startsWith("[") && input.endsWith("]")) {
            input = "{\"fakelist\":" + input + "}";
            try {
                JSONObject jsonObject = new JSONObject(input);
                isJSONArray = true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return isJSONArray;
    }

}
