package com.jt.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Cart;
import com.jt.web.service.CartService;
import com.jt.web.util.UserThreadLocal;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	/**
	 * 实现购物车页面的跳转
	 * 1.拦截url
	 * 2.准备转向数据.List<Carts>
	 * 3.转向购物车页面
	 */
	@RequestMapping("/show")
	public String toCart(Model model){
		
		Long userId = UserThreadLocal.get().getId();
		List<Cart> cartList = cartService.findCartList(userId);
		//将数据回显到页面中
		model.addAttribute("cartList", cartList);
		//跳转页面
		return "cart";	//转发
	}
	
	//修改商品数量
	//url http://www.jt.com/service/cart/update/num/1474391982/11
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(
			@PathVariable Long itemId,
			@PathVariable Integer num){
		Long userId = UserThreadLocal.get().getId();
		Cart cart = new Cart();
		cart.setUserId(userId);
		cart.setItemId(itemId);
		cart.setNum(num);
		try {
			cartService.updateCartNum(cart);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201,"修改失败");
		}
	}
	
	//新增购物车信息   /cart/add/1474392001.html
	@RequestMapping("/add/{itemId}")
	public String saveCart(@PathVariable Long itemId,Cart cart){
		
		Long userId = UserThreadLocal.get().getId();
		
		cart.setItemId(itemId);
		cart.setUserId(userId);
		cartService.saveCart(cart);
		//跳转到购物车展现页面
		return "redirect:/cart/show.html";
	}
	
	
	//实现购物车删除/cart/delete/1474391982.html
	@RequestMapping("//delete/{itemId}")
	public String deleteCart(@PathVariable Long itemId){
		
		Long userId = UserThreadLocal.get().getId();
		cartService.deleteCart(userId,itemId);
		//进行页面转向
		return "redirect:/cart/show.html";
	}
	
	
	
	

}
