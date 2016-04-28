
package org.harvest.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Str {
    public static int str2Int(String str) {
        return str2Int(str, 0);
    }

    public static int str2Int(String str, int defaultValue) {
        if (str != null) {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {

            }
        }
        return defaultValue;
    }

    public static long str2Long(String str) {
        return str2Long(str, 0);
    }

    public static long str2Long(String str, long defaultValue) {
        if (str != null) {
            try {
                return Long.parseLong(str);
            } catch (NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    public static String getStr(String str) {
        return str == null ? "" : str;
    }

    public static boolean isNull(String str) {
        return str == null || "".equals(str.trim());
    }

    public static String encoder(String str) {
        if (!isNull(str)) {
            try {
                str = URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
        }
        return str;
    }

    public static String md5(String string, String charsetName) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes(charsetName));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
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
     * 字符匹配方法
     * 
     * @param patternStr
     * @param content
     * @param find
     * @param group
     * @return
     */
    public static String getMatcher(String patternStr, String content, int find, int group) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(content);
        String result = "";
        int i = 0;
        while (matcher.find()) {
            if (i == find) {
                result = matcher.group(group);
                break;
            } else if (find == -1) {
                result = matcher.group(group);
            }
            ++i;
        }
        return result;
    }
}
