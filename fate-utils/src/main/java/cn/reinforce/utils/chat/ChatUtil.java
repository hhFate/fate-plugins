package cn.reinforce.utils.chat;

import java.util.ArrayList;
import java.util.List;

import cn.reinforce.utils.HttpClientUtil;
import cn.reinforce.utils.entity.HttpResult;
import cn.reinforce.utils.GsonUtil;
import cn.reinforce.utils.entity.CommonResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

/**
 * 聊天服务器的对外接口API
 * @author Fate
 *
 */
public class ChatUtil {

	/**
	 * 服务器地址
	 */
	private static String endpoint;
	
	/**
	 * 同步密钥
	 */
	private static String appSecret;
	
	private ChatUtil() {
		super();
	}

	public static void init(String ep, String appSec){
		endpoint = ep;
		appSecret = appSec;
	}

	public static CommonResponse createDept(String id, String name, String desc, String parent){
		List<NameValuePair> pairs = new ArrayList<>();
		pairs.add(new BasicNameValuePair("synId", id));
		pairs.add(new BasicNameValuePair("name", name));
		pairs.add(new BasicNameValuePair("desc", desc));
		pairs.add(new BasicNameValuePair("parent", parent));
		pairs.add(new BasicNameValuePair("appSecret", appSecret));
		String result = HttpClientUtil.post(endpoint+"op/syn/dept", pairs).getResult();
		Gson gson = GsonUtil.getGson();
		System.out.println(result);
		return gson.fromJson(result, CommonResponse.class);
	}

	public static CommonResponse updateDept(String id, String name, String desc){
		List<NameValuePair> pairs = new ArrayList<>();
		pairs.add(new BasicNameValuePair("name", name));
		pairs.add(new BasicNameValuePair("desc", desc));
		pairs.add(new BasicNameValuePair("appSecret", appSecret));
		String result = HttpClientUtil.put(endpoint+"op/syn/dept/name/"+id, pairs).getResult();
		Gson gson = GsonUtil.getGson();
		return gson.fromJson(result, CommonResponse.class);
	}

	public static CommonResponse deleteDept(String id){
		List<NameValuePair> pairs = new ArrayList<>();
		pairs.add(new BasicNameValuePair("appSecret", appSecret));
		String result = HttpClientUtil.put(endpoint+"op/syn/dept/"+id, pairs).getResult();
		Gson gson = GsonUtil.getGson();
		return gson.fromJson(result, CommonResponse.class);
	}

	public static CommonResponse createUser(String uid, String realName, String username, String password, String mobile, String headIcon, String department){
		List<NameValuePair> pairs = new ArrayList<>();
		pairs.add(new BasicNameValuePair("synId", uid));
		pairs.add(new BasicNameValuePair("nickName", realName));
		pairs.add(new BasicNameValuePair("username", username));
		pairs.add(new BasicNameValuePair("password", password));
		pairs.add(new BasicNameValuePair("mobile", mobile));
		pairs.add(new BasicNameValuePair("headIcon", headIcon));
		pairs.add(new BasicNameValuePair("department", department));
		pairs.add(new BasicNameValuePair("appSecret", appSecret));
		String result = HttpClientUtil.post(endpoint+"op/syn/user", pairs).getResult();
		Gson gson = GsonUtil.getGson();
		System.out.println(result);
		return gson.fromJson(result, CommonResponse.class);
	}

	public static CommonResponse updateUserDept(String uid, String deptId){
		List<NameValuePair> pairs = new ArrayList<>();
		pairs.add(new BasicNameValuePair("deptId", deptId));
		pairs.add(new BasicNameValuePair("appSecret", appSecret));
		String result = HttpClientUtil.put(endpoint+"op/syn/user/user/dept/"+uid, pairs).getResult();
		Gson gson = GsonUtil.getGson();
		return gson.fromJson(result, CommonResponse.class);
	}

	public static CommonResponse updateChatToken(String uid, String chatToken){
		List<NameValuePair> pairs = new ArrayList<>();
		pairs.add(new BasicNameValuePair("chatToken", chatToken));
		pairs.add(new BasicNameValuePair("appSecret", appSecret));
		String result = HttpClientUtil.put(endpoint+"op/syn/user/chatToken/"+uid, pairs).getResult();
		Gson gson = GsonUtil.getGson();
		return gson.fromJson(result, CommonResponse.class);
	}

	public static CommonResponse updateNickname(String uid, String nickname){
		List<NameValuePair> pairs = new ArrayList<>();
		pairs.add(new BasicNameValuePair("nickname", nickname));
		pairs.add(new BasicNameValuePair("appSecret", appSecret));
		String result = HttpClientUtil.put(endpoint+"op/syn/user/nickname/"+uid, pairs).getResult();
		Gson gson = GsonUtil.getGson();
		return gson.fromJson(result, CommonResponse.class);
	}
	
	public static CommonResponse updateHeadIcon(String uid, String headIcon){
		List<NameValuePair> pairs = new ArrayList<>();
		pairs.add(new BasicNameValuePair("headIcon", headIcon));
		pairs.add(new BasicNameValuePair("appSecret", appSecret));
		String result = HttpClientUtil.put(endpoint+"op/syn/user/headIcon/"+uid, pairs).getResult();
		Gson gson = GsonUtil.getGson();
		return gson.fromJson(result, CommonResponse.class);
	}
	
	public static CommonResponse updatePassword(String uid, String password){
		List<NameValuePair> pairs = new ArrayList<>();
		pairs.add(new BasicNameValuePair("password", password));
		pairs.add(new BasicNameValuePair("appSecret", appSecret));
		String result = HttpClientUtil.put(endpoint+"op/syn/user/password/"+uid, pairs).getResult();
		System.out.println(result);
		Gson gson = GsonUtil.getGson();
		return gson.fromJson(result, CommonResponse.class);
	}
	
	public static CommonResponse updateMobile(String uid, String mobile){
		List<NameValuePair> pairs = new ArrayList<>();
		pairs.add(new BasicNameValuePair("mobile", mobile));
		pairs.add(new BasicNameValuePair("appSecret", appSecret));
		String result = HttpClientUtil.put(endpoint+"op/syn/user/mobile/"+uid, pairs).getResult();
		Gson gson = GsonUtil.getGson();
		return gson.fromJson(result, CommonResponse.class);
	}

	public static CommonResponse deleteUser(String uids){
		List<NameValuePair> pairs = new ArrayList<>();
		pairs.add(new BasicNameValuePair("appSecret", appSecret));
		String result = HttpClientUtil.put(endpoint+"op/syn/user/"+uids, pairs).getResult();
		Gson gson = GsonUtil.getGson();
		return gson.fromJson(result, CommonResponse.class);
	}
	
	public static CommonResponse createGroup(String creator, String groupName, String desc, String members, String thirdId){
		List<NameValuePair> pair = new ArrayList<>();
		pair.add(new BasicNameValuePair("creator", creator));
		pair.add(new BasicNameValuePair("groupName", groupName));
		pair.add(new BasicNameValuePair("desc", desc));
		pair.add(new BasicNameValuePair("members", members));
		pair.add(new BasicNameValuePair("thirdId", thirdId));
		pair.add(new BasicNameValuePair("appSecret", appSecret));
		HttpResult result = HttpClientUtil.post(endpoint+"/op/syn/group/", pair);
		Gson gson = GsonUtil.getGson();
		return gson.fromJson(result.getResult(), CommonResponse.class);
	}

	public static CommonResponse addUserToGroup(String uid, String thirdId){
		List<NameValuePair> pair = new ArrayList<>();
		pair.add(new BasicNameValuePair("synId", uid));
		pair.add(new BasicNameValuePair("thirdId", thirdId));
		pair.add(new BasicNameValuePair("appSecret", appSecret));
		HttpResult result = HttpClientUtil.post(endpoint+"/op/syn/group/user", pair);
		Gson gson = GsonUtil.getGson();
		return gson.fromJson(result.getResult(), CommonResponse.class);
	}
}
