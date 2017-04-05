package com.caogen.WeiXinStudy.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/weixin")
public class WeiXinController {

	private static Logger logger = Logger.getLogger(WeiXinController.class); 
	
	@RequestMapping("/helloworld")
	public String sayHello() {
		logger.info("Hello World!");
		System.out.println("Hello World!");
		return "weixin";
	}
}
