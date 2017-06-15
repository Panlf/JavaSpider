package com.plf.jsoup;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupTextDown {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		try {
			Document doc = Jsoup.connect("http://www.biqudu.com/43_43821/").get();
			if(doc!=null){
				Elements links=doc.getElementById("list").select("dd>a[href]");
				for (Element link : links) {
					FileWriter fw=new FileWriter("E:\\圣墟.txt",true);
					BufferedWriter bufw=new BufferedWriter(fw);
					Thread.sleep(1000);
					String path="http://www.biqudu.com"+link.attr("href");
					String title=link.text();
					System.out.println(path+"---"+title);
					Map<String,String> content=downText(path);
					bufw.write(content.get("title"));
					System.out.println(content.get("title"));
					bufw.newLine();
					bufw.write(content.get("text"));
					bufw.newLine();
					bufw.flush();
					bufw.close();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取题目和正文
	public static Map<String,String> downText(String path){
		Map<String,String> map=new Hashtable<String,String>();
		map.put("title", "");
		map.put("text","");
		try{
			Document doc = Jsoup.connect(path)
					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:49.0) Gecko/20100101 Firefox/49.0")
					.header("Connection", "close")//如果是这种方式，这里务必带上
					.timeout(50000)//超时时间
					.get();
			String text="",title="";
			if(doc!=null){
				title=doc.select("h1").text().toString();
				title=replace(StringEscapeUtils.escapeHtml4(title));
				text=doc.getElementById("content").text().toString();
				text=replace(StringEscapeUtils.escapeHtml4(text));
				//System.out.println(text.substring(1));
			}
			map.put("title", title);
			map.put("text",text.substring(1));
			return map;	
		}catch(Exception e){
			return map;
		}
	}
	
	public static String replace(String str){
		str = str.replace("&ldquo;", "“");
        str = str.replace("&rdquo;", "”");
        str = str.replace("&nbsp;", " ");
        str = str.replace("hellip;", "…");
        str = str.replace("&amp;", "");
        str = str.replace("&", "");
        str = str.replace("&#39;", "'");
        str = str.replace("&rsquo;", "’");
        str = str.replace("&mdash;", "—");
        str = str.replace("&ndash;", "–");
        str = str.replace("$#65279","");
		return str;
	}
}
