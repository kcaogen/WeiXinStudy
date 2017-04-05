package com.caogen.WeiXinStudy.util;

import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis连接池
 * @author Administrator
 *
 */
public class RedisPool {
	
	private static JedisPool jedisPool = null;
	private static String host = "192.168.99.100";
	private static int port = 6379;
	
	static{
		JedisPoolConfig config = new JedisPoolConfig();
        jedisPool = new JedisPool(config, host, port, 3000, "caogen");
	}
	
	
	public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public synchronized static JedisPool getJedisPool() {
        try {
        	return jedisPool;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void close(final Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
    
    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedis();
        //do something
        //jedis.set("info", "ddd");
        System.out.println(jedis.get("info"));

        if(StringUtils.isEmpty(jedis.get("info"))){
        	System.out.println(1);
        }
        
        RedisPool.close(jedis);
    }
	
}
