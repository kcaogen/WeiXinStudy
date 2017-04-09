package com.caogen.WeiXinStudy.util;

import java.io.File;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSONObject;

public class UploadUtil {
	
	private static Logger logger = Logger.getLogger(UploadUtil.class);
	
	/** 
	 * 上传其他永久素材(图片素材的上限为5000，其他类型为1000) 
	 * @return 
	 * @throws Exception 
	 */  
	public static JSONObject addMaterialEver(File file,String type) throws Exception {  
	    try {  
	        logger.info("开始上传"+type+"永久素材---------------------");   
	          
	        //开始获取证书  
	        String access_token = AccessTokenUtil.getAccessToken();
	          
	        //上传素材    
	        String path="https://api.weixin.qq.com/cgi-bin/material/add_material?access_token="+access_token+"&type="+type;  
	        String result=HttpUtil.sendPost(path, file);  
	        result=result.replaceAll("[\\\\]", "");  
	        logger.info("result:"+result);      
	        JSONObject resultJSON=JSONObject.parseObject(result);  
	        if(resultJSON!=null){  
	            if(resultJSON.get("media_id")!=null){  
	                logger.info("上传"+type+"永久素材成功");  
	                return resultJSON;  
	            }else{  
	                logger.info("上传"+type+"永久素材失败");  
	            }  
	        }  
	          
	        return null;  
	    } catch (Exception e) {  
	        logger.info("程序异常", e);  
	        throw e;  
	    }finally{  
	        logger.info("结束上传"+type+"永久素材---------------------");   
	    }  
	}  
	  
	/** 
	 * 上传图片到微信服务器(本接口所上传的图片不占用公众号的素材库中图片数量的5000个的限制。图片仅支持jpg/png格式，大小必须在1MB以下) 
	 * @param type 
	 * @param file 
	 * @return 
	 * @throws Exception 
	 */  
	public static JSONObject addMaterialEver(File file) throws Exception {  
	    try {  
	        logger.info("开始上传图文消息内的图片---------------------");   
	          
	        //开始获取证书  
	        String access_token = AccessTokenUtil.getAccessToken();       
	          
	        //上传图片素材      
	        String path="https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token="+access_token;  
	        String result=HttpUtil.sendPost(path, file);  
	        result=result.replaceAll("[\\\\]", "");  
	        logger.info("result:"+result);      
	        JSONObject resultJSON=JSONObject.parseObject(result);  
	        if(resultJSON!=null){  
	            if(resultJSON.get("url")!=null){  
	                logger.info("上传图文消息内的图片成功");  
	                return resultJSON;  
	            }else{  
	                logger.info("上传图文消息内的图片失败");  
	            }  
	        }  
	          
	        return null;  
	    } catch (Exception e) {  
	        logger.info("程序异常", e);  
	        throw e;  
	    }finally{  
	        logger.info("结束上传图文消息内的图片---------------------");   
	    }  
	}  

	public static void main(String[] args) {
		File file = new File("D:/lyf.jpg");
		try {
			System.out.println(addMaterialEver(file, "image"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
