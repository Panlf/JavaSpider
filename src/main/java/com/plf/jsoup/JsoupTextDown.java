package com.plf.jsoup;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.pojo.Novel;
import com.service.NovelService;

public class JsoupTextDown {

	public static void main(String[] args) throws Exception {
		Document doc = Jsoup.connect("http://www.biqudu.com/16_16088/").get();
		if(doc!=null){
			Elements links=doc.getElementById("list").select("dd>a[href]");
			for (Element link : links) {
				Thread.sleep(2000);
				String path="http://www.biqudu.com"+link.attr("href");
				String title=link.text();
				System.out.println(path+"---"+title);
				if(NovelService.selectNovelURl(path)){
					continue;
				}
				Map<String,String> content=downText(path);
				NovelService.insertNovel(content.get("title"),path,content.get("text"));
				System.out.println(content.get("title"));	
			}
		}
		updateTheNull();
		writeTheText("最强反派系统.txt");
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
	
	//写入txt文件
	public static void writeText(List<Novel> novelList,String textName){
		try {
			FileWriter fw=new FileWriter("E:\\"+textName,true);
			BufferedWriter bufw=new BufferedWriter(fw);
			for (Novel novel : novelList) {
				bufw.write(replace(novel.getTitle()));
				bufw.newLine();
				bufw.write(replace(novel.getText()));
				bufw.newLine();
				bufw.flush();
			}
			bufw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String replace(String str){
		str = str.replace("&ldquo;", "“");
		str = str.replace("ldquo;", "“");
        str = str.replace("&rdquo;", "”");
        str = str.replace("rdquo;", "”");
        str = str.replace("&nbsp;", " ");
        str = str.replace("hellip;", "…");
        str = str.replace("&amp;", "");
        str = str.replace("&", "");
        str = str.replace("&#39;", "'");
        str = str.replace("&lsquo;", "‘");
        str = str.replace("lsquo;", "‘");
        str = str.replace("&rsquo;", "’");
        str = str.replace("rsquo;", "’");
        str = str.replace("&mdash;", "—");
        str = str.replace("&ndash;", "–");
		return str;
	}
	
	public static void updateTheNull(){
		List<Novel> list=NovelService.selectNovelNUll();
		if(list.size()!=0){
			for (Novel novel : list) {
				Map<String,String> map=JsoupTextDown.downText(novel.getUrl());
				System.out.println(novel.getId()+"+++"+map.get("title"));
				NovelService.updateNovel(novel.getId(), map.get("title"), map.get("text"));
			}
		}
	}
	
	public static void writeTheText(String textname){
		List<Novel> novelList=NovelService.selectNovel();
		JsoupTextDown.writeText(novelList,textname);
	}
}