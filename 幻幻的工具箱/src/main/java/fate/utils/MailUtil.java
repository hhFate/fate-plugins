package fate.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MailUtil {

	
	private MailUtil() {
	}

	/**
	 * 截取邮件中的html部分
	 * @param mailContent
	 * @return
	 */
	public static String getHtml(String mailContent){
		if(mailContent.indexOf("<!DOCTYPE")!=-1)
			mailContent = mailContent.substring(mailContent.indexOf("<!DOCTYPE"));
		else if(mailContent.indexOf("<!doctype")!=-1)
			mailContent = mailContent.substring(mailContent.indexOf("<!doctype"));
		else if(mailContent.indexOf("<html>")!=-1)
			mailContent= mailContent.substring(mailContent.indexOf("<html>"));
		else if(mailContent.indexOf("<div")!=-1){
			mailContent= mailContent.substring(mailContent.indexOf("<div"));
			mailContent = "<html><body>"+mailContent+"</body></html>";
		}else if(mailContent.indexOf("<p")!=-1){
			mailContent= mailContent.substring(mailContent.indexOf("<p"));
		}
		else
			return "";
		return mailContent;
	}
	
	public static boolean hasHtml(String content){
		String reg = "<[^>]*>";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(content);
		return matcher.find();
	}
}
