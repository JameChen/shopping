package com.nahuo.service.autoupdate;


/**
 * 将服务端响应解析数据为Version对象
 * 
 * @author ilovedeals
 * 
 */
public interface ResponseParser {
	/**
	 * 将字符数据解析成Version对象
	 * @param response
	 * @return
	 */
	Version parser(String response);
}