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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.LogUtils;

public class LoginNoUser {
	static CloseableHttpClient httpclient = HttpClients.createDefault();;
	static CloseableHttpResponse response = null;
	private static Properties urlsProp = new Properties();
	private static String sysName = "监控指挥中心";
	// static HttpHost target = new HttpHost("https://coinmarketcap.com", 443,
	// "https");
	// static HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");
	static RequestConfig config = null;
	static String cookie = "";
	// private static final String User_Agent =
	// "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

	// 加载用户配置文件
	static {
		try {
			urlsProp.load(LoginNoUser.class.getClassLoader()
					.getResourceAsStream("resource/urls.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {

		doAction();

		if (response != null) {
			response.close();
		}
		if (httpclient != null) {
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

	static boolean doAction() {
		boolean success = false;
		String currency="", pair="",price="";
		
		int i;
		try {
			String url = "https://coinmarketcap.com/exchanges/poloniex/";
			Document doc = Jsoup.connect(url).get();
			Elements trs = doc.getElementsByTag("tr");
			for (Element tr : trs) {
				Elements tds = tr.getElementsByTag("td");
				i = 0;
				for (Element td : tds) {
					if (i == 1) {
						currency = td.text();
					}
					if (i == 2) {
						pair = td.text();
					}
					if (i==4){
						price=td.text();
					}
					i++;
				}
				if  ("Ethereum".equals(currency)&&"ETH/BTC".equals(pair)){
					System.out.println(currency+":"+pair+":"+price);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

}
