package com.jt.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.SysResult;
import com.jt.dubbo.pojo.Cart;
import com.jt.dubbo.pojo.Order;
import com.jt.dubbo.service.DubboCartService;
import com.jt.dubbo.service.DubboOrderService;
import com.jt.web.util.UserThreadLocal;


@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private DubboOrderService orderService;
	
	@Autowired
	private DubboCartService cartService;
	
	
	//order/create.html
	@RequestMapping("/create")
	public String create(Model model){
		
		Long userId = UserThreadLocal.get().getId();
		//查询用户购物车信息  ${carts}
		List<Cart> carts = cartService.findCartByUserId(userId);
		
		model.addAttribute("carts", carts);
		//转到订单确认页面
		return "order-cart";
	}
	
	//订单提交  http://www.jt.com/service/order/submit
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult saveOrder(Order order){
		Long userId = UserThreadLocal.get().getId();
	
		order.setUserId(userId);
		try {
			
			//在消费者配置文件当中进入接口文件
			String orderId = orderService.saveOrder(order);	
			if(!StringUtils.isEmpty(orderId)){
				
				return SysResult.oK(orderId);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return SysResult.build(201, "新增订单失败");
	}
	
	
	//跳转到成功页面	http://www.jt.com/order/success.html?id=71521120079239
	@RequestMapping("/success")
	public String findOrderById(String id,Model model){
		
		Order order = orderService.findOrderById(id);
		model.addAttribute("order", order);
		return "success";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
