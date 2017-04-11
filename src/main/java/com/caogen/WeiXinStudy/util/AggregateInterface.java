package com.caogen.WeiXinStudy.util;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 聚合接口
 * @author 草根
 *
 */
@SuppressWarnings("unchecked")
public class AggregateInterface {
	
	private static final String key = "44e6d4390be17a195d0e137c295b723a";
	
	/**
	 * 通过笑话大全接口获取笑话
	 * @return
	 * @throws Exception 
	 */
	public static String getJoke() throws Exception{
		long time = System.currentTimeMillis()/1000;
		String url = "http://japi.juhe.cn/joke/content/list.from?sort=desc&pagesize=1&time="+time+"&key="+key;
		String message = HttpUtil.sendGet(url);
		
		JSONObject json1 = JSONObject.parseObject(message);
		JSONObject json2 = JSONObject.parseObject(json1.get("result").toString());
		List<Object> list = (List<Object>) JSON.parseObject(json2.get("data").toString(), List.class);
		JSONObject json3 = JSONObject.parseObject(list.get(0).toString());
		String content = json3.get("content").toString();
		
		return content;
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(getJoke());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
