package fate.qq4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.qq.connect.QQConnect;
import com.qq.connect.QQConnectException;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.utils.QQConnectConfig;
import com.qq.connect.utils.RandomStatusGenerator;
import com.qq.connect.utils.http.PostParameter;

/**
 * 对原qq登录的API做了点修改，不再从文件读取授权信息，而是从数据库中读取
 * @author Fate
 *
 */
public class Oauth extends QQConnect
{
  private static final long serialVersionUID = -7860508274941797293L;

  private String[] extractionAuthCodeFromUrl(String url)
    throws QQConnectException
  {
    if (url == null) {
      throw new QQConnectException("you pass a null String object");
    }
    Matcher m = Pattern.compile("code=(\\w+)&state=(\\w+)&?").matcher(url);
    String authCode = "";
    String state = "";
    if (m.find()) {
      authCode = m.group(1);
      state = m.group(2);
    }

    return new String[] { authCode, state };
  }

  public AccessToken getAccessTokenByRequest(ServletRequest request, String client_id, String client_secret, String redirect_uri)
    throws QQConnectException
  {
    String queryString = ((HttpServletRequest)request).getQueryString();
    if (queryString == null) {
      return new AccessToken();
    }
    String state = (String)((HttpServletRequest)request).getSession().getAttribute("qq_connect_state");
    if ((state == null) || (state.equals(""))) {
      return new AccessToken();
    }

    String[] authCodeAndState = extractionAuthCodeFromUrl(queryString);
    String returnState = authCodeAndState[1];
    String returnAuthCode = authCodeAndState[0];

    AccessToken accessTokenObj = null;

    if ((returnState.equals("")) || (returnAuthCode.equals("")))
    {
      accessTokenObj = new AccessToken();
    }
    else if (!state.equals(returnState))
    {
      accessTokenObj = new AccessToken();
    }
    else accessTokenObj = new AccessToken(this.client.post(QQConnectConfig.getValue("accessTokenURL"), new PostParameter[] { new PostParameter("client_id", client_id), new PostParameter("client_secret", client_secret), new PostParameter("grant_type", "authorization_code"), new PostParameter("code", returnAuthCode), new PostParameter("redirect_uri", redirect_uri) }, Boolean.valueOf(false)));

    return accessTokenObj;
  }

  /** @deprecated */
  public AccessToken getAccessTokenByQueryString(String queryString, String state, String client_id, String client_secret, String redirect_uri)
    throws QQConnectException
  {
    if (queryString == null) {
      return new AccessToken();
    }

    String[] authCodeAndState = extractionAuthCodeFromUrl(queryString);
    String returnState = authCodeAndState[1];
    String returnAuthCode = authCodeAndState[0];

    AccessToken accessTokenObj = null;

    if ((returnState.equals("")) || (returnAuthCode.equals("")))
    {
      accessTokenObj = new AccessToken();
    }
    else if (!state.equals(returnState))
    {
      accessTokenObj = new AccessToken();
    }
    else accessTokenObj = new AccessToken(this.client.post(QQConnectConfig.getValue("accessTokenURL"), new PostParameter[] { new PostParameter("client_id", client_id), new PostParameter("client_secret", client_secret), new PostParameter("grant_type", "authorization_code"), new PostParameter("code", returnAuthCode), new PostParameter("redirect_uri", redirect_uri) }, Boolean.valueOf(false)));

    return accessTokenObj;
  }

  /** @deprecated */
  public String getAuthorizeURL(String scope, String state, String client_id, String redirect_uri)
    throws QQConnectException
  {
    return QQConnectConfig.getValue("authorizeURL").trim() + "?client_id=" + client_id.trim() + "&redirect_uri=" + redirect_uri.trim() + "&response_type=" + "code" + "&state=" + state + "&scope=" + scope;
  }

  /** @deprecated */
  public String getAuthorizeURL(String state, String client_id, String redirect_uri)
    throws QQConnectException
  {
    String scope = QQConnectConfig.getValue("scope");
    if ((scope != null) && (!scope.equals(""))) {
      return getAuthorizeURL("code", state, scope, client_id, redirect_uri);
    }
    return QQConnectConfig.getValue("authorizeURL").trim() + "?client_id=" + client_id.trim() + "&redirect_uri=" + redirect_uri.trim() + "&response_type=" + "code" + "&state=" + state;
  }

  /** @deprecated */
  public String getAuthorizeURLByScope(String scope, ServletRequest request, String client_id, String redirect_uri)
    throws QQConnectException
  {
    String state = RandomStatusGenerator.getUniqueState();
    ((HttpServletRequest)request).setAttribute("qq_connect_state", state);

    return QQConnectConfig.getValue("authorizeURL").trim() + "?client_id=" + client_id.trim() + "&redirect_uri=" + redirect_uri.trim() + "&response_type=" + "code" + "&state=" + state + "&scope=" + scope;
  }

  public String getAuthorizeURL(ServletRequest request,String client_id, String redirect_uri)
    throws QQConnectException
  {
    String state = RandomStatusGenerator.getUniqueState();
    ((HttpServletRequest)request).getSession().setAttribute("qq_connect_state", state);
    String scope = QQConnectConfig.getValue("scope");
    if ((scope != null) && (!scope.equals(""))) {
      return getAuthorizeURL("code", state, scope, client_id, redirect_uri);
    }
    return QQConnectConfig.getValue("authorizeURL").trim() + "?client_id=" + client_id.trim() + "&redirect_uri=" + redirect_uri.trim() + "&response_type=" + "code" + "&state=" + state;
  }

  /** @deprecated */
  public String getAuthorizeURL(String response_type, String state, String scope, String client_id, String redirect_uri)
    throws QQConnectException
  {
    return QQConnectConfig.getValue("authorizeURL").trim() + "?client_id=" + client_id.trim() + "&redirect_uri=" + redirect_uri.trim() + "&response_type=" + response_type + "&state=" + state + "&scope=" + scope;
  }
}