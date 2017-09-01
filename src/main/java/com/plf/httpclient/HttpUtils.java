package com.plf.httpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
	// 获取当前客户端对象
	private static CloseableHttpClient httpClient = HttpClients.createDefault();  
	//携带的信息
    private static HttpClientContext context = new HttpClientContext();
    
    private static RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(5000)   //设置连接超时时间
            .setConnectionRequestTimeout(5000) // 设置请求超时时间
            .setSocketTimeout(5000)
            .setRedirectsEnabled(true)//默认允许自动重定向
            .build();
    
	public String sendGet(String url){
		CloseableHttpResponse response=null;
		String result = null; 
		HttpEntity entity=null;
		try{
			HttpGet get = new HttpGet(url); 
			get.setConfig(requestConfig);
			//通过请求对象获取响应对象
            response = httpClient.execute(get, context);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            	entity = response.getEntity();
            	result= EntityUtils.toString(entity,"utf-8");
             } 
            EntityUtils.consume(entity); //关闭
            return result;
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public String sendPost(String url,Map<String,String> map){
		CloseableHttpResponse response = null;    
        String result = null;
        HttpEntity entity=null;
        try{
            HttpPost post = new HttpPost(url); 
            post.setConfig(requestConfig);
            if(!map.isEmpty()){
            	List <NameValuePair> nvps = new ArrayList <NameValuePair>();
    			for (Map.Entry<String, String> entry : map.entrySet()) {
    				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
    			post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            }
          //执行请求用execute方法    
          response = httpClient.execute(post, context);  
          if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
          		entity = response.getEntity();
          		result= EntityUtils.toString(entity,"utf-8");
           } 
          EntityUtils.consume(entity); //关闭
          return result;
        }catch(Exception e){
        	e.printStackTrace();
        }
        return result;
	}
}
