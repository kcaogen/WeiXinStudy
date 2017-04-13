package com.caogen.WeiXinStudy.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.caogen.WeiXinStudy.entity.PublicAccount;

import redis.clients.jedis.Jedis;

public class AccessTokenUtil {
	private static Logger logger = Logger.getLogger(AccessTokenUtil.class); 
	
	private static final String ACCESSTOKEN = "access_token";
	
	/**
	 * 获取accessToken
	 * @param appID		微信公众号凭证
	 * @param appScret	微信公众号凭证秘钥
	 * @return
	 * @throws Exception 
	 */
	public static String getAccessToken() throws Exception {
		Jedis jedis = RedisPool.getJedis();
		String access_token = jedis.get(ACCESSTOKEN);
		if(!StringUtils.isEmpty(access_token)){
			RedisPool.close(jedis);
        	return access_token;
        }
		
		/**
		 * 以防多个线程同时调用这个方法并且redis里面没有数据
		 * 这个时候只能有一个线程通过微信服务器获取access_token
		 * 当第一个线程执行完这些同步代码块之后，redis有数据了
		 * 第二个线程进来同步代码块之后就不会再去微信服务器获取access_token
		 */
		int expires_in;
		synchronized (ACCESSTOKEN) {
			access_token = jedis.get(ACCESSTOKEN);
			if(!StringUtils.isEmpty(access_token)){
				RedisPool.close(jedis);
	        	return access_token;
	        }
			
			// 访问微信服务器
			String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + PublicAccount.getAppid() + "&secret="
					+ PublicAccount.getAppsecret();
			String message = HttpUtil.sendGet(url);
			
			if(StringUtils.isEmpty(message)){
				return null;
			}
			
			JSONObject json = JSONObject.parseObject(message);

			if(json.get("errcode") != null){
				logger.info("获取access_token返回错误码:" + json.get("errcode").toString());
				return null;
			}
			access_token = json.get(ACCESSTOKEN).toString();
			expires_in = Integer.parseInt(json.get("expires_in").toString())-100;
			
			/**
			 * access_token的有效期目前为2个小时，需定时刷新，重复获取将导致上次获取的access_token失效。
			 * 虽然有效期是两个小时，但是缓存有效时间还是少于两个小时更好点，以防网络延迟等事故
			 */
			jedis.setex(ACCESSTOKEN, expires_in, access_token);
			
		}
		RedisPool.close(jedis);
		
		return access_token;
	}
	
	/**
	 * 获取网页授权的access_token
	 * @return
	 * @throws Exception 
	 */
	public static boolean getPageAccessToken(HttpServletRequest request) throws Exception{
		String code = request.getParameter("code");
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+PublicAccount.getAppid()+"&secret="+PublicAccount.getAppsecret()+"&code="+code+"&grant_type=authorization_code";
		String message = HttpUtil.sendGet(url);
		
		if(StringUtils.isEmpty(message)){
			return false;
		}
		
		JSONObject json = JSONObject.parseObject(message);

		if(json.get("errcode") != null){
			logger.info("获取网页access_token返回错误码:" + json.get("errcode").toString());
			return false;
		}
		
		String access_token, refresh_token, openid, unionid;
		access_token = json.getString("access_token");
		refresh_token = json.getString("refresh_token");
		openid = json.getString("openid");
		unionid = json.getString("unionid");
		
		request.getSession().setAttribute("access_token", access_token);
		request.getSession().setAttribute("refresh_token", refresh_token);
		request.getSession().setAttribute("openid", openid);
		request.getSession().setAttribute("unionid", unionid);
		
		return true;
	}
	
	public static void main(String[] args) {
		/*try {
			System.out.println(getAccessToken());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i = 0; i < 4; i++){
			exec.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						System.out.println(getAccessToken());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		exec.shutdown();
		
	}
}
