package com.jt.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.SysResult;
import com.jt.dubbo.pojo.Cart;
import com.jt.dubbo.service.DubboCartService;
import com.jt.web.pojo.User;
import com.jt.web.util.UserThreadLocal;


@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private DubboCartService cartService;
	
	/**
	 * CartController是一个消费者.只需要调用接口即可
	 *
	 */
	@RequestMapping("/show")
	public String findCartById(Model model){
		
		User user = UserThreadLocal.get();
		//要求:对象必须序列化 因为是通过RPC调用
		List<Cart> cartList = cartService.findCartByUserId(user.getId());
		
		model.addAttribute("cartList", cartList);
		//返回的是购物车展现页面
		return "cart";
	}
	
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(@PathVariable Long itemId,@PathVariable Integer num){
		try {
			User user = UserThreadLocal.get();
			Cart cart = new Cart();
			cart.setUserId(user.getId());
			cart.setItemId(itemId);
			cart.setNum(num);

			
			cartService.updateCartNum(cart);
			
			return SysResult.oK();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201, "商品修改失败");
	}
	
	//cart/delete/1474391982.html

	@RequestMapping("/delete/{itemId}")
	public String deleteCart(@PathVariable Long itemId){
		
		User user = UserThreadLocal.get();
		Cart cart = new Cart();
		cart.setUserId(user.getId());
		cart.setItemId(itemId);
		
		cartService.deleteCart(cart);
		
		return "redirect:/cart/show.html";
	}
	
	//新增购物信息
	@RequestMapping("/add/{itemId}")
	public String saveCart(@PathVariable Long itemId,Cart cart){
		
		User user = UserThreadLocal.get();
		cart.setItemId(itemId);
		cart.setUserId(user.getId());
		cartService.saveCart(cart);
		return "redirect:/cart/show.html";
	}
	
	
	
	
	
}
