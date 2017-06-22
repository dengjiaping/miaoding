package cn.cloudworkshop.miaoding.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Author：Libin on 2016/10/31 15:20
 * Email：1993911441@qq.com
 * Describe：输入中英文判断
 */
public class CharacterUtils {

    /**
     * 输入中文
     */

    public static boolean inputEnglish(Activity activity,String charaString){
        if (charaString.matches("^[a-zA-Z]*")){
            return true;
        }else {
            Toast.makeText(activity, "请输入英文", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    /**
     * 输入中文
     */
    public static boolean inputChinese(Activity activity, String txt) {

        boolean res=true;
        char [] cTemp = txt.toCharArray();
        for(int i=0;i<txt.length();i++)
        {
            if(!isChinese(cTemp[i]))
            {
                Toast.makeText(activity, "请输入中文", Toast.LENGTH_SHORT).show();
                res=false;
                break;
            }
        }
        return res;


    }

    /**
     * 判定输入汉字
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }


}
