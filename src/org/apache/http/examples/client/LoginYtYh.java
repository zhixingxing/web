package org.apache.http.examples.client;

import java.io.IOException;
import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import utils.LogUtils;

public class LoginYtYh {
	static CloseableHttpClient httpclient = HttpClients.createDefault();
	static CloseableHttpResponse response = null;
	private static Properties usersProp = new Properties();
	private static Properties urlsProp = new Properties();
	private static String sysName = "";   
	static HttpHost target=new HttpHost("www.sdmingjian.top",80,"http");
	static HttpHost proxy=new HttpHost("127.0.0.1",8888,"http");
	static RequestConfig config=RequestConfig.custom().setProxy(proxy).build();
	// 加载用户配置文件
	static {
		try {
			usersProp.load(LoginYtYh.class.getClassLoader()
					.getResourceAsStream("resource/users.properties"));
			urlsProp.load(LoginYtYh.class.getClassLoader().getResourceAsStream(
					"resource/urls.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		openbrower();
	
		
		String[] users = usersProp.getProperty("users").split(",");
		String[] pwds = usersProp.getProperty("pwds").split(",");
		int i = 0;
		for (String user : users) {
			login(user, pwds[i]);			
			doAction();
			logout();
			i++;
		}

	}

	/**
	 * 记录需要的日志信息以及界面提示信息
	 * 
	 * @param info
	 * @param e
	 */
	public static void logEx(String info, Exception e) {
		// 日志信息
		// LogUtil.addLog(sysName + serviceName + info);
		LogUtils.addLog(sysName + info);
		if (e != null) {
			StackTraceElement[] stes = e.getStackTrace();
			for (StackTraceElement ste : stes) {
				LogUtils.addLog(ste.toString());
			}
		}

	}

	static boolean login(String username, String pwd) {
		boolean success = false;
		HttpUriRequest login = org.apache.http.client.methods.RequestBuilder
				.post("/j_spring_security_check")
				.addParameter("j_username", username)
				.addParameter("j_password", pwd)
				.setConfig(config)
				.build();
		try {
			response = httpclient.execute(target,login);
			String location = response.getFirstHeader("Location").getValue();
			if ("http://www.sdmingjian.top/admin/index".equals(location)) {
				System.out.println(username + "登录成功");
				System.out.println("cokie:"+response.getFirstHeader("Set-Cookie").getValue());
				logEx(username + "登录成功", null);
				success = true;
			} else {
				System.out.println("登录失败");
				logEx(username + "登录失败", null);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	static boolean doAction() {
		boolean success = false;
		try {
			String[] urls = urlsProp.getProperty("urls").split(",");
			String[] bzs = urlsProp.getProperty("response_bzs").split(",");
			String[] modules = urlsProp.getProperty("modules").split(",");
			int i = 0;
			for (String url : urls) {
				HttpPost httpget = new HttpPost(url);
				
				httpget.setConfig(config);
				response = httpclient.execute(target,httpget);
				String content = EntityUtils.toString(response.getEntity());
				String module = null;
				if (content.contains(bzs[i])) {
					module = new String(modules[i].getBytes("iso8859-1"),
							"utf-8");
					System.out.println("点击" + module + "操作成功");
					logEx("点击" + module + "操作成功", null);
				} else {
					System.out.println("操作" + module+ "失败");
					logEx("操作" + module + "失败", null);
				}
				i++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	static boolean logout() {
		boolean success = false;
		HttpGet httpget = new HttpGet(
				"http://www.sdmingjian.top/j_spring_security_logout");
		try {
			response = httpclient.execute(httpget);
			String content = EntityUtils.toString(response.getEntity());
			if (content.contains("j_spring_security_check")) {
				System.out.println("注销成功");
				logEx("注销成功", null);
			} else {
				System.out.println("注销失败");
				logEx("注销失败", null);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	static boolean openbrower() {
		boolean success = false;
		HttpGet httpget = null;
		try {
			httpget = new HttpGet("http://www.sdmingjian.top/");
			response = httpclient.execute(httpget);
			String content = EntityUtils.toString(response.getEntity());
			if (content.contains("j_spring_security_check")) {
				System.out.println("打开浏览器");
			} else {
				System.out.println("打开浏览器失败");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}
}
