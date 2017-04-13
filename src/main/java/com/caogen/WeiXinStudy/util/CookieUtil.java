package com.caogen.WeiXinStudy.util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author 曹雄
 * @date  2015年8月27日16:19:26
 * @category cookie工具类
 */

public class CookieUtil {
	
	/**
	 * 添加cookie
	 * @param response
	 * @param name
	 * @param value
	 * @param age
	 */
	public static void addCookie(HttpServletResponse response, String name, String value, int age) {
		Cookie cookies = new Cookie(name, value);
		cookies.setPath("/");
		cookies.setMaxAge(age);
		response.addCookie(cookies);
	}
	
	public static void addDomainCookie(HttpServletResponse response, String name, String value, int age) {
		Cookie cookies = new Cookie(name, value);
		cookies.setDomain("kcaogen.com"); 
		cookies.setPath("/");
		cookies.setMaxAge(age);
		response.addCookie(cookies);
	}
	
	/**
	 * 添加cookie并设置共享域
	 * @param response
	 * @param name
	 * @param value
	 * @param age
	 * @param url
	 */
	public static void addCookie(HttpServletResponse response, String name, String value, int age,String url) {
		Cookie cookies = new Cookie(name, value);
		cookies.setPath("/");
		cookies.setDomain(url);
		cookies.setMaxAge(age);
		response.addCookie(cookies);
	}

	/**
	 * 从单个cookie中获取cookie值
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		if (cookieName != null) {
			Cookie cookie = getCookie(request, cookieName);
			if (cookie != null) {
				return cookie.getValue();
			}
		}
		return "";
	}

	/**
	 * 从多个cookie中获取cookie值
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		Cookie cookie_tagret = null;
		try {
			if (cookies != null && cookies.length > 0) {
				for (int i = 0; i < cookies.length; i++) {
					cookie = cookies[i];
					if (cookie.getName().equals(cookieName)) {
						cookie_tagret=cookies[i];
						return cookie_tagret;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cookie_tagret;
	}

	/**
	 * 删除cookie
	 * @param request
	 * @param response
	 * @param cookieName
	 * @return
	 */
	public static boolean deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
		if (cookieName != null) {
			Cookie cookie = getCookie(request, cookieName);
			if (cookie != null) {
				cookie.setMaxAge(0);
				cookie.setPath("/");
				cookie.setValue("");
				response.addCookie(cookie);
				return true;
			}
		}
		return false;
	}

	/**
	 * 删除cookie
	 * @param httpservletresponse
	 * @param httpservletrequest
	 * @param s
	 * @throws UnsupportedEncodingException
	 */
	public static void delCookie(HttpServletResponse httpservletresponse, HttpServletRequest httpservletrequest, String s) throws UnsupportedEncodingException {
		Cookie acookie[] = httpservletrequest.getCookies();
		if (acookie != null) {
			for (int i = 0; i < acookie.length; i++) {
				String s1 = acookie[i].getName();
				if (s1.equals(s)) {
					acookie[i].setMaxAge(0);
					httpservletresponse.addCookie(acookie[i]);
				}
			}

		}
	}
}
