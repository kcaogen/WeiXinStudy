package com.caogen.WeiXinStudy.util;

import java.util.Arrays;

import org.springframework.util.StringUtils;

public class SignUtil {

	private final static String token = "kcaogen";

	/**
	 * 检验signature
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 * @author 草根
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce) {
		String[] str = { token, timestamp, nonce };
		if(StringUtils.isEmpty(str))return false;
		Arrays.sort(str); // 字典序排序
		String bigStr = str[0] + str[1] + str[2];
		// SHA1加密
		String digest = new SHA1().getDigestOfString(bigStr.getBytes()).toLowerCase();

		if (digest.equals(signature)) {
			return true;
		}

		return false;
	}

}
