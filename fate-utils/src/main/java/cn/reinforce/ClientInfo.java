package cn.reinforce;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取客户端的信息
 * @author Fate
 *
 */
public class ClientInfo {

	private static final String UNKNOW = "unknown";
	
	private ClientInfo() {
	}

	/**
	 * 获取用户的IP
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request){
		String ip = request.getHeader("x-forwarded-for");
		 if (Strings.isEmpty(ip) || UNKNOW.equalsIgnoreCase(ip)) {
		  ip = request.getHeader("Proxy-Client-IP");
		 }
		 if (Strings.isEmpty(ip) || UNKNOW.equalsIgnoreCase(ip)) {
		  ip = request.getHeader("WL-Proxy-Client-IP");
		 }
		 if (Strings.isEmpty(ip) || UNKNOW.equalsIgnoreCase(ip)) {
		  ip = request.getHeader("http_client_ip");
		 }
		 if (Strings.isEmpty(ip) || UNKNOW.equalsIgnoreCase(ip)) {
		  ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		 }
		 if (Strings.isEmpty(ip) || UNKNOW.equalsIgnoreCase(ip)) {
			  ip = request.getRemoteAddr();
		 }
		 if (ip != null && ip.indexOf(",") != -1) {// 如果是多级代理，那么取第一个ip为客户ip
			  ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
			 }
		return ip;
	}
	
}
