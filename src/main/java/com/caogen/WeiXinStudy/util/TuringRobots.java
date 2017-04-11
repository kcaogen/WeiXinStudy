package com.caogen.WeiXinStudy.util;

import com.alibaba.fastjson.JSONObject;

/**
 * 图灵机器人
 * @author 草根
 *
 */
public class TuringRobots {
	
	private static final String APIkey = "7e2394adc4a14ad8861288e6d01f89bc";
	
	/**
	 * 图灵机器人对话接口
	 * @param info
	 * @return
	 * @throws Exception 
	 */
	public static String dialogue(String info) throws Exception{
		String url = "http://www.tuling123.com/openapi/api?key="+APIkey+"&info="+info;
		String message = HttpUtil.sendGet(url);
		JSONObject json = JSONObject.parseObject(message);
		return json.get("text").toString();
	}

	public static void main(String[] args) {
		try {
			System.out.println(dialogue("你好"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
