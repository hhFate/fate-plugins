package fate.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import fate.utils.entity.HttpResult;

/**
 * 麦卡的短信发送接口
 * 
 * @author Fate
 *
 */
public class MCSmsUtil {

	private static Logger LOG = Logger.getLogger(MCSmsUtil.class);

	private static String URL = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";

	private MCSmsUtil() {
	}

	/**
	 * 发送短信验证码
	 * 
	 * @param mobile
	 * @param smsCode
	 * @return
	 */
	public static String sendCode(String mobile, String smsCode) {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(URL);
		
		String content = new String("您的验证码是：" + smsCode + "。请不要把验证码泄露给其他人。");
		List<BasicNameValuePair> pair = new ArrayList<>();
		pair.add(new BasicNameValuePair("account", "cf_mka"));
		pair.add(new BasicNameValuePair("password", MD5.md5("mka123")));
		pair.add(new BasicNameValuePair("mobile", mobile));
		pair.add(new BasicNameValuePair("content", content));
		String code = null;
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pair, "UTF-8");
			post.setEntity(entity);
			CloseableHttpResponse response = httpclient.execute(post);
			HttpEntity responseEntity = response.getEntity();  
			
			if (responseEntity != null) {  
				String submitResult = EntityUtils.toString(responseEntity, "UTF-8");
				System.out.println(submitResult);
				HttpResult result = new HttpResult();
				result.setStatusCode(response.getStatusLine().getStatusCode());
				result.setResult(submitResult);
				Document doc = DocumentHelper.parseText(submitResult);
				Element root = doc.getRootElement();

				code = root.elementText("code");
				String msg = root.elementText("msg");
				String smsid = root.elementText("smsid");

				if ("2".equals(code)) {
					System.out.println("短信提交成功");
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return code;
	}

	public static void main(String[] args) {
		sendCode("18521507352", "374523");
	}
}