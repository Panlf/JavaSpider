package com.plf.boss;


import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

/**
 * 
 * @author panlf
 * @date 2020-05-04
 */
public class DefaultConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {

	/**
	 * keep alive策略
	 * 在HTTP 1.0中，每一次请求响应之后，下一次的请求需要断开之前的连接，再重新开始
	 * 在HTTP 1.1中，使用keep-alive在一次TCP连接中可以持续发送多份数据而不会断开连接
	 */
    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
        if (response == null) {
            throw new IllegalArgumentException("HTTP response may not be null");
        }
        HeaderElementIterator it = new BasicHeaderElementIterator(
                //HTTP.CONN_KEEP_ALIVE的为"Keep-Alive"
                response.headerIterator(HTTP.CONN_KEEP_ALIVE));
        while (it.hasNext()) {
            HeaderElement headerElement = it.nextElement();
            String param = headerElement.getName(); 
            String value = headerElement.getValue();
            if (value != null && param.equalsIgnoreCase("timeout")) {
                try {
                    return Long.parseLong(value) * 1000;
                } catch(NumberFormatException ignore) {
                }
            }
        }
        return 60 * 1000;
    }

}
