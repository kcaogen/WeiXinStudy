package com.caogen.WeiXinStudy.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
	
	public static String sendPost(String url, File file) throws Exception{
		URL realUrl = new URL(url);
		// 打开和URL之间的连接
		URLConnection con = realUrl.openConnection();
		
		String result =null;  
		con.setDoInput(true);  
		  
		con.setDoOutput(true);  
		  
		con.setUseCaches(false); // post方式不能使用缓存  
		  
		// 设置请求头信息  
		  
		con.setRequestProperty("Connection", "Keep-Alive");  
		  
		con.setRequestProperty("Charset", "UTF-8");  
		// 设置边界  
		  
		String BOUNDARY = "----------" + System.currentTimeMillis();  
		  
		con.setRequestProperty("Content-Type",  
		        "multipart/form-data; boundary="  
		  
		        + BOUNDARY);  
		  
		// 请求正文信息  
		  
		// 第一部分：  
		  
		StringBuilder sb = new StringBuilder();  
		  
		sb.append("--"); // 必须多两道线  
		  
		sb.append(BOUNDARY);  
		  
		sb.append("\r\n");  
		  
		sb.append("Content-Disposition: form-data;name=\"media\";filelength=\""+file.length()+"\";filename=\""  
		  
		        + file.getName() + "\"\r\n");  
		  
		sb.append("Content-Type:application/octet-stream\r\n\r\n");  
		  
		byte[] head = sb.toString().getBytes("utf-8");  
		  
		// 获得输出流  
		  
		OutputStream out = new DataOutputStream(con.getOutputStream());  
		  
		// 输出表头  
		  
		out.write(head);  
		  
		// 文件正文部分  
		  
		// 把文件已流文件的方式 推入到url中  
		  
		DataInputStream in = new DataInputStream(new FileInputStream(file));  
		  
		int bytes = 0;  
		  
		byte[] bufferOut = new byte[1024];  
		  
		while ((bytes = in.read(bufferOut)) != -1) {  
		  
		    out.write(bufferOut, 0, bytes);  
		  
		}  
		  
		in.close();  
		  
		// 结尾部分  
		  
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线  
		  
		out.write(foot);  
		  
		out.flush();  
		  
		out.close();  
		  
		StringBuffer buffer = new StringBuffer();  
		  
		BufferedReader reader = null;  
		  
		try {  
		  
		    // 定义BufferedReader输入流来读取URL的响应  
		  
		    reader = new BufferedReader(new InputStreamReader(con.getInputStream()));  
		  
		    String line = null;  
		  
		    while ((line = reader.readLine()) != null) {  
		  
		        buffer.append(line);  
		  
		    }  
		  
		    if (result == null) {  
		  
		        result = buffer.toString();  
		  
		    }  
		  
		} catch (IOException e) {  
		  
		    System.out.println("发送POST请求出现异常！" + e);  
		  
		    e.printStackTrace();  
		  
		    throw new IOException("数据读取异常");  
		  
		} finally {  
		  
		    if (reader != null) {  
		  
		        reader.close();  
		  
		    }  
		  
		}  
		return result;  
	}

}
