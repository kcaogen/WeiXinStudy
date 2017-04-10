package com.caogen.WeiXinStudy.util;

import java.util.HashMap;

import com.alibaba.fastjson.JSONObject;

/**
 * 生成带参数的二维码
 * @author Administrator
 *
 */
public class CodeUtil {
	
	/**
	 * 创建二维码ticket
	 * @param json
	 * @throws Exception
	 */
	public static void CreateTicket(String json) throws Exception{
		String access_token = AccessTokenUtil.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+access_token;
		String message = HttpUtil.sendPost(url, json);
		System.err.println(message);
	}
	
	public static void main(String[] args) {
		
		HashMap<String, Integer> sceneMap = new HashMap<>();
		sceneMap.put("scene_id", 1);
		
		
		HashMap<String, Object> infoMap = new HashMap<>();
		infoMap.put("scene", sceneMap);
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("expire_seconds", 604800);
		map.put("action_name", "QR_SCENE");
		map.put("action_info", infoMap);
		
		String json = JSONObject.toJSONString(map);
		try {
			CreateTicket(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
