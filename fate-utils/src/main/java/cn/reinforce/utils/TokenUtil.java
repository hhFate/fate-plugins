package cn.reinforce.utils;

public class TokenUtil {

	private static String[] src = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"}; 
	
	/**
	 * 获取随机字符串
	 * @param length
	 * @param type 1.数字验证码 2.token
	 * @return
	 */
	public static String getRandomString(int length,int type){
		String code = "";
		for(int i=0;i<length;i++){
			code+=src[(int)(Math.random()*(type==1?10:16))];
		}
		return code;
	}
	
	public static void main(String[] args) {
		System.out.println(getRandomString(16, 2));
	}
}
