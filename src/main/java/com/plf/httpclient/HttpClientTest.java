package com.plf.httpclient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

/**
 * HttpClient测试类
 * @author plf 2018年7月20日下午9:26:25
 *
 */
public class HttpClientTest {

	public static HttpsUtils httpsUtils = HttpsUtils.getInstance();
	
	public static HttpUtils httpUtils = HttpUtils.getInstance();
	
	
	/**
	 * 这边测试了很多HTTPS网站发现基本网站都是可以通用的，都可以访问。、
	 * 不够我的GITHUB中的那个小说下载项目，使用HTTP的工具类不能访问HTTPS，
	 * 配置为HTTPS后才能正常访问。
	 */
	@Test
	public void test(){
		String result = "";
		//String url = "https://www.bilibili.com/";
		String url = "https://www.biqudu.com/31_31729/2171012.html";
		result = httpsUtils.sendGet(url);
		writeText(result,"https");
		result = httpUtils.sendGet(url);
		writeText(result,"http");
	}
	
	
	public static void writeText(String text,String textname){
		try {
			FileWriter fw=new FileWriter(new File("E:\\"+textname+".txt"),false);
			BufferedWriter bw=new BufferedWriter(fw);
			bw.write(text);
			bw.flush();
			fw.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
