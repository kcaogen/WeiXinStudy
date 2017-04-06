package com.caogen.WeiXinStudy.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.alibaba.fastjson.JSONObject;

/**
 * 模板消息接口
 * @author 草根
 *
 */
public class TemplateMessageUtil {
	
	/**
	 * 发送模板消息
	 * @param messageJson
	 * @throws Exception
	 */
	public static void sendTemplateMessage(String messageJson) throws Exception{
		String access_token = AccessTokenUtil.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+access_token;
		
		String message = HttpUtil.sendPost(url, messageJson);
		
		JSONObject json = JSONObject.parseObject(message);
		
		System.err.println(json);
	}
	
	/**
	 * 关注成功发送消息模板
	 * @param openId
	 */
	public static void ConcernedSuccess(String openId){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		
		HashMap<String, String> first = new HashMap<>();
		first.put("value", "恭喜您关注成功！");
		first.put("color", "#173177");
		
		HashMap<String, String> time = new HashMap<>();
		time.put("value", df.format(new Date()));
		time.put("color", "#173177");
		
		HashMap<String, String> remark = new HashMap<>();
		remark.put("value", "非常感谢！");
		remark.put("color", "#173177");
		
		HashMap<String, Object> data = new HashMap<>();
		data.put("first", first);
		data.put("time", time);
		data.put("remark", remark);
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("touser", openId);
		map.put("template_id", "xJDLg_VojFK9pzFowVy7eRu4jTTEjb01oR8OFp0s48k");
		map.put("url", "http://kcaogen.ngrok.cc/image/lyf.jpg");
		map.put("data", data);
		String messageJson = JSONObject.toJSONString(map);
		
		System.out.println(messageJson);
		try {
			sendTemplateMessage(messageJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ConcernedSuccess("obh5OwxMu3wnQX3goFKpRgd0Gac8");
	}
}
