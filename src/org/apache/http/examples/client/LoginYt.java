package org.apache.http.examples.client;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class LoginYt {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = null;
		CloseableHttpResponse response =null;
		try {
			httpget=new HttpGet("http://www.sdmingjian.top/");
			httpclient.execute(httpget);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpUriRequest login = org.apache.http.client.methods.RequestBuilder
				.post("http://www.sdmingjian.top/j_spring_security_check")
				.addParameter("j_username", "admin")
				.addParameter("j_password", "123").build();
		try {
			 response = httpclient.execute(login);
			String location = response.getFirstHeader("Location").getValue();
			if ("http://www.sdmingjian.top/admin/index".equals(location)) {
				System.out.println("µÇÂ¼³É¹¦");
			} else {
				System.out.println("µÇÂ¼Ê§°Ü");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			httpget=new HttpGet("http://www.sdmingjian.top/admin/menuTreeNodeRedirect?id=78");
			response=httpclient.execute(httpget);
			String content=EntityUtils.toString(response.getEntity());
			if (content.contains("res/js/mingjian/dmzl/DmZl.js")){
				System.out.println("µã»÷²Ù×÷³É¹¦");
			}else{
				System.out.println("³´×÷Ê§°Ü");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}
		

	}

}
