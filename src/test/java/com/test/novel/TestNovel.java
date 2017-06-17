package com.test.novel;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.plf.jsoup.JsoupTextDown;
import com.pojo.Novel;
import com.service.NovelService;

public class TestNovel {
	@Test
	public void TestDb() throws Exception{
//		boolean b=NovelService.insertNovel("","","");
//		System.out.println(b);
		boolean b=NovelService.selectNovelURl("http://www.biqudu.com/16_16088/9380827.html");
		System.out.println(b);
	}
	
	@Test
	public void update(){
		List<Novel> list=NovelService.selectNovelNUll();
		if(list.size()!=0){
			for (Novel novel : list) {
				Map<String,String> map=JsoupTextDown.downText(novel.getUrl());
				System.out.println(novel.getId()+"+++"+map.get("title"));
				NovelService.updateNovel(novel.getId(), map.get("title"), map.get("text"));
			}
		}
	}
	
	@Test
	public void write(){
		List<Novel> novelList=NovelService.selectNovel();
		//System.out.println(novelList.size());
//		for(int i=1;i<30;i++){
//			System.out.println(novelList.get(i).getTitle());
//		}
		JsoupTextDown.writeText(novelList,"最强反派系统.txt");
	}
}
