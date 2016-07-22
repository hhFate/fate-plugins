package fate.utils;

import com.github.stuxuhai.jpinyin.PinyinHelper;

public class PinyinUtil {

	public static String getFirst(String src){
		String s = src.substring(0,1);
		return PinyinHelper.getShortPinyin(s);
	}
}
