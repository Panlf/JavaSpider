package com.plf.jsoup;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JsoupVideoDown {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			String path="http://i.snssdk.com/neihan/video/playback/?video_id=5b54540731dc414c93dd3001e1877a88&quality=480p&line=0&is_gif=0.mp4";
			downVideo(path,"美女");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void downVideo(String path,String fileName) throws IOException{
		URL url = new URL(path);
		URLConnection uc = url.openConnection();
		InputStream is = uc.getInputStream();
		byte[] bs = new byte[1024];
		FileOutputStream out = new FileOutputStream("E:\\temp\\"+fileName+".mp4");
		int i = 0;
		while ((i = is.read(bs)) != -1) {
			out.write(bs,0,i);
		} 
		is.close();
		out.close();
	}

}
