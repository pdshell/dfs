package com.jt.common.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HttpClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientService.class);

    @Autowired(required=false)
    private CloseableHttpClient httpClient;

    @Autowired(required=false)
    private RequestConfig requestConfig;

    
    /**
     * 说明:
     * 	编辑工具类时需要处理2中类型的请求 get post
     * 参数介绍:
     *  addUser?id:1&name=tom&age=18
     * 	定义url  确定访问的路径
     *  定义参数集合 Map<String,String>.指定参数的类型都是String
     *  定义字符集 encode=utf-8 
     *  
     *  方法介绍
     *  根据不同的用户需求,重载多个方法
     */
    
    
    /**
     * 编辑思路:
     * Url:findItem?id=1&name=tom
     * 1.判断是否包含参数,如果包含参数应该将参数进行动态的拼接
     * 2.判断是否指定字符集编码  如果没有指定则设置默认值UTF-8
     * 3.通过httpClient对象发起http请求
     * 4.判断返回值是否有效
     * 5.将结果返回
     * @param url
     * @param params
     * @param charset
     * @return
     * @throws URISyntaxException 
     */
    public String doGet(String uri,Map<String, String> params,String charset) throws URISyntaxException{
    	//1.判断是否含有参数 Url:findItem?id=1&name=tom
    	URIBuilder builder = new URIBuilder(uri);
    	if(params !=null){
    		//整理get提交参数
    		for (Map.Entry<String, String> param :params.entrySet()) {
    			builder.addParameter(param.getKey(), param.getValue());
			}
    		//Uri:findItem?id=1&name=tom&age=18
    		System.out.println("编辑uri结果:!!!!"+builder.toString());
    		uri = builder.toString();	
    	}
    	
    	//判断字符集编码
    	if(StringUtils.isEmpty(charset)){
    		
    		charset = "UTF-8";
    	}
    	
    	//定义Get请求对象
    	HttpGet httpGet = new HttpGet(uri);
    	httpGet.setConfig(requestConfig);
    	
    	//发送请求
    	try {
    		CloseableHttpResponse httpResponse 
    			= httpClient.execute(httpGet);
    		//判断请求是否正确
    		if(httpResponse.getStatusLine().getStatusCode() == 200){
    			//result是远程返回的JSON数据
    			String result = EntityUtils.toString(httpResponse.getEntity(),charset);
    			return result;	
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}

    	return null;
    }
    
    public String doGet(String uri) throws URISyntaxException{
    	
    	return doGet(uri, null, null);
    	
    }
    
    public String doGet(String uri,Map<String, String> params) throws URISyntaxException{
    	
    	return doGet(uri, params, null);
    }
    
    
    /*
     * 1.doPost请求方式和doget类似
     * 2.doPost中的参数传递借助form表单.
     * 
     * 编码步骤:
     * 	1.定义请求的对象 httpPost()
     *  2.判断是否含有参数,如果含有参数需要表单的赋值
     *  3.将form表单的参数赋值给post请求
     * 
     */
    public String doPost(String uri,Map<String, String> params,String charset) throws UnsupportedEncodingException{
    	
    	//定义post提交方式
    	HttpPost httpPost = new HttpPost(uri);
    	if(StringUtils.isEmpty(charset)){
    		 
    		charset = "UTF-8";
    	}
    	//判断参数是否为空
    	if(params !=null){
    		//定义参数提交的集合
    		List<NameValuePair> parameters = 
    				new ArrayList<NameValuePair>();
    		
    		//为参数赋值
    		for (Map.Entry<String, String> param : params.entrySet()) {
    			
    			BasicNameValuePair nameValuePair =
    					new BasicNameValuePair(param.getKey(), param.getValue());
    			parameters.add(nameValuePair);
			}
    		
    		UrlEncodedFormEntity entity = 
    				new UrlEncodedFormEntity(parameters, charset);
    		//为post请求赋值
    		httpPost.setEntity(entity);
    	}
    	try {
			CloseableHttpResponse response = 
					httpClient.execute(httpPost);
			
			//判断返回值是否正确
			if(response.getStatusLine().getStatusCode() == 200){
				
				String result = EntityUtils.toString(response.getEntity(),charset);
				return result;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
    	return null;
    }
    public String doPost(String uri) throws UnsupportedEncodingException{
    	 
    	return doPost(uri,null,null);
    }
    
    
    public String doPost(String uri,Map<String, String> params) throws UnsupportedEncodingException{
   	 
    	return doPost(uri,params,null);
    }

    
    
}
