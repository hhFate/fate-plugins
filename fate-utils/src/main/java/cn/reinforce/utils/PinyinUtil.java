package cn.reinforce.utils;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;

public class PinyinUtil {

	public static String getFirst(String src){
		String s = src.substring(0,1);
		try {
			return PinyinHelper.getShortPinyin(s);
		} catch (PinyinException e) {
			e.printStackTrace();
		}
		return null;
	}
}
