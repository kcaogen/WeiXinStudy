package com.caogen.WeiXinStudy.util;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ServerUtil {
	
	/**
	 * 如果公众号基于消息接收安全上的考虑，需要获知微信服务器的IP地址列表，以便识别出哪些消息是微信官方推送给你的，
	 * 哪些消息可能是他人伪造的，可以通过该接口获得微信服务器IP地址列表。
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getServerIp(){
		String access_token = AccessTokenUtil.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=" + access_token + "";
		String message = HttpUtil.getMessage(url);
		
		JSONObject json = JSONObject.parseObject(message);
		
		List<String> ipList = (List<String>)JSON.parseObject(json.get("ip_list").toString(),List.class);
		return ipList;
	}
	
	public static void main(String[] args) {
		System.out.println(getServerIp());
		
	}

}
