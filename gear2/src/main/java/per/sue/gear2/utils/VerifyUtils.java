package per.sue.gear2.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2016/5/4
*/
public class VerifyUtils {


    /**
     * 检测手机号有效性*
     *
     * @param mobile 手机号码
     * @return 是否有效
     */
    public static final boolean isMobileNo(String mobile) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(17[6-8]))\\d{8}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }
    //判断email格式是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }
    //判断email格式是否正确
    public static boolean isUrl(String url) {
        String str = "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&amp;%\\$\\-]+)*@)*((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|localhost|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(\\:[0-9]+)*(/($|[a-zA-Z0-9\\.\\,\\?\\'\\\\\\+&amp;%\\$#\\=~_\\-]+))*$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(url);

        return m.matches();
    }
    /**
     * 检测是否纯数字
     *
     * @param
     * @return 是否有效
     */
    public static final boolean isNumber(String number) {
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher((CharSequence) number);
        boolean result = matcher.matches();
        return result;
    }

    /**
     * 检测是否纯字母
     *
     * @param
     * @return 是否有效
     */
    public static final boolean isLetter(String str) {
        Pattern pattern = Pattern.compile("[a-zA-Z]{1,}");
        Matcher matcher = pattern.matcher((CharSequence) str);
        boolean result = matcher.matches();
        return result;
    }



    /**
     * 验证验证码
     * @param code
     * @return
     */
    public static boolean isCode(String code){
        Pattern pattern  = Pattern.compile("[0-9]{5}$"); // 验证验证码
        Matcher matcher = pattern.matcher(code);
        boolean result = matcher.matches();
        return result;
    }
}
