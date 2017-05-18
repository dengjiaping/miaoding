package cn.cloudworkshop.miaoding.utils;

import android.text.TextUtils;

/**
 * Author：Libin on 2016-10-20 12:07
 * Email：1993911441@qq.com
 * Describe：手机号判断
 */

public class PhoneNumberUtils {
    public static boolean judgePhoneNumber(String phoneNumber) {
        if (isMatchLength(phoneNumber, 11)
                && isMobileNO(phoneNumber)) {
            return true;
        }
        return false;
    }


    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.getBytes().length == length ? true : false;
        }
    }

    /**
     * 验证手机号码格式
     */
    public static boolean isMobileNO(String mobileNums) {
    /*  电信
        133、153、180、181、189、177、173、149
        联通
        130、131、132、155、156、145、185、186、176、175
        移动
        1340-1348、135、136、137、138、139、150、151、152、157、158、159、182、183、184、187、188、147、178
     */
        String telRegex = "[1][34578]\\d{9}";// "[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }
}
