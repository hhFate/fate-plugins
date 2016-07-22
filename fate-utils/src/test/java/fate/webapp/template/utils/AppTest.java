package fate.webapp.template.utils;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import cn.reinforce.Base64;


/**
 * Unit test for simple App.
 */
public class AppTest {
	private static final String HMAC_SHA1 = "HmacSHA1";
	
	public static void main(String[] args) throws ClientProtocolException, IOException, JSONException {
		
//		System.out.println(HttpClientHelper.requestBodyString("http://121.40.32.134:8080/locations", null));
		Map<String, String> para = new HashMap<>();
		para.put("mobile", "18521507352");
//		para.put("uid", "2c908a8b51f6c74e01520a9330be0001");
//		System.out.println(HttpClientHelper.postBodyString("http://127.0.0.1/mobile/v1/smsCode", para));
//		NameValuePair[] pair = {
//				new NameValuePair("state", "2"),
//				new NameValuePair("uid", "2c908a8b51f6c74e01520a9330be0001"),
//				new NameValuePair("timestamp", Long.toString(System.currentTimeMillis())),
//				new NameValuePair("accessToken", "BF18F425BD7853ABBA643EC8B7D92910"),
//		};
//		para.put("state", "2");
//		para.put("uid", "2c908a8b51f6c74e01520a9330be0001");
//		para.put("timestamp", Long.toString(System.currentTimeMillis()));
//		para.put("accessToken", "BF18F425BD7853ABBA643EC8B7D92910");
//		
//		System.out.println(HttpClientUtil.put("http://127.0.0.1/mobile/v1/oppo/state/8a7b73ff525e265201525e85663c0015", pair));
		
//		System.out.println(HttpClientUtil.delete("http://127.0.0.1/mobile/v1/oppo/8a7b73ff525e265201525e85663c0015?uid=2c908a8b51f6c74e01520a9330be0001&timestamp="+Long.toString(System.currentTimeMillis())+"&accessToken=BF18F425BD7853ABBA643EC8B7D92910"));
		System.out.println(Base64.encode("yctpaztxdoytbjcj".getBytes()));
	}
	
	public static String getSignature(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {  
        SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);  
        Mac mac = Mac.getInstance(HMAC_SHA1);  
        mac.init(signingKey);  
        byte[] rawHmac = mac.doFinal(data);  
        return DigestUtils.md5Hex(rawHmac);
    }  
}