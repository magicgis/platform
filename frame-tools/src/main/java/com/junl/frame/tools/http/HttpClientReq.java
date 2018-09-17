package com.junl.frame.tools.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * 
 * @author chenmaolong
 * @date 2016年7月15日 上午10:32:40
 * @description 
 *		TODO
 */
public class HttpClientReq {
	/** 日志 */
	protected static Logger log = LoggerFactory.getLogger(HttpClientReq.class);
	
	private static final int connectTimeout = 10000;  
	private static final int soTimeout = 10000;  
	public static String httpClientPost(String url,Map<String,String> postData){
		//创建默认的 HttpClient 实例  
        HttpClient httpClient = new DefaultHttpClient();
        //连接超时
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), connectTimeout);
        //请求超时
        HttpConnectionParams.setSoTimeout(httpClient.getParams(), soTimeout);
        
        HttpPost httpPost = new HttpPost(url);  
  
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();  
        for(String key:postData.keySet()){
        	formParams.add(new BasicNameValuePair(key, postData.get(key)));  
        } 
        UrlEncodedFormEntity urlEncodedFormEntity;  
  
        try {  
            urlEncodedFormEntity = new UrlEncodedFormEntity(formParams, "UTF-8");  
            httpPost.setEntity(urlEncodedFormEntity);   
            HttpResponse httpResponse = null;  
            httpResponse = httpClient.execute(httpPost);  
            HttpEntity httpEntity = (HttpEntity) httpResponse.getEntity();  
            if (httpEntity != null) {  
                String content = EntityUtils.toString(httpEntity, "UTF-8");  
                return content;
            } 
            
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();
            log.error("========微信接口连接异常[httpClientPost]=================httpsUrl:{}",url);
        } finally {  
            //关闭连接，释放资源  
            httpClient.getConnectionManager().shutdown();  
        }  
        return "";
	}
 
	/**
	 * 发送json
	 * @author fuxin
	 * @date 2017年3月25日 下午5:49:45
	 * @description 
	 *		TODO
	 * @param url
	 * @param json
	 * @return
	 *
	 */
	public static String httpClientPostJson(String url,String json){
		  HttpClient client = new DefaultHttpClient();    
		  HttpPost post = new HttpPost(url);    
		  try {    
		      StringEntity s = new StringEntity(json.toString(), "UTF-8");    
		      s.setContentEncoding("UTF-8");
		      s.setContentType("application/json");    
		      post.setEntity(s);    
		           
		       HttpResponse res = client.execute(post);    
		       if(res.getStatusLine().getStatusCode() == HttpStatus.OK.value()){    
		           HttpEntity entity = res.getEntity();    
		           String charset = EntityUtils.getContentCharSet(entity);    
		           return EntityUtils.toString(entity, charset) ;
		       }    
		   } catch (Exception e) {    
			   log.error("post请求发送失败" + e);
		      return "";
		   }
		   return "";    
	}
}
