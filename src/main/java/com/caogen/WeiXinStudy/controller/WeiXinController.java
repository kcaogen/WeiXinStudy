package com.caogen.WeiXinStudy.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.caogen.WeiXinStudy.entity.User;
import com.caogen.WeiXinStudy.service.CoreService;
import com.caogen.WeiXinStudy.util.AccessTokenUtil;
import com.caogen.WeiXinStudy.util.CookieUtil;
import com.caogen.WeiXinStudy.util.MessageUtil;
import com.caogen.WeiXinStudy.util.SignUtil;
import com.caogen.WeiXinStudy.util.UserUtil;

@Controller
@RequestMapping("/weixin")
public class WeiXinController {

	private static Logger logger = Logger.getLogger(WeiXinController.class);

	@Autowired
	private CoreService coreService;

	/**
	 * 验证服务器地址的有效性
	 * 
	 * @param request
	 * @param response
	 * @author 草根
	 */
	@RequestMapping(value = "/message", method = RequestMethod.GET)
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		// 微信加密签名
		String signature = request.getParameter("signature");

		// 时间戳
		String timestamp = request.getParameter("timestamp");

		// 随机数
		String nonce = request.getParameter("nonce");

		// 随机字符串
		String echostr = request.getParameter("echostr");

		logger.info("检验signature开始");
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			logger.info("返回echostr");
			try {
				response.getWriter().print(echostr);
			} catch (IOException e) {
				logger.info("返回echostr失败！");
				e.printStackTrace();
			}
		}

		logger.info("检验signature结束");
	}

	/**
	 * 用于接收服务端消息
	 * 
	 * @param request
	 * @param response
	 * @author 草根
	 */
	@RequestMapping(value = "/message", method = RequestMethod.POST)
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, String> map = MessageUtil.parseXml(request);
			logger.info(map);
			String msgtype = map.get("MsgType");
			String reMessage = "";
			if (MessageUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgtype)) {
				reMessage = coreService.EventDispatcher(map); // 进入事件处理
			} else {
				reMessage = coreService.processMessage(map); // 进入消息处理
			}

			logger.info(reMessage);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(reMessage);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 获取用户授权code
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/userCode")
	public String getUserCode(HttpServletRequest request, HttpServletResponse response){
		try {
			boolean flag = AccessTokenUtil.getPageAccessToken(request, response);
			if(flag){
				return "redirect:/userInfo";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * 通过网页授权获取用户信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/userInfo")
	public String getUserInfo(HttpServletRequest request, HttpServletResponse response){
		try {
			//先从cookie获取access_token和openid
			String access_token = CookieUtil.getCookieValue(request, "access_token");
			String openid = CookieUtil.getCookieValue(request, "openid");
			
			if(StringUtils.isEmpty(access_token) || StringUtils.isEmpty(openid))return "userCode";
			
			User user = UserUtil.getUserInfo(access_token, openid);
			request.setAttribute("user", user);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "userInfo";
	}
}
