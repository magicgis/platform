package com.junl.frame.tools.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

/**
 * 
 * @author xus
 * @date 2016年6月23日 下午4:12:26
 * @description 
 *		http请求
 */
public class HttpUtils {

	/**
	 * 
	 * @author xus
	 * @date 2016年6月23日 下午4:13:11
	 * @description 
	 *		指定http请求
	 *		<b>这里使用spring的工具,需系统集成spring</b>
	 * @param url 请求地址
	 * @return String 请求返回内容
	 * @throws Exception
	 *
	 */
	public static String httpPost(String url) throws Exception {
		
		URI uri = new URI(url);
		SimpleClientHttpRequestFactory schr = new SimpleClientHttpRequestFactory();
		ClientHttpRequest chr = schr.createRequest(uri, HttpMethod.POST);
		ClientHttpResponse res = chr.execute();
		
		BufferedReader breader = new BufferedReader(new InputStreamReader(res.getBody()));
        return breader.readLine().toString();
	}
}
