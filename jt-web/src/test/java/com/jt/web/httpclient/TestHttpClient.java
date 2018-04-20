package com.jt.web.httpclient;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class TestHttpClient {
	
	//模拟get请求
	@Test
	public void testGet() throws ClientProtocolException, IOException{
		
		//1.创建httpCLient对象
		CloseableHttpClient httpClient 
		 = HttpClients.createDefault();
		
		//2.定义uri
		String uri = "https://item.jd.com/1607218.html";
		
		//3.定义请求方式
		HttpGet httpGet = new HttpGet(uri);

		//4.发出请求
		CloseableHttpResponse response
		 			= httpClient.execute(httpGet);
		
		//判断请求是否正确
		if(response.getStatusLine().getStatusCode() == 200){
			//获取请求内容
			String result = 
					EntityUtils.toString(response.getEntity())  ;
			System.out.println("打印实体信息"+result);
		}
	}
	
	@Test
	public void testPost() throws ClientProtocolException, IOException{
		
		//获取httpclient对象
		CloseableHttpClient client = 
				HttpClients.createDefault();
		
		//定义url
		String url = "http://www.tmooc.cn/web/index_new.html?tedu";
		
		//定义Post请求方式
		HttpPost httpPost = new HttpPost(url);
		
		//Entity中需要设定post中提交的参数
		//UrlEncodedFormEntity entity =  new UrlEncodedFormEntity(parameters)
		//httpPost.setEntity(entity);
		
		CloseableHttpResponse httpResponse 
		 =	client.execute(httpPost);
		
		//判断数据是否正确
		if(httpResponse.getStatusLine().getStatusCode() == 200){
			
			String msg = EntityUtils.toString(httpResponse.getEntity());
			System.out.println(msg);
		}
		
	}
	
	
	
	
	
	
	

}
