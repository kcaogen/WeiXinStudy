package com.caogen.WeiXinStudy.service;

import java.util.Map;

public interface CoreService {
	
	/**
	 * 消息业务处理分发器
	 * @param map
	 */
	public String processMessage(Map<String, String> map);
	
	/**
	 * 事件消息业务分发器
	 * @param map
	 */
	public String EventDispatcher(Map<String, String> map);

}
