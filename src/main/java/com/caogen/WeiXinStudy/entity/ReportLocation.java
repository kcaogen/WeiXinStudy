package com.caogen.WeiXinStudy.entity;

/**
 * 上报地理位置实体
 * @author 草根
 *
 */
public class ReportLocation {
	
	//开发者微信号
	private String ToUserName;
	
	//发送方帐号（一个OpenID）
	private String FromUserName;
	
	//消息创建时间 （整型）
	private long CreateTime;
	
	//地理位置纬度
	private String Latitude;
	
	//地理位置经度
	private String Longitude;
	
	//地理位置精度
	private String Precision;
	
	//百度地图的地址
	private String formatted_address;
	
	//百度地图对于地址的描述
	private String sematic_description;

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public String getPrecision() {
		return Precision;
	}

	public void setPrecision(String precision) {
		Precision = precision;
	}

	public String getFormatted_address() {
		return formatted_address;
	}

	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	public String getSematic_description() {
		return sematic_description;
	}

	public void setSematic_description(String sematic_description) {
		this.sematic_description = sematic_description;
	}
	
}
