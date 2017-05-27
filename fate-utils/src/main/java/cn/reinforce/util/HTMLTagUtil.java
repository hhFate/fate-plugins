package cn.reinforce.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTML处理工具
 * @author hhFate
 * @create 2016-08-17
 * @since 1.0.0
 */
public class HTMLTagUtil{

	
	
	private HTMLTagUtil() {
	}

	/**
	 * 去掉所有的HTML标签和空白符号
	 * 
	 * @param htmlStr
	 * @return
	 */
	public static String delHTMLTag(String htmlStr) {
		String script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
		String style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
		String html = "<[^>]+>"; // 定义HTML标签的正则表达式
		String space = "(\r?\n(\\s*\r?\n)+)";
		String white = "&nbsp;";

		Pattern p_script = Pattern.compile(script,
				Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern
				.compile(style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		Pattern p_space = Pattern
				.compile(space, Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll("");// 过滤空白数据

		htmlStr = htmlStr.replaceAll(white, "");// 过滤&nbsp;

		return htmlStr.trim(); // 返回文本字符串
	}

}
