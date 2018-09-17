package com.junl.frame.tools.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * 
 * @author chenmaolong
 * @date 2016年7月15日 上午10:32:51
 * @description 
 *		TODO
 */
public class HttpPost {
	
	static Logger logger = LoggerFactory.getLogger(HttpPost.class);
	
	public static String post(String uri, String data) throws IOException {
		URL url = new URL(uri);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		conn.setInstanceFollowRedirects(false);
		conn.setConnectTimeout(3 * 1000);
		conn.setRequestProperty("Accept-Charset", "utf-8");
		conn.setRequestProperty("contentType", "utf-8");
//		conn.setRequestProperty("Cookie", "JSESSIONID=4037EA919860EC1BC9000692076FC4F7");
		if (data != null) {
			OutputStream os = conn.getOutputStream();
			os.write(data.getBytes("UTF-8"));
			os.flush();
		}
		conn.connect();
		InputStream stream = conn.getInputStream();
		String result = convertStreamToString(stream);
		return result;

	}
	
	
	public static String httpClientPostJsonGBK(String url,String json){
		  HttpClient client = new DefaultHttpClient();    
		  org.apache.http.client.methods.HttpPost post = new org.apache.http.client.methods.HttpPost(url);    
		  try {    
		      StringEntity s = new StringEntity(json.toString(), "UTF-8");    
		      s.setContentEncoding("UTF-8");
		      s.setContentType("application/json");    
		      post.setEntity(s);    
		           
		       HttpResponse res = client.execute(post);    
		       if(res.getStatusLine().getStatusCode() == HttpStatus.OK.value()){    
		           HttpEntity entity = res.getEntity();    
		           return new String (EntityUtils.toString(entity, "GBK").getBytes("GBK")) ;
		       }    
		   } catch (Exception e) {    
			   logger.error("post请求发送失败" + e);
		   }
		  return "{'succ':'fail','message':'网络请求失败'}";
	}

	public static String convertStreamToString(InputStream is) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		return sb.toString();
	}

	public static InputStream getSource(String uri, String data) throws IOException {
		URL url = new URL(uri);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		conn.setInstanceFollowRedirects(false);
		conn.setConnectTimeout(3 * 1000);
		conn.setRequestProperty("Accept-Charset", "utf-8");
		conn.setRequestProperty("contentType", "utf-8");
		conn.setRequestProperty("Cookie", "JSESSIONID=4037EA919860EC1BC9000692076FC4F7");
		if (data != null) {
			OutputStream os = conn.getOutputStream();
			os.write(data.getBytes("UTF-8"));
			os.flush();
		}
		conn.connect();
		InputStream stream = conn.getInputStream();
		
		return stream;

	}
}
