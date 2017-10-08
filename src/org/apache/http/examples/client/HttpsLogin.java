package org.apache.http.examples.client;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

public class HttpsLogin {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		
	    /**
	     * @throws IOException 
	     * @throws ClientProtocolException 
	     *  
	     */
		//从工具方法中获得对应的可以访问Https的httpClient
	        CloseableHttpClient httpClient =createSSLClientDefault();   
	        
	        HttpGet httpGet=new HttpGet("https://218.56.36.87");
	        //自己先在浏览器登录一下，自行复制具体的Cookie
	        //httpGet.setHeader("Cookie", "HS_ETS_SID=4jSFY2wWwT0gPrWJ45ly!-1286216704; Null=31111111.51237.0000; logtype=2; certtype=0; certNo=33****************; isorgloginpage_cookie=0; hs_etrading_customskin=app_css");
	        
	        //设置代理，方便Fiddle捕获具体信息
	        RequestConfig config=RequestConfig.custom()
	                .setProxy(HttpHost.create("127.0.0.1:8888"))
	                .build();
	        httpGet.setConfig(config);
	        //执行get请求，获得对应的响应实例
	        CloseableHttpResponse response=httpClient.execute(httpGet);
	        
	        //打印响应的到的html正文
	        HttpEntity entity =response.getEntity();
	        String html=EntityUtils.toString(entity);
	        System.out.println(html);
	        
	        //关闭连接
	        response.close();
	        httpClient.close();
	    
    }  
	/**
     * 创建一个可以访问Https类型URL的工具类，返回一个CloseableHttpClient实例
     */
    public static CloseableHttpClient createSSLClientDefault(){
        try {
            SSLContext sslContext=new SSLContextBuilder().loadTrustMaterial(
                    null,new TrustStrategy() {
                        //信任所有
                        public boolean isTrusted(X509Certificate[] chain, String authType)
                                throws CertificateException {
                            return true;
                        }
                    }).build();
            SSLConnectionSocketFactory sslsf=new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }
	}


