package com.jt.cart.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.cart.pojo.Cart;
import com.jt.cart.service.CartService;
import com.jt.common.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	//http://cart.jt.com/cart/query/{userId}
	//根据用户id查询购物车信息
	/**
	 * 根据userId返回List<Cart>
	 * @param userId
	 * @return
	 * 
	 *java对象远程传输问题.
	 *	1.RPC(远程传输的协议)
	 *  2.JSON串的形式
	 */
	@RequestMapping("/query/{userId}")
	@ResponseBody
	public SysResult findCartByUserId(@PathVariable Long userId){
		try {
			List<Cart> carts = cartService.findCartByUserId(userId);
			
			//将List集合转化为JSON串
			return SysResult.oK(carts);
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "查询失败");
		}	
	}
	
	
	//http://cart.jt.com/cart/update/num/{userId}/{itemId}/{num}
	/**
	 * @param userId
	 * @param itemId
	 * @param num
	 * @return
	 */
	@RequestMapping("/update/num/{userId}/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCart(
			@PathVariable Long userId,
			@PathVariable Long itemId,
			@PathVariable Integer num){
		 try {
			 Cart cart = new Cart();
			 cart.setUserId(userId);
			 cart.setItemId(itemId);
			 cart.setNum(num);
			 cart.setUpdated(new Date());
			 cartService.updateCartNum(cart); //修改商品数量
			 return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "修改商品数量失败");
		}
	}
	
	
	//购物车新增  http://cart.jt.com/cart/save
	@RequestMapping("/save")
	@ResponseBody
	public SysResult saveCart(Cart cart){
		try {
			cartService.saveCart(cart);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "购物车新增失败");
		}
	}
	
	//购物车的删除
	@RequestMapping("/delete/{userId}/{itemId}")
	@ResponseBody
	public SysResult deleteCart(
			@PathVariable Long userId,
			@PathVariable Long itemId){
		try {
			cartService.deleteCart(userId,itemId);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "删除购物车失败");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
