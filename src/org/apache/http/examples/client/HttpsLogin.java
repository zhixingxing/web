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
		//�ӹ��߷����л�ö�Ӧ�Ŀ��Է���Https��httpClient
	        CloseableHttpClient httpClient =createSSLClientDefault();   
	        
	        HttpGet httpGet=new HttpGet("https://218.56.36.87");
	        //�Լ������������¼һ�£����и��ƾ����Cookie
	        //httpGet.setHeader("Cookie", "HS_ETS_SID=4jSFY2wWwT0gPrWJ45ly!-1286216704; Null=31111111.51237.0000; logtype=2; certtype=0; certNo=33****************; isorgloginpage_cookie=0; hs_etrading_customskin=app_css");
	        
	        //���ô�������Fiddle���������Ϣ
	        RequestConfig config=RequestConfig.custom()
	                .setProxy(HttpHost.create("127.0.0.1:8888"))
	                .build();
	        httpGet.setConfig(config);
	        //ִ��get���󣬻�ö�Ӧ����Ӧʵ��
	        CloseableHttpResponse response=httpClient.execute(httpGet);
	        
	        //��ӡ��Ӧ�ĵ���html����
	        HttpEntity entity =response.getEntity();
	        String html=EntityUtils.toString(entity);
	        System.out.println(html);
	        
	        //�ر�����
	        response.close();
	        httpClient.close();
	    
    }  
	/**
     * ����һ�����Է���Https����URL�Ĺ����࣬����һ��CloseableHttpClientʵ��
     */
    public static CloseableHttpClient createSSLClientDefault(){
        try {
            SSLContext sslContext=new SSLContextBuilder().loadTrustMaterial(
                    null,new TrustStrategy() {
                        //��������
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


