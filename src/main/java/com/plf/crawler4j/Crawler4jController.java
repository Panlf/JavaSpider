package com.plf.crawler4j;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Crawler4jController {

	public static void main(String[] args) throws Exception {
		String crawlStorageFolder = "E://temp//";
        int numberOfCrawlers = 7;
        
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        
        controller.addSeed("http://www.27270.com/ent/meinvtupian/");
        controller.addSeed("http://www.27270.com/ent/meinvtupian/2017");
        
        controller.start(Crawler4j.class, numberOfCrawlers);
	}

}
