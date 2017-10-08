package org.apache.http.examples.client;

import org.apache.http.HeaderIterator;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;


public class RedirctLogin {

	public static void main(String[] args) {
	
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(new BasicCookieStore()) 
				.setRedirectStrategy(new LaxRedirectStrategy()).build(); 
		
		try { 
			// �ȷ�����ҳ���õ�cookie 
			// cookie��Ϣ�Զ�������HttpClient�� 
		
			//��½�ǻ۳��� 
			HttpGet getMethod = new HttpGet("http://www.sdmingjian.top/");
    
				httpClient.execute(getMethod);
	        // Я��cookie���ʵ�¼����
	        // ���õ�¼���˺�������
	        HttpUriRequest login = RequestBuilder.post().setUri("http://www.sdmingjian.top/j_spring_security_check")
	                .addParameter("j_username", "admin").addParameter("j_password", "123").build();
	        // httpclient���ʵ�¼��ҳ,���õ���Ӧ����
	        CloseableHttpResponse response = httpClient.execute(login);
	        // ��Ӧ�ı�
	        String content = EntityUtils.toString(response.getEntity());
	        EntityUtils.consume(response.getEntity());
	        // �����Ӧҳ��Դ����
	        System.out.println(content);
	        // ���Ϊ302��Ҳ����˵��ҳ�������ض���
	        // �õ��ض�����
	        
	        HeaderIterator redirect = response.headerIterator("location");
	        while (redirect.hasNext()) {
	            // ʹ��get���󣬷��ʵ�½���ҳ��
	             getMethod = new HttpGet(redirect.next().toString());
	            CloseableHttpResponse response2 = httpClient.execute(getMethod);
	            // �õ������ı�
	            String content1 = EntityUtils.toString(response2.getEntity());
	            EntityUtils.consume(response2.getEntity());
	            // ��ӡ�����ı�
	            System.out.println("��Ӧ�����ı���:" + content1);
	        }
	     getMethod = new HttpGet("http://www.sdmingjian.top/admin/menuTreeNodeRedirect?id=78");
	        
			response=httpClient.execute(getMethod);
			System.out.println(EntityUtils.toString(response.getEntity()));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
