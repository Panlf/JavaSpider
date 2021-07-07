package com.plf.boss;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {
	private static Logger log = LoggerFactory.getLogger(HttpClientUtils.class);
	
	public HttpClientUtils() {}
	
	/**
	 * 建立全局共享的httpclient
	 */
	private static CloseableHttpClient httpClient = null;
	
	static {
		// http 连接池
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(10);
		connectionManager.setDefaultMaxPerRoute(200);// 例如默认每路由最高50并发，具体依据业务来定
		// 从连接池中获取连接的时间最长为100ms，建立与服务端通信的时间最长为1秒，处理业务最长时间为10秒
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(1000)
				.setConnectTimeout(1000)
				.setSocketTimeout(10000)
				.build();

		ConnectionKeepAliveStrategy kaStrategy = new DefaultConnectionKeepAliveStrategy();
		// 获取httpClient
		httpClient = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.setKeepAliveStrategy(kaStrategy)
				.setDefaultRequestConfig(requestConfig)
				.build();

		// 控制空闲连接
		IdleConnectionMonitorThread idleConnectionMonitor = new IdleConnectionMonitorThread(connectionManager);
		idleConnectionMonitor.start();
	}
}
