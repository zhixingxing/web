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
			// 先访问首页，得到cookie 
			// cookie信息自动保存在HttpClient中 
		
			//登陆智慧城市 
			HttpGet getMethod = new HttpGet("http://www.sdmingjian.top/");
    
				httpClient.execute(getMethod);
	        // 携带cookie访问登录网面
	        // 设置登录的账号与密码
	        HttpUriRequest login = RequestBuilder.post().setUri("http://www.sdmingjian.top/j_spring_security_check")
	                .addParameter("j_username", "admin").addParameter("j_password", "123").build();
	        // httpclient访问登录网页,并得到响应对象
	        CloseableHttpResponse response = httpClient.execute(login);
	        // 响应文本
	        String content = EntityUtils.toString(response.getEntity());
	        EntityUtils.consume(response.getEntity());
	        // 输出响应页面源代码
	        System.out.println(content);
	        // 输出为302，也就是说网页发生了重定向
	        // 得到重定向后的
	        
	        HeaderIterator redirect = response.headerIterator("location");
	        while (redirect.hasNext()) {
	            // 使用get请求，访问登陆后的页面
	             getMethod = new HttpGet(redirect.next().toString());
	            CloseableHttpResponse response2 = httpClient.execute(getMethod);
	            // 得到返回文本
	            String content1 = EntityUtils.toString(response2.getEntity());
	            EntityUtils.consume(response2.getEntity());
	            // 打印请求文本
	            System.out.println("响应请求文本是:" + content1);
	        }
	     getMethod = new HttpGet("http://www.sdmingjian.top/admin/menuTreeNodeRedirect?id=78");
	        
			response=httpClient.execute(getMethod);
			System.out.println(EntityUtils.toString(response.getEntity()));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
