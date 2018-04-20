package com.jt.web.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Cart;
import com.jt.web.pojo.User;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private HttpClientService httpClient;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	
	/**
	 * 1.定位uri
	 * 2.选择合适的数据的传输方式 map restFul
	 * 3.进行数据的访问.得到响应结果
	 * 4.将结果进行数据解析
	 * 5.最终返回list集合
	 */
	
	@Override
	public List<Cart> findCartList(Long userId) {
		
		String uri = "http://cart.jt.com/cart/query/"+userId;
		try {
			String resultJSON = httpClient.doGet(uri);
			SysResult result =
					objectMapper.readValue(resultJSON, SysResult.class);
			//判断服务端的返回值是否正确,如果正确获取返回值,如果不正确返回null
			if(result.getStatus() == 200){
				//正确  将数据强转为List集合
				/*String cartListJSON = (String) result.getData();
				Cart[] carts = 
				objectMapper.readValue(cartListJSON,Cart[].class);
				
				List<Cart> cartList = Arrays.asList(carts);*/
				
				List<Cart> carts = (List<Cart>) result.getData(); 
				//获取的是数组类型的JSON [{},{},{}]
				return carts;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 1.定位url
	 * 2.参数的封装
	 * 3.发出请求获取数据
	 * 4.解析数据
	 * http://cart.jt.com/cart/update/num/{userId}/{itemId}/{num}
	 */
	@Override
	public void updateCartNum(Cart cart) {
		String uri = "http://cart.jt.com/cart/update/num/" + cart.getUserId()
		+ "/" + cart.getItemId() + "/" + cart.getNum();
		try {
			httpClient.doGet(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 1.定义uri
	 * 2.封装数据
	 * 3.发送请求
	 */
	@Override
	public void saveCart(Cart cart) {
		String uri = "http://cart.jt.com/cart/save";
		//封装数据
		Map<String, String> map = new HashMap<String,String>();
		map.put("userId",cart.getUserId()+"");
		map.put("itemId",cart.getItemId()+"");
		map.put("itemTitle",cart.getItemTitle());
		map.put("itemImage",cart.getItemImage());
		map.put("itemPrice",cart.getItemPrice()+"");
		map.put("num",cart.getNum()+"");
		
		try {
			httpClient.doPost(uri,map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteCart(Long userId, Long itemId) {
		
		String uri = "http://cart.jt.com/cart/delete/" + userId + "/" + itemId;
		try {
			httpClient.doGet(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	
	
	
	
}
