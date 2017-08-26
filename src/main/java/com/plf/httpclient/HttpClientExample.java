package com.plf.httpclient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

/**
 * httpClient发送请求
 * @author Panlf 2017年7月18日下午10:31:35
 *
 */
public class HttpClientExample {
	@Test
	public void TestGet() {
		//CloseableHttpClient  httpClient=HttpClients.createDefault();
		CloseableHttpResponse response=null;
		String url="http://zhannei.baidu.com/cse/search?q=斗破苍穹&click=1&s=13603361664978768713&nsid=";
		try{
			HttpGet request = new HttpGet(url);//这里发送get请求
            // 获取当前客户端对象
			CloseableHttpClient httpClient =HttpClients.createDefault();
            // 通过请求对象获取响应对象
            response = httpClient.execute(request);
            
            // 判断网络连接状态码是否正常(0--200都数正常)
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
               String result= EntityUtils.toString(response.getEntity(),"utf-8");
               System.out.println(result);
            } 
		}catch(ClientProtocolException e){
			e.printStackTrace();
		}catch(IOException e1){
			e1.printStackTrace();
		}finally{
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void TestPost(){
		CloseableHttpResponse response =null;
		try{
			CloseableHttpClient httpClient =HttpClients.createDefault();
			 RequestConfig requestConfig = RequestConfig.custom()
					 	.setConnectTimeout(1000)//设置连接超时时间
		                .setConnectionRequestTimeout(1000)// 设置请求超时时间
		                .setSocketTimeout(1000)
		                .setRedirectsEnabled(true)//允许重定向
		                .build();
			
			HttpPost httpPost = new HttpPost("http://zhannei.baidu.com/cse/search");
			httpPost.setConfig(requestConfig);
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("s", "13603361664978768713"));
			nvps.add(new BasicNameValuePair("search", "斗破苍穹"));
			nvps.add(new BasicNameValuePair("click", "1"));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
			
	        // 通过请求对象获取响应对象
			response = httpClient.execute(httpPost);
	        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	               String result= EntityUtils.toString(response.getEntity(),"utf-8");
	               System.out.println(result);
	        } 
	        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
	        	 Header header = response.getFirstHeader("location");
	        	 System.out.println(header.getValue());
	        }
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void analyzeURL(){
		String path="http://www.tuicool.com/articles/zQNfyaN";
		int i=0;
		try {
			URL url=new URL(path);
			URLConnection conn=url.openConnection();
			conn.connect();
			InputStream ins=conn.getInputStream();
			InputStreamReader insread=new InputStreamReader(ins,"utf-8");
			BufferedReader buff=new BufferedReader(insread);
			String nextline=buff.readLine();
			while(nextline != null){
				//System.out.println(nextline);
				if(!getPattern(nextline).isEmpty()){
					downloadPicture(getPattern(nextline),"tuicool"+i);
					i++;
				}
				nextline=buff.readLine();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	public void TestUtil(){
		String str="<p><img src=\"http://img1.tuicool.com/RR3eyyE.jpg!web\" class=\"alignCenter\" /> </p>";
		System.out.println(getPattern(str));
	}
	public static String getPattern(String str){
		String pattern="http:.+?\\.(jpg|gif|png)";
		Pattern pa=Pattern.compile(pattern);
		Matcher ma=pa.matcher(str);
		if(ma.find()){
			return ma.group();
		}
		return "";
	}
	
	private static void downloadPicture(String urlList,String filename) {
		  URL url = null;
		  try {
		      url = new URL(urlList);
		      DataInputStream dataInputStream = new DataInputStream(url.openStream());
		
		      String imageName =  "E:/temp/"+filename+".jpg";
		
		      FileOutputStream fileOutputStream = new FileOutputStream(new File(imageName));
		      ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		      byte[] buffer = new byte[1024];
		      int length;
		
		      while ((length = dataInputStream.read(buffer)) > 0) {
		          output.write(buffer, 0, length);
		      }
		      //byte[] context=output.toByteArray();
		      fileOutputStream.write(output.toByteArray());
		      dataInputStream.close();
		      fileOutputStream.close();
			  } catch (MalformedURLException e) {
			      e.printStackTrace();
			  } catch (IOException e) {
			      e.printStackTrace();
		  }
	}
}
