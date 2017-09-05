package com.plf.httpclient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginByHttpClient {

	private static Logger logger=LoggerFactory.getLogger(LoginByHttpClient.class);
	private static HttpUtils httpUtils=HttpUtils.getInstance();
	private String loginURL = "";  
    private String lt=null;  
    private String execution=null;  
    private String _eventId=null;
    
	//登录CSDN时需要的必要信息
	private void fetchParams(){
		logger.info("开始获取登录的表单参数...");
		loginURL="https://passport.csdn.net/account/login";
		String html = httpUtils.sendGet(loginURL,"");    
        Document doc = Jsoup.parse(html);    
        Element form = doc.select(".user-pass").get(0);    
        lt = form.select("input[name=lt]").get(0).val();    
        execution = form.select("input[name=execution]").get(0).val();    
        _eventId = form.select("input[name=_eventId]").get(0).val(); 
        logger.info("表单参数获取结束>>>lt={},execution={},_eventId={}",lt,execution,_eventId);
	}
	
	//将信息下载到txt文本中
	private void writeText(String text){
		try {
			logger.info("开始下载页面到文本中...");
			FileWriter fw=new FileWriter(new File("E:\\demo.txt"),false);
			BufferedWriter bw=new BufferedWriter(fw);
			bw.write(text);
			bw.flush();
			fw.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void LoginCSDN(){
		fetchParams();
		logger.info("开始登录CSDN...");
		Map<String,String> map=new HashMap<String,String>();
		map.put("username", "");
		map.put("password", "");
		map.put("lt", lt);
		map.put("execution", execution);
		map.put("_eventId", _eventId);
		Map<String,String> resultmap= httpUtils.sendPost(loginURL, map);
		String result = resultmap.get("entity"); 
		String cookie = resultmap.get("cookie");
		logger.info("最后获取到的cookie===>{}",cookie);
		if (result.indexOf("redirect_back") > -1) {    
			logger.info("登陆成功..."); 
			//writeText(result);
        } else if (result.indexOf("登录太频繁") > -1) {    
        	logger.info("登录太频繁，请稍后再试...");    
        } else {    
        	logger.info("登陆失败...");    
        }  
		
		//访问子页面
		String context=httpUtils.sendGet("http://www.csdn.net/",cookie);
		if(context.length()>0){
			writeText(context);
			logger.info("下载完毕...");
		}
		
		logger.info("程序全部结束...");
	}
}
