package fate.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * SEO优化，搜索引擎ping工具
 * 
 * @author fate
 * 
 */
public class PingUtils {

	private static final String[] RPCs = {
	 "http://blogsearch.google.com/ping/RPC2",
	// "http://ping.blog.qikoo.com/rpc2.php",
	"http://ping.baidu.com/ping/RPC2" };

	private PingUtils() {
    }

    /**
	 * 生成ping包
	 * 
	 * @param title
	 *            文章标题
	 * @param url
	 *            网站首页地址
	 * @param shareURL
	 *            文章底子好
	 * @param rssURL
	 *            网站rss地址
	 * @return
	 */
	private static String buildMethodCall(String title, String url,
			String shareURL, String rssURL) {
		Document document = DocumentHelper.createDocument();
		Element methodCall = document.addElement("methodCall");
		Element methodName = methodCall.addElement("methodName");
		methodName.addText("weblogUpdates.extendedPing");
		Element params = methodCall.addElement("params");
		params.addElement("param").addElement("value").addElement("string")
				.addText(title);
		params.addElement("param").addElement("value").addElement("string")
				.addText(url);
		params.addElement("param").addElement("value").addElement("string")
				.addText(shareURL);
		params.addElement("param").addElement("value").addElement("string")
				.addText(rssURL == null ? "" : rssURL);
		return document.asXML();
	}

	/**
	 * ping搜索引擎
	 * 
	 * @param rpc
	 *            搜索引擎的ping地址
	 * @param title
	 *            文章标题
	 * @param url
	 *            网站首页地址
	 * @param shareURL
	 *            文章底子好
	 * @param rssURL
	 *            网站rss地址
	 * @return 搜索引擎返回的结果
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws DocumentException 
	 */
	public static boolean ping(String rpc, String title, String url,
			String shareURL, String rssURL){
		HttpPost post = new HttpPost(rpc);
//		post.addHeader("User-Agent", "request");
		System.out.println(rpc.substring(rpc.indexOf("http://") + 7,
				rpc.indexOf(".com/") + 4));
		post.addHeader("Host", rpc.substring(rpc.indexOf("http://") + 7,
				rpc.indexOf(".com/") + 4));
		post.addHeader("Content-Type", "text/xml");
		post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
		String methodCall = buildMethodCall(title, url, shareURL, rssURL);
		HttpEntity entity = new StringEntity(methodCall, "utf-8");
		post.setEntity(entity);
		HttpClient httpclient = HttpClients.createDefault();
		HttpResponse response;
		Element root = null;
		try {
			response = httpclient.execute(post);
			byte[] b = new byte[256];
			response.getEntity().getContent().read(b);
			String ret = new String(b);
			post.releaseConnection();
			Document document = DocumentHelper.parseText(ret.trim());
			root = document.getRootElement();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return root.element("params").element("param").element("value").element("int").getText().equals("0")?true:false;
	}

}
