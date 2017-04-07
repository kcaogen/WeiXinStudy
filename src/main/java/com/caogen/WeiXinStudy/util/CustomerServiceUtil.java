package com.caogen.WeiXinStudy.util;

import java.util.HashMap;

import com.alibaba.fastjson.JSONObject;

/**
 * 微信客服工具类
 * 
 * @author caogen
 *
 */
public class CustomerServiceUtil {

	/**
	 * 发送客服消息
	 * 
	 * @param messageJson
	 * @throws Exception
	 */
	public static boolean sendMessage(String messageJson) throws Exception {
		String access_token = AccessTokenUtil.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + access_token;
		String message = HttpUtil.sendPost(url, messageJson);

		JSONObject json = JSONObject.parseObject(message);

		if (json.get("errmsg").equals("ok")) {
			return true;
		}

		return false;
	}

	public static void main(String[] args) {
		HashMap<String, String> content = new HashMap<>();
		content.put("content", "你好啊，逗比！");

		HashMap<String, Object> map = new HashMap<>();
		map.put("touser", "obh5OwxMu3wnQX3goFKpRgd0Gac8");
		map.put("msgtype", "text");
		map.put("text", content);
		String messageJson = JSONObject.toJSONString(map);
		try {
			sendMessage(messageJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
