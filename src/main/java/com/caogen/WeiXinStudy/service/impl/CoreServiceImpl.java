package com.caogen.WeiXinStudy.service.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.caogen.WeiXinStudy.entity.ImageMessage;
import com.caogen.WeiXinStudy.entity.TextMessage;
import com.caogen.WeiXinStudy.service.CoreService;
import com.caogen.WeiXinStudy.util.MessageUtil;

@Service
public class CoreServiceImpl implements CoreService {
	
	/**
	 * 消息业务处理分发器
	 * @param MsgType
	 */
	@Override
	public String processMessage(Map<String, String> map){
		String reMessage = "";
		String MsgType = map.get("MsgType");
		
		String openid=map.get("FromUserName"); //用户 openid
		String mpid=map.get("ToUserName");   //公众号原始 ID
		
		switch (MsgType) {
			case MessageUtil.REQ_MESSAGE_TYPE_TEXT:
				TextMessage textMessage = new TextMessage();
				textMessage.setToUserName(openid);
				textMessage.setFromUserName(mpid);
				textMessage.setCreateTime(new Date().getTime());
				textMessage.setMsgType(MessageUtil.REQ_MESSAGE_TYPE_TEXT);
				textMessage.setContent("你好，我是草根！"); 
				reMessage = MessageUtil.textMessageToXml(textMessage);
				break;
			case MessageUtil.REQ_MESSAGE_TYPE_IMAGE:
				ImageMessage imageMessage = new ImageMessage();
				imageMessage.setToUserName(openid);
				imageMessage.setFromUserName(mpid);
				imageMessage.setCreateTime(new Date().getTime());
				imageMessage.setMsgType(MessageUtil.REQ_MESSAGE_TYPE_IMAGE);
				imageMessage.setPicUrl("http://kcaogen.ngrok.cc/image/lyf.jpg");
				reMessage = MessageUtil.imageMessageToXml(imageMessage);
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
	 * @param Event
	 */
	@Override
	public String EventDispatcher(Map<String, String> map){
		String reMessage = "";
		String Event = map.get("Event");
		
		String openid=map.get("FromUserName"); //用户 openid
		String mpid=map.get("ToUserName");   //公众号原始 ID
		
		switch (Event) {
		case MessageUtil.EVENT_TYPE_SUBSCRIBE:
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(openid);
			textMessage.setFromUserName(mpid);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.REQ_MESSAGE_TYPE_TEXT);
			textMessage.setContent("订阅草根公众号成功！"); 
			reMessage = MessageUtil.textMessageToXml(textMessage);
			break;
		case MessageUtil.EVENT_TYPE_UNSUBSCRIBE:
			//取消订阅
			break;
		default:
			break;
		}
		
		return reMessage;
	}

}
