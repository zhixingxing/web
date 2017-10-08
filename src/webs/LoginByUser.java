package webs;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import utils.LogUtils;
public class LoginByUser {
	static CloseableHttpClient httpclient = null;
	static CloseableHttpResponse response = null;
	private static Properties usersProp = new Properties();
	private static Properties urlsProp = new Properties();
	private static String sysName = "监控指挥中心";
	private static String jsessionid = "";
	static HttpHost target = new HttpHost("76.12.152.1", 7001, "http");
    //static HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");
   static  RequestConfig config = null;
   static String cookie="";
   private static final String User_Agent = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; InfoPath.2)";

	// 加载用户配置文件
	static {
		try {
			usersProp.load(LoginByUser.class.getClassLoader()
					.getResourceAsStream("resource/users.properties"));
			urlsProp.load(LoginByUser.class.getClassLoader().getResourceAsStream(
					"resource/urls.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		String[] originUsers = usersProp.getProperty("users").split(",");
		String pwds = usersProp.getProperty("pwds");
		//随机生成操作模块，为总数-10
		int k=originUsers.length-10;
		String []users= new String[k];
		for(int i=0;i<k;i++){
			int index=(int)(Math.random()*k);
			users[i]=originUsers[index];
		}
	
		//int i = 0;
		for (String user : users) {
			openbrower();
			login(user, pwds);
			doAction();
			logout();
			//i++;
		}
		if(response!=null){
			response.close();
		}
	    if(httpclient!=null){
	    	httpclient.close();
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
		 cookie="username="+username+"; "+jsessionid;	
		config=RequestConfig.custom()
	            .setCookieSpec(cookie)
	            .build();
		HttpUriRequest login = org.apache.http.client.methods.RequestBuilder
				.post("/pc/logincmd.cmd?method=doLogin").setConfig(config)
				.addParameter("j_username", username)
				.addParameter("j_password", pwd).addHeader("Cookie", cookie).addHeader("User-Agent", User_Agent).	
				addHeader("Referer","http://76.12.152.1:7001/pc/")
				.addHeader("Content-Type","application/x-www-form-urlencoded")
				.addParameter("rdmCode","")
				.build();
		try {
			response = httpclient.execute(target,login);			 
			String content = EntityUtils.toString(response.getEntity());
			if (content.contains("首页")) {
				System.out.println(username + "登录成功");
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
			String[] originUrls = urlsProp.getProperty("urls").split(",");
			String[] originModules = urlsProp.getProperty("modules").split(",");
			//随机生成操作模块，为总数-1
			int k=originUrls.length-1;
			String []urls= new String[k];
			String[]modules=new String[k];
			for(int i=0;i<k;i++){
				int index=(int)(Math.random()*k);
				urls[i]=originUrls[index];
				modules[i]=originModules[index];
			}
			//String[] bzs = urlsProp.getProperty("response_bzs").split(",");
			
			String logurl=urlsProp.getProperty("logurl");
			int i = 0;
			for (String url : urls) {
				//随即间隔秒数
				int interValSecond=(int) (Math.random()*100000);
				Thread.sleep(interValSecond);
				HttpGet httpget = new HttpGet(url);
				initHttpGet(httpget);
				response.close();
				response = httpclient.execute(target,httpget);			
				//String content = EntityUtils.toString(response.getEntity());
				String status=response.getStatusLine().toString();
				String module = null;
				if (status.contains("200") ){
				
					module = new String(modules[i].getBytes("iso8859-1"),
							"utf-8");
					System.out.println("点击" + module + "操作成功");
					logEx("点击" + module + "操作成功", null);
				} else {
					System.out.println("操作" + module+ "失败");
					logEx("操作" + module + "失败", null);
				}
				HttpPost httppost = new HttpPost(logurl);
				initHttpPost(httppost,url);
				//httpget.setHeader("Referer","http://76.12.152.1:7001/pc/");
				response.close();
				response = httpclient.execute(target,httppost);
				i++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}
  static void initHttpGet(HttpGet httpget){
	   httpget.setConfig(config);
		httpget.setHeader("Cookie", cookie);
		httpget.setHeader("User-Agent", User_Agent);
  }
  static void initHttpPost(HttpPost httppost,String mainUrl) throws UnsupportedEncodingException{
	  httppost.setConfig(config);
	  String url=mainUrl.substring(4,mainUrl.indexOf("&"));
	 String gndm=mainUrl.substring(mainUrl.indexOf("gndm=")+5,16+mainUrl.indexOf("gndm=")+5);
	  httppost.setHeader("Cookie", cookie);
	  httppost.setHeader("User-Agent", User_Agent);
	  List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("gndm", gndm));
		formparams.add(new BasicNameValuePair("url", url ));	
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
				"UTF-8");
		httppost.setEntity(entity);
  }
	static boolean logout() {
		boolean success = false;
		HttpGet httpget = new HttpGet(
				"http://76.12.152.1:7001/pc/logincmd.cmd?method=loginOut");
		try {
			response = httpclient.execute(httpget);
			String content = EntityUtils.toString(response.getEntity());
			if (content.contains("frmLogin")) {
				System.out.println("注销成功");
				logEx("注销成功", null);
				httpclient.close();
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
		httpclient=HttpClients.createDefault();
		try {
			httpget = new HttpGet("http://76.12.152.1:7001/pc/");
			response = httpclient.execute(httpget);
			String content = EntityUtils.toString(response.getEntity());
		    String[] header	=response.getFirstHeader("Set-Cookie").getValue().split(";");
			if (content.contains("frmLogin")) {
				System.out.println("打开浏览器");
			
				jsessionid=header[0];
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
