package com.danny.media.library.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tingw on 2018/1/2.
 */

public class Util {
    /**
     *  判断是否是中文
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS);
    }

    /**
     * 判断是否乱码
     */
    public static boolean isGarbledCode(String str) {
        if (null == str || 0 == str.trim().length()) {
            return true;
        }
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
        Matcher m = p.matcher(str);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;

        //foreach效率更高
        for (int i : ch) {
            if (i < ch.length) {
                char c = ch[i];
                if (!Character.isLetterOrDigit(c)) {
                    if (!isChinese(c)) {
                        count = count + 1;
                    }
                }
            }
        }

        float result = count / chLength;
        return (result > 0.4);
    }
}
