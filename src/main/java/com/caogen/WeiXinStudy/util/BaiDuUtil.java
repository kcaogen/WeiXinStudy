package com.caogen.WeiXinStudy.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caogen.WeiXinStudy.entity.ReportLocation;

public class BaiDuUtil {
	
	private static Logger logger = Logger.getLogger(BaiDuUtil.class);
	
	private static final String ak = "3N1eoxKnlxrQGV3W2Gsqm3joaCW6YL4I";
	
	/**
	 * 微信坐标转换成百度坐标
	 * @param location
	 * @throws Exception
	 */
	public static ReportLocation getBaiDuCoordinate(ReportLocation location) throws Exception{
		String url = "http://api.map.baidu.com/geoconv/v1/?coords="+location.getLongitude()+","+location.getLatitude()+"&ak="+ak;
		String message = HttpUtil.sendGet(url);
		JSONObject json = JSONObject.parseObject(message);
		List<Object> list = (List<Object>) JSON.parseObject(json.get("result").toString(), List.class);
		JSONObject jsonLocation = JSONObject.parseObject(list.get(0).toString());
		
		location.setLatitude(jsonLocation.getString("y"));
		location.setLongitude(jsonLocation.getString("x"));
		
		return location;
	}
	
	/**
	 * 通过百度坐标获取地理位置
	 * @param location
	 * @throws Exception
	 */
	public static ReportLocation getBaiDuAddress(ReportLocation location) throws Exception{
		String url = "http://api.map.baidu.com/geocoder/v2/?ak="+ak+"&location="+location.getLatitude()+","+location.getLongitude()+"&output=json";
		String message = HttpUtil.sendGet(url);
		JSONObject json = JSONObject.parseObject(message);
		logger.info("百度地理位置:"+json);
		JSONObject jsonLocation = JSONObject.parseObject(json.get("result").toString());
		location.setFormatted_address(jsonLocation.get("formatted_address").toString());
		location.setSematic_description(jsonLocation.get("sematic_description").toString());
		return location;
	}
	
	public static void main(String[] args) {
		ReportLocation location = new ReportLocation();
		location.setLatitude("22.500761");
		location.setLongitude("113.915024");
		try {
			location = getBaiDuCoordinate(location);
			getBaiDuAddress(location);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
