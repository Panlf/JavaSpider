package com.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.dao.NovelMapper;
import com.pojo.Novel;
import com.pojo.NovelExample;
import com.pojo.NovelExample.Criteria;

/**
 * novel数据库的操作
 * @author Panlf 2017年6月17日下午1:08:41
 *
 */
public class NovelService {
	private static SqlSessionFactory factory;
	
	static {
		String resource = "SqlMapConfig.xml";
		//通过流将核心配置文件读取进来
		InputStream inputStream;
		try {
			inputStream = Resources.getResourceAsStream(resource);
			//通过核心配置文件输入流来创建会话工厂
			factory = new SqlSessionFactoryBuilder().build(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean insertNovel(String title,String url,String text) throws Exception{
		SqlSession openSession = factory.openSession();
		///通过getMapper方法来实例化接口
		NovelMapper mapper = openSession.getMapper(NovelMapper.class);
		Novel novel=new Novel();
		novel.setTitle(title);
		novel.setText(text);
		novel.setUrl(url);
		int i=mapper.insertSelective(novel);
		openSession.commit();
		openSession.close();
		if(i>0){
			return true;
		}
		return false;
	}
	
	public static boolean updateNovel(int id,String title,String text){
		SqlSession openSession = factory.openSession();
		///通过getMapper方法来实例化接口
		NovelMapper mapper = openSession.getMapper(NovelMapper.class);
		Novel novel=new Novel();
		novel.setTitle(title);
		novel.setText(text);
		NovelExample example=new NovelExample();
		Criteria criteria=example.createCriteria();
		criteria.andIdEqualTo(id);
		int i=mapper.updateByExampleSelective(novel, example);
		openSession.commit();
		openSession.close();
		if(i>0){
			return true;
		}
		return false;
	}
	
	public static List<Novel> selectNovelNUll(){
		SqlSession openSession = factory.openSession();
		///通过getMapper方法来实例化接口
		NovelMapper mapper = openSession.getMapper(NovelMapper.class);
		NovelExample example=new NovelExample();
		Criteria criteria=example.createCriteria();
		criteria.andTitleEqualTo("");
		List<Novel> novellist=mapper.selectByExample(example);
		openSession.close();
		return novellist;
	}
	
	public static boolean selectNovelURl(String url){
		SqlSession openSession = factory.openSession();
		NovelMapper mapper = openSession.getMapper(NovelMapper.class);
		NovelExample example=new NovelExample();
		Criteria criteria=example.createCriteria();
		criteria.andUrlEqualTo(url);
		List<Novel> nolve=mapper.selectByExample(example);
		openSession.close();
		if(nolve.size()==0){
			return false;
		}
		return true;
	}
	
	public static List<Novel> selectNovel(){
		SqlSession openSession = factory.openSession();
		NovelMapper mapper = openSession.getMapper(NovelMapper.class);
		NovelExample example=new NovelExample();
		return mapper.selectByExampleWithBLOBs(example);
	}
}
