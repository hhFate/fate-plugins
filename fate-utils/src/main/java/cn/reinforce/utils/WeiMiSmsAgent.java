package cn.reinforce.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/**
 * 微米的短信接口
 * 
 * @author hhFate
 * @create 2016-09-06
 * @since 1.0.0
 */
public class WeiMiSmsAgent {

	private WeiMiSmsAgent() {
	}

	public static Map<String, Object> sendSms(String mobiles, String code, int timeout) {

		Map<String, Object> result = new HashMap<String, Object>();
		List<NameValuePair> pair = new ArrayList<>();
		/**
		 * 目标手机号码，多个以“,”分隔，一次性调用最多100个号码，示例：139********,138********
		 */
		pair.add(new BasicNameValuePair("mob", mobiles));
		/**
		 * 微米账号的接口UID
		 */
		pair.add(new BasicNameValuePair("uid", "8TWLspIskXMH"));
		/**
		 * 微米账号的接口密码
		 */
		pair.add(new BasicNameValuePair("pas", "8uf8m7pj"));
		/**
		 * 接口返回类型：json、xml、txt。默认值为txt
		 */
		pair.add(new BasicNameValuePair("type", "json"));
		/**
		 * 短信模板cid，通过微米后台创建，由在线客服审核。必须设置好短信签名，签名规范： <br>
		 * 1、模板内容一定要带签名，<span class='label label-success'>签名放在模板内容的最前面</span>；
		 * <br>
		 * 2、签名格式：【***】，签名内容为三个汉字以上（包括三个）；<br>
		 * 3、短信内容不允许双签名，即短信内容里只有一个“【】”<br>
		 */
		pair.add(new BasicNameValuePair("cid", "ohGOgRMLLSPl"));
		/**
		 * 传入模板参数。<br>
		 * <br>
		 * 短信模板示例：<br>
		 * 【微米网】您的验证码是：%P%，%P%分钟内有效。如非您本人操作，可忽略本消息。<br>
		 * <br>
		 * 传入两个参数：<br>
		 * p1：610912<br>
		 * p2：3<br>
		 * 最终发送内容：<br>
		 * 【微米网】您的验证码是：610912，3分钟内有效。如非您本人操作，可忽略本消息。
		 */
		pair.add(new BasicNameValuePair("p1", "code"));
		pair.add(new BasicNameValuePair("p2", Integer.toString(timeout)));

		String res = "";

		try {

			res = HttpClientUtil.post("http://api.weimi.cc/2/sms/send.html", pair).getResult();
			JSONObject dataJson = new JSONObject(res);
			result.put("code", dataJson.get("code"));
			result.put("msg", dataJson.get("msg"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
