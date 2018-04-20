package com.jt.dubbo.service;

import java.util.List;

import com.jt.dubbo.pojo.Cart;

public interface DubboCartService {

	List<Cart> findCartByUserId(Long id);

	void updateCartNum(Cart cart);

	void deleteCart(Cart cart);

	void saveCart(Cart cart);
	
}
