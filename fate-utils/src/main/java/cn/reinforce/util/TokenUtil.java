package cn.reinforce.util;

/**
 * Token生成工具
 *
 * @author 幻幻Fate
 * @create 2016-07-28
 * @since 1.0.0
 */
public class TokenUtil {

    private static String[] src = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    /**
     * 获取随机字符串
     *
     * @param length
     * @param type   1.数字验证码 2.token 3.数据+字母的字符串
     * @return
     */
    public static String getRandomString(int length, int type) {
        String code = "";
        int range = 0;
        switch (type) {
            case 1:
                range = 10;
                break;
            case 2:
                range = 16;
                break;
            case 3:
                range = 62;
                break;
            default:
                range = 62;
                break;
        }
        for (int i = 0; i < length; i++) {
            code += src[(int) (Math.random() * range)];
        }
        return code;
    }

}
