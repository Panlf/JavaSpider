package com.plf.jsoup;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupTextDown {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Document doc = Jsoup.connect("http://www.biqudu.com/21_21470/").get();
			if(doc!=null){
				System.out.println(doc.getElementById("list").select("dd>a[href]"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
