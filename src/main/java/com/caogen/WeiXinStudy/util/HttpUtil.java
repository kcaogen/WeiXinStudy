package com.caogen.WeiXinStudy.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtil {

	public static String sendGet(String url) throws Exception {
		String message = null;

		URL getUrl = new URL(url);
		HttpURLConnection http = (HttpURLConnection) getUrl.openConnection();
		http.setRequestMethod("GET");
		http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		http.setDoOutput(true);
		http.setDoInput(true);

		http.connect();
		InputStream is = http.getInputStream();
		int size = is.available();
		byte[] b = new byte[size];
		is.read(b);

		message = new String(b, "UTF-8");

		return message;
	}

	public static String sendPost(String url, String param) throws Exception {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";

		URL realUrl = new URL(url);
		// 打开和URL之间的连接
		URLConnection conn = realUrl.openConnection();
		// 设置通用的请求属性
		conn.setRequestProperty("user-agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0)");
		// 发送POST请求必须设置如下两行
		conn.setDoOutput(true);
		conn.setDoInput(true);
		// 获取URLConnection对象对应的输出流
		OutputStreamWriter outWriter = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
		out = new PrintWriter(outWriter);
		// 发送请求参数
		out.print(param);
		// flush输出流的缓冲
		out.flush();
		// 定义BufferedReader输入流来读取URL的响应
		in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = in.readLine()) != null) {
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			result += line;
		}

		out.close();
		in.close();

		return result;
	}

}
