package fate.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import fate.utils.entity.HttpResult;

/**
 * 模拟http请求的工具类
 * 支持get，post，put，delete
 * @author Fate
 *
 */
public class HttpClientUtil {

	private static Logger LOG = Logger.getLogger(HttpClientUtil.class);
	
	private HttpClientUtil() {
		super();
	}

	public static HttpResult post(String url, List<NameValuePair> data){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(data, "UTF-8");
			post.setEntity(entity);
			CloseableHttpResponse response = httpclient.execute(post);
			HttpEntity responseEntity = response.getEntity();  
			
			if (responseEntity != null) {  
				String submitResult = EntityUtils.toString(responseEntity, "UTF-8");
				System.out.println(submitResult);
				HttpResult result = new HttpResult();
				result.setStatusCode(response.getStatusLine().getStatusCode());
				result.setResult(submitResult);
				return result;
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			LOG.error(e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			LOG.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error(e);
		}finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 模拟表单上传
	 */
	public static String multipartRequest(String url, Map<String, String> parameters){
		MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			multipartEntity.addTextBody(entry.getKey(), entry.getValue());
		}
		// Now write the image
		String fullFilePath = parameters.get("file_path");
		if(!Strings.isEmpty(fullFilePath)){
			multipartEntity.addBinaryBody("uploadFile", new File(fullFilePath));
		}
		
        HttpPost request = new HttpPost(url);     
        request.setEntity(multipartEntity.build());  
        request.addHeader("Accept", "*/*");
        request.addHeader("Content-Type","multipart/form-data; boundary=--------------------"
				+ Long.toString(System.currentTimeMillis(), 16));
        request.addHeader("Connection", "Keep-Alive");
        request.addHeader("Cache-Control", "no-cache");
        
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        post.setEntity(multipartEntity.build());
        HttpResponse response = null;
		try {
			response = client.execute(post);
			String submitResult = EntityUtils.toString(response.getEntity(), "UTF-8");
			
			return submitResult;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static HttpResult get(String url){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		try {
			HttpGet get = new HttpGet(url);
			get.addHeader("Content-Type","text/html;charset=UTF-8");
			get.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
			CloseableHttpResponse response = httpclient.execute(get);
			HttpEntity responseEntity = response.getEntity();
			
			if (responseEntity != null) {  
				String submitResult = EntityUtils.toString(responseEntity, "UTF-8");
				System.out.println(submitResult);
				HttpResult result = new HttpResult();
				result.setStatusCode(response.getStatusLine().getStatusCode());
				result.setResult(submitResult);
				return result;
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			LOG.error(e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			LOG.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error(e);
		}finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public static HttpResult put(String url, List<NameValuePair> data){
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPut put = new HttpPut(url);
		
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(data, "UTF-8");
			put.setEntity(entity);
			CloseableHttpResponse response = httpclient.execute(put);
			HttpEntity responseEntity = response.getEntity();  
			
			if (responseEntity != null) {  
				String submitResult = EntityUtils.toString(responseEntity, "UTF-8");
				System.out.println(submitResult);
				HttpResult result = new HttpResult();
				result.setStatusCode(response.getStatusLine().getStatusCode());
				result.setResult(submitResult);
				return result;
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			LOG.error(e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			LOG.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error(e);
		}finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public static HttpResult delete(String url){
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		try {
			HttpDelete delete = new HttpDelete(url);
			delete.addHeader("Content-Type","text/html;charset=UTF-8");
			CloseableHttpResponse response = httpclient.execute(delete);
			HttpEntity responseEntity = response.getEntity();  
			
			if (responseEntity != null) {  
				String submitResult = EntityUtils.toString(responseEntity, "UTF-8");
				System.out.println(submitResult);
				HttpResult result = new HttpResult();
				result.setStatusCode(response.getStatusLine().getStatusCode());
				result.setResult(submitResult);
				return result;
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			LOG.error(e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			LOG.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error(e);
		}finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 下载图片到本地
	 * @param url
	 * @return
	 */
	public static String downloadImg(String url, String folder, String downloadUrl){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String fileType = url.substring(url.lastIndexOf("."));
		if(Strings.isEmpty(fileType)){
			fileType = ".jpg";
		}
		long now = System.currentTimeMillis();
		String newUrl = downloadUrl + now + fileType;
		File dir = new File(folder);
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		File storeFile = new File(folder+now+fileType);  
	    FileOutputStream output = null;
		try {
			HttpGet get = new HttpGet(url);
			get.addHeader("Content-Type","text/html;charset=UTF-8");
			get.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
			CloseableHttpResponse response = httpclient.execute(get);
			HttpEntity responseEntity = response.getEntity();
			
			if (responseEntity != null) {  
				InputStream input = responseEntity.getContent();
				output = new FileOutputStream(storeFile);
				IOUtils.copy(input, output);
				output.flush();
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			LOG.error(e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			LOG.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error(e);
		}finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(output!=null){
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	    return newUrl;
	}
	
//	public static void main(String[] args) {
//		List<NameValuePair> data = new ArrayList<>();
//		data.add(new BasicNameValuePair("carNum","123"));
//		System.out.println(post("http://211.144.68.101/tractorService.do",data));
//	}
}
