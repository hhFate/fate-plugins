package cn.reinforce.utils.qq4j.qzone;

import com.qq.connect.QQConnect;
import com.qq.connect.QQConnectException;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.utils.QQConnectConfig;
import com.qq.connect.utils.http.PostParameter;

public class UserInfo extends QQConnect{

	private static final long serialVersionUID = -6124397423510235640L;

	  public UserInfo(String token, String openID)
	  {
	    super(token, openID);
	  }

	  private UserInfoBean getUserInfo(String openid, String client_id)
	    throws QQConnectException
	  {
	    return new UserInfoBean(this.client.get(QQConnectConfig.getValue("getUserInfoURL"), new PostParameter[] { new PostParameter("openid", openid), new PostParameter("oauth_consumer_key", client_id), new PostParameter("access_token", this.client.getToken()), new PostParameter("format", "json") }).asJSONObject());
	  }

	  public UserInfoBean getUserInfo(String client_id)
	    throws QQConnectException
	  {
	    return getUserInfo(this.client.getOpenID(), client_id);
	  }
}
