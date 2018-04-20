package com.jt.cart.service;

import java.util.List;

import com.jt.cart.pojo.Cart;

public interface CartService {

	List<Cart> findCartByUserId(Long userId);

	void updateCartNum(Cart cart);

	void saveCart(Cart cart);

	void deleteCart(Long userId, Long itemId);

}
