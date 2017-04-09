package com.caogen.WeiXinStudy.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * 菜单工具类
 * @author 草根
 *
 */
public class MenuUtil {
	
	/**
	 * 创建菜单
	 * @param json
	 * @throws Exception
	 */
	public static void createMenu(String json) throws Exception{
		String access_token = AccessTokenUtil.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+access_token;
		String message = HttpUtil.sendPost(url, json);
		System.out.println(message);
	}
	
	public static void main(String[] args) {
		HashMap<String, String> map1 = new HashMap<>();
		map1.put("type", "click");
		map1.put("name", "赞一下我");
		map1.put("key", "V1001_GOOD");
		
		HashMap<String, String> map2 = new HashMap<>();
		map2.put("type", "view");
		map2.put("name", "视频");
		map2.put("url", "http://v.qq.com/");
		
		HashMap<String, String> map3 = new HashMap<>();
		map3.put("type", "view");
		map3.put("name", "搜索");
		map3.put("url", "https://www.baidu.com/");
		
		List<Object> sub_buttonList = new ArrayList<>();
		sub_buttonList.add(map3);
		sub_buttonList.add(map2);
		sub_buttonList.add(map1);
		
		HashMap<String, Object> sub_buttonMap = new HashMap<>();
		sub_buttonMap.put("name", "菜单");
		sub_buttonMap.put("sub_button", sub_buttonList);
		
		HashMap<String, String> buttonMap = new HashMap<>();
		buttonMap.put("type", "click");
		buttonMap.put("name", "今日歌曲");
		buttonMap.put("key", "V1001_TODAY_MUSIC");
		
		List<Object> buttonList = new ArrayList<>();
		buttonList.add(buttonMap);
		buttonList.add(sub_buttonMap);
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("button", buttonList);
		
		String json = JSONObject.toJSONString(map);
		System.out.println(json);
		
		try {
			createMenu(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
