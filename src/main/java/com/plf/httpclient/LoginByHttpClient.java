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

/**
 * 该案例已经不适应新版本的CSDN的登录，因为CSDN登录里有一个参数fkid不知道怎么获取。
 * @author plf 2018年4月23日下午5:17:21
 *
 */
@Deprecated
public class LoginByHttpClient {

	private static Logger logger=LoggerFactory.getLogger(LoginByHttpClient.class);
	private static HttpUtils httpUtils=HttpUtils.getInstance();
    private String lt=null;  
    private String execution=null;  
    private String gps=null;
    private String fkid=null;
    private String _eventId=null;
    
	//登录CSDN时需要的必要信息
	private void fetchParams(){
		logger.info("开始获取登录的表单参数...");
		String html = httpUtils.sendGet("https://passport.csdn.net/account/login");    
        Document doc = Jsoup.parse(html);    
        Element form = doc.select(".user-pass").get(0);    
        lt = form.select("input[name=lt]").get(0).val();    
        execution = form.select("input[name=execution]").get(0).val();    
        _eventId = form.select("input[name=_eventId]").get(0).val(); 
        gps = form.select("input[name=gps]").get(0).val();    
        fkid = form.select("input[name=fkid]").get(0).val(); 
        logger.info("表单参数获取结束>>>lt={},execution={},_eventId={},gps={},fkid={}",lt,execution,_eventId,gps,fkid);
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
		map.put("gps", gps);
		map.put("fkid", "WHJMrwNw1k/Gmz7WqccQNQfe2bJP7jKnWCAkshyhqs7+4AbEX7/MzgJZqHWhbgHmfHwMAJ0sHxeMsxZbq0PYuwjRZaV63hlDINv2h6qHuX4t8xpBF9DPUCpSD3EmPbcN1b2GXgZ5AQVLqPnuw0RORC7+8f0t6LV2c2LYZljG1VJdkhoMiJ81hY8XGXUjeh6qHyw74jIGEpERiNVCcw5ywKJYdG3GlHu/oVOGgQQwfzqCplzSqoMfhMvRAIJagVsdgR/t9WW/jIrAFA4nOgFtsiIsUjFtjkIWK1487582755342");
		String result= httpUtils.sendPost("https://passport.csdn.net/account/verify", map);
		writeText(result);
		
		//访问子页面
		String context=httpUtils.sendGet("http://my.csdn.net/my/score");
		if(context!=null && context.length()>0){
			writeText(context);
			logger.info("下载完毕...");
		}
		
		logger.info("程序全部结束...");
	}
}
