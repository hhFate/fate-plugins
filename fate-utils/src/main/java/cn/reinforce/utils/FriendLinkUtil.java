package cn.reinforce.utils;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 友链工具
 * @author hhFate
 * @create 2016-09-06
 * @since 1.0.0
 */
public class FriendLinkUtil {

	private static Logger LOG = Logger.getLogger(FriendLinkUtil.class);
	
	private FriendLinkUtil() {
	}

	/**
	 * 检测网站的友链是否互链
	 * @param url
	 * @return
	 */
	public static boolean checkLink(String website, String url){
		try {
		       Document target = Jsoup.connect(url).header("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2").get();
		       return target.toString().contains(website);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error("网站解析错误", e);
		}
	    return false;
	}
}
