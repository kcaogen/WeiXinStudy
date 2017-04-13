package com.caogen.WeiXinStudy.util;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caogen.WeiXinStudy.entity.User;

/**
 * 微信用户管理
 * 
 * @author 草根
 *
 */
public class UserUtil {

	/**
	 * 获取公众号的关注者列表 关注者列表由一串OpenID（加密后的微信号，每个用户对每个公众号的OpenID是唯一的）组成。
	 * 一次拉取调用最多拉取10000个关注者的OpenID，可以通过多次拉取的方式来满足需求。
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getUserList() throws Exception {
		String access_token = AccessTokenUtil.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + access_token;
		String message = HttpUtil.sendGet(url);

		JSONObject json = JSONObject.parseObject(message);
		JSONObject openJson = JSONObject.parseObject(json.get("data").toString());

		List<String> openIdList = (List<String>) JSON.parseObject(openJson.get("openid").toString(), List.class);
		return openIdList;
	}

	/**
	 * 获取用户基本信息（包括UnionID机制）
	 * 
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public static User getUserInfo(String openid) throws Exception {
		String access_token = AccessTokenUtil.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + access_token + "&openid=" + openid
				+ "&lang=zh_CN";
		String message = HttpUtil.sendGet(url);

		User user = JSON.parseObject(message, User.class);

		return user;
	}
	
	/**
	 * 通过网页授权的access_token获取用户信息
	 * @param access_token
	 * @param openid
	 * @return
	 * @throws Exception 
	 */
	public static User getUserInfo(String access_token, String openid) throws Exception{
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid
				+ "&lang=zh_CN";
		String message = HttpUtil.sendGet(url);

		User user = JSON.parseObject(message, User.class);

		return user;
	}

	public static void main(String[] args) {
		try {
			List<String> openIdList = getUserList();
			int size = openIdList.size();

			for (int i = 0; i < size; i++) {
				User user = getUserInfo(openIdList.get(i));
				System.err.println(user.getNickname() + ":" + JSONObject.toJSONString(user));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
