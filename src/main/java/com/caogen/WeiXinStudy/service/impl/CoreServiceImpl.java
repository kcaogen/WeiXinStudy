package com.caogen.WeiXinStudy.service.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.caogen.WeiXinStudy.entity.ImageMessage;
import com.caogen.WeiXinStudy.entity.ReportLocation;
import com.caogen.WeiXinStudy.entity.TextMessage;
import com.caogen.WeiXinStudy.service.CoreService;
import com.caogen.WeiXinStudy.util.BaiDuUtil;
import com.caogen.WeiXinStudy.util.MessageUtil;
import com.caogen.WeiXinStudy.util.TemplateMessageUtil;

@Service
public class CoreServiceImpl implements CoreService {

	/**
	 * 消息业务处理分发器
	 * 
	 * @param MsgType
	 */
	@Override
	public String processMessage(Map<String, String> map) {
		String reMessage = "";
		String MsgType = map.get("MsgType");

		String openid = map.get("FromUserName"); // 用户 openid
		String mpid = map.get("ToUserName"); // 公众号原始 ID

		switch (MsgType) {
		case MessageUtil.REQ_MESSAGE_TYPE_TEXT:
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(openid);
			textMessage.setFromUserName(mpid);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.REQ_MESSAGE_TYPE_TEXT);
			textMessage.setContent(map.get("Content"));
			reMessage = MessageUtil.textMessageToXml(textMessage);
			break;
		case MessageUtil.REQ_MESSAGE_TYPE_IMAGE:
			reMessage = "<xml>"+
						"<ToUserName><![CDATA["+openid+"]]></ToUserName>" +
						"<FromUserName><![CDATA["+mpid+"]]></FromUserName>" +
						"<CreateTime>"+new Date().getTime()+"</CreateTime>" +
						"<MsgType><![CDATA[image]]></MsgType>" +
						"<Image><MediaId><![CDATA[GjqnHKOGrkbKj9FE2RSlkyM5XdMjP0qWv0ZtU3bBhiY]]></MediaId></Image>" + 
						"</xml>";
			break;
		case MessageUtil.REQ_MESSAGE_TYPE_VOICE:
			reMessage = "语音消息";
			break;
		case MessageUtil.REQ_MESSAGE_TYPE_VIDEO:
			reMessage = "视频消息";
			break;
		case MessageUtil.REQ_MESSAGE_TYPE_SHORTVIDEO:
			reMessage = "小视频消息";
			break;
		case MessageUtil.REQ_MESSAGE_TYPE_LOCATION:
			reMessage = "地理位置消息";
			break;
		case MessageUtil.REQ_MESSAGE_TYPE_LINK:
			reMessage = "链接消息";
			break;
		default:
			break;
		}

		return reMessage;
	}

	/**
	 * 事件消息业务分发器
	 * 
	 * @param Event
	 * @throws Exception 
	 */
	@Override
	public String EventDispatcher(Map<String, String> map) throws Exception {
		String reMessage = "";
		String Event = map.get("Event");

		String openid = map.get("FromUserName"); // 用户 openid
		String mpid = map.get("ToUserName"); // 公众号原始 ID
		TextMessage textMessage = new TextMessage();
		switch (Event) {
		case MessageUtil.EVENT_TYPE_SUBSCRIBE:
			
			textMessage.setToUserName(openid);
			textMessage.setFromUserName(mpid);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.REQ_MESSAGE_TYPE_TEXT);
			textMessage.setContent("订阅草根公众号成功！");
			reMessage = MessageUtil.textMessageToXml(textMessage);

			// 发送消息模板
			TemplateMessageUtil.ConcernedSuccess(openid);
			break;
		case MessageUtil.EVENT_TYPE_UNSUBSCRIBE:
			// 取消订阅
			break;
			
		case MessageUtil.EVENT_TYPE_LOCATION:
			//上报地理位置
			ReportLocation location = new ReportLocation();
			location.setFromUserName(openid);
			location.setLatitude(map.get("Latitude"));
			location.setLongitude(map.get("Longitude"));
			location.setPrecision(map.get("Precision"));
			
			location = BaiDuUtil.getBaiDuCoordinate(location);
			location = BaiDuUtil.getBaiDuAddress(location);
			//发送地理位置消息模板
			TemplateMessageUtil.ConcernedLocation(location);
			break;
		case MessageUtil.EVENT_TYPE_CLICK:
			/**
			 * 点击菜单拉取消息时的事件推送
			 * 根据key的通过发送不同的消息
			 */
			String EventKey = map.get("EventKey");
			if(EventKey.equals("V1001_TODAY_MUSIC")){
				textMessage.setToUserName(openid);
				textMessage.setFromUserName(mpid);
				textMessage.setCreateTime(new Date().getTime());
				textMessage.setMsgType(MessageUtil.REQ_MESSAGE_TYPE_TEXT);
				textMessage.setContent("你是不是想听我唱歌！");
				reMessage = MessageUtil.textMessageToXml(textMessage);
			}else if(EventKey.equals("V1001_GOOD")){
				textMessage.setToUserName(openid);
				textMessage.setFromUserName(mpid);
				textMessage.setCreateTime(new Date().getTime());
				textMessage.setMsgType(MessageUtil.REQ_MESSAGE_TYPE_TEXT);
				textMessage.setContent("谢谢您的赞！");
				reMessage = MessageUtil.textMessageToXml(textMessage);
			}
			break;
		default:
			break;
		}

		return reMessage;
	}

}
