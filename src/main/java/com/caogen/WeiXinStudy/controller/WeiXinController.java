package com.caogen.WeiXinStudy.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.caogen.WeiXinStudy.util.SignUtil;


@Controller
@RequestMapping("/weixin")
public class WeiXinController {

	private static Logger logger = Logger.getLogger(WeiXinController.class); 
	
	/**
	 * 验证服务器地址的有效性
	 * @param request
	 * @param response
	 * @author 草根
	 */
	@RequestMapping("/checkWeiXin")
	public void checkWeiXin(HttpServletRequest request, HttpServletResponse response) {
		//微信加密签名
		String signature = request.getParameter("signature");
		
		//时间戳
		String timestamp = request.getParameter("timestamp");
		
		//随机数
		String nonce = request.getParameter("nonce");
		
		//随机字符串
		String echostr = request.getParameter("echostr");
		
		logger.info("检验signature开始");
		if(SignUtil.checkSignature(signature, timestamp, nonce)){
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
}
