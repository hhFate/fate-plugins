package cn.reinforce.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一堆正则表达式
 *
 * @author hhFate
 * @create 2017-01-04
 * @since 1.0.1
 */
public class RegularUtil {

    /**
     * 判断手机号的格式
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile){
        if(Strings.isEmpty(mobile)){
            return false;
        }
        Pattern pattern = Pattern.compile("^13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|18[0-9]{9}$|17[0-9]{9}$");
        Matcher matcher = pattern.matcher(mobile);
        return !matcher.find();
    }

    /**
     * 检查邮箱格式
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        if(Strings.isEmpty(email)){
            return false;
        }
        Pattern pattern = Pattern.compile("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        Matcher matcher = pattern.matcher(email);
        return !matcher.find();
    }
}
