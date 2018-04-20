package com.jt.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Cart;
import com.jt.web.pojo.Order;
import com.jt.web.pojo.User;
import com.jt.web.service.CartService;
import com.jt.web.service.OrderService;
import com.jt.web.util.UserThreadLocal;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private CartService CartService;
	
	@Autowired
	private OrderService orderService;
	
	/**
	 * 1.需要跳转到订单的确认页面
	 * 2.准备购物车信息
	 * 		CartService 查询购物车信息
	 * 3.将信息会传到页面中 
	 * 4.将页面返回
	 * @return
	 */
	@RequestMapping("/create")
	public String toCreate(Model model){
		
		//配置拦截器的路径实现set赋值User对象
		Long userId = UserThreadLocal.get().getId();
		List<Cart> carts = CartService.findCartList(userId);
		model.addAttribute("carts", carts);
		
		return "order-cart";
	}
	
	
	/**
	 * 1.获取order的提交数据
	 * 2.通过threadLocal获取userId
	 * 3.通过服务端程序获取OrderId
	 * 4.判断OrderId是否有效
	 * 		非空校验 如果不为null 直接正确返回
	 * @param order
	 * @return
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult saveOrder(Order order){
		//获取用户Id
		Long userId = UserThreadLocal.get().getId();
		order.setUserId(userId);
		try {
			
			String orderId = orderService.saveOrder(order);
			if(!StringUtils.isEmpty(orderId)){		
				return SysResult.oK(orderId);	//正确返回
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SysResult.build(201, "订单新增失败");
	}
	
	
	/**
	 * 实现订单的查询 
	 * url:/success.html?id=71521189765462
	 * 查询数据应该是三张一起查询.
	 */
	@RequestMapping("/success")
	public String findOrderById(String id,Model model){
		
		Order order = orderService.findOrderById(id);
		
		model.addAttribute("order", order);
		return "success";
	}
	
	
	
	
	
	
	
	
	
	
}
