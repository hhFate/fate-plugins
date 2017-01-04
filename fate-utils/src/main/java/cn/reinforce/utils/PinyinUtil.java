package cn.reinforce.utils;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * 基于JPinyin的工具封装
 *
 * @author hhFate
 * @create 2016-09-06
 * @since 1.0.0
 */
public class PinyinUtil {

	/**
	 * 获取第一个中文的首字母
	 * @param src
	 * @return
	 */
	public static String getFirst(String src){
		if(Strings.isEmpty(src)){
			return null;
		}
		String s = src.substring(0,1);
		try {
			return PinyinHelper.getShortPinyin(s);
		} catch (PinyinException e) {
			e.printStackTrace();
		}
		return null;
	}
}
