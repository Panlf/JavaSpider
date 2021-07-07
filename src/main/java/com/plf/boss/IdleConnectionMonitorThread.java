package com.plf.boss;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.HttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http连接池中的空闲连接管理
 * @author panlf
 * @date 2020-05-04
 */
public class IdleConnectionMonitorThread extends Thread{
	
	private static Logger log = LoggerFactory.getLogger(IdleConnectionMonitorThread.class);
	
	private final HttpClientConnectionManager connectionManager;
    private volatile boolean shutdown = false;

    public IdleConnectionMonitorThread(HttpClientConnectionManager connectionManager) {
        super();
        this.setName("http-connection-monitor");
        this.setDaemon(true);
        this.connectionManager = connectionManager;
        this.start();
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    // 等待5秒
                    wait(5000);
                    // 关闭过期的链接
                    connectionManager.closeExpiredConnections();
                    // 选择关闭 空闲30秒的链接
                    connectionManager.closeIdleConnections(30, TimeUnit.SECONDS);
                }
            }
        } catch (InterruptedException ex) {
        	log.error("httpclient空闲连接监控线程终止.", ex);
        }
    }
    
    public void shutdown() {
		shutdown = true;
		synchronized (this) {
			notifyAll();
		}
	}
}
