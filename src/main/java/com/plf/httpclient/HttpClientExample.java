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
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;*/
import org.junit.Test;


/**
 * httpClient发送请求
 * @author Panlf 2017年7月18日下午10:31:35
 *
 */
public class HttpClientExample {
	private static HttpUtils httpUtils=HttpUtils.getInstance();
	//GET请求
	@Test
	public void TestGet() {
		String url="http://zhannei.baidu.com/cse/search?q=斗破苍穹&click=1&s=13603361664978768713&nsid=";
		//CloseableHttpClient  httpClient=HttpClients.createDefault();
		/*CloseableHttpResponse response=null;
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
		}*/
		String result=httpUtils.sendGet(url,null);
		System.out.println(result);
	}
	
	//分析URL
	@Test
	public void analyzeURL(){
		String path="http://www.27270.com/ent/meinvtupian/2017/225210.html";
		String uuid="";
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
					getPattern(nextline);
					uuid=UUID.randomUUID().toString();//随机获取一个UUID
					downloadPicture(getPattern(nextline),uuid);
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
	
	//测试正则表达式
	@Test
	public void TestUtil(){
		String str="<p><img src=\"http://img1.tuicool.com/RR3eyyE.jpg!web\" class=\"alignCenter\" /> </p>";
		System.out.println(getPattern(str));
	}
	
	//图片正则表达式
	public static String getPattern(String str){
		String pattern="src=\"http:.+?\\.(jpg|gif|png)";
		Pattern pa=Pattern.compile(pattern);
		Matcher ma=pa.matcher(str);
		if(ma.find()){
			//System.out.println(ma.group().replace("src=\"", ""));
			return ma.group().replace("src=\"", "");
		}
		return "";
	}
	
	//根据URL下载图片
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
