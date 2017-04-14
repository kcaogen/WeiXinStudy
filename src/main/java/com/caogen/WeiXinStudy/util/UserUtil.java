package com.caogen.WeiXinStudy.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caogen.WeiXinStudy.entity.AccessToken;
import com.caogen.WeiXinStudy.entity.PublicAccount;
import com.caogen.WeiXinStudy.entity.User;

import redis.clients.jedis.Jedis;

/**
 * 微信用户管理
 * 
 * @author 草根
 *
 */
public class UserUtil {
	
	private static Logger logger = Logger.getLogger(UserUtil.class); 

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
	 * @param openid
	 * @return
	 * @throws Exception 
	 */
	public static User getPageUserInfo(String openid) throws Exception{
		//通过openid获取从缓存获取token等数据
		Jedis jedis = RedisPool.getJedis();
		String tokenJson = jedis.get(openid);
		if(StringUtils.isEmpty(tokenJson)){
			RedisPool.close(jedis);
			return null;
		}
		
		AccessToken accessToken = JSON.parseObject(tokenJson, AccessToken.class);
		
		//检验授权凭证（access_token）是否有效
		String checkUrl = "https://api.weixin.qq.com/sns/auth?access_token="+accessToken.getAccess_token()+"&openid="+openid+"";
		String checkMessage = HttpUtil.sendGet(checkUrl);
		
		JSONObject json = JSONObject.parseObject(checkMessage);
		String errcode = json.get("errcode").toString();
		if(!errcode.equals("0")){
			//这个时候说明token失效或请求失败
			String refresh_token = accessToken.getRefresh_token();
			if(StringUtils.isEmpty(refresh_token)){
				RedisPool.close(jedis);
				return null;
			}
			
			//通过refresh_token刷新access_token
			String updateUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="+PublicAccount.getAppid()+"&grant_type=refresh_token&refresh_token="+refresh_token+"";
			String updateMessage = HttpUtil.sendGet(updateUrl);
			JSONObject updateJson = JSONObject.parseObject(updateMessage);

			if(updateJson.get("errcode") != null){
				RedisPool.close(jedis);
				logger.info("刷新网页access_token返回错误码:" + updateJson.get("errcode").toString());
				return null;
			}
			
			accessToken.setAccess_token(updateJson.getString("access_token"));
			accessToken.setRefresh_token(updateJson.getString("refresh_token"));
			
			jedis.set(openid, JSONObject.toJSONString(accessToken));
			RedisPool.close(jedis);
		}
		
		
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken.getAccess_token() + "&openid=" + openid
				+ "&lang=zh_CN";
		String message = HttpUtil.sendGet(url);

		User user = JSON.parseObject(message, User.class);
		String headImgUrl = user.getHeadimgurl();
		headImgUrl = headImgUrl.substring(0, headImgUrl.lastIndexOf("0"));
		headImgUrl = headImgUrl + "132";
		user.setHeadimgurl(headImgUrl);

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
