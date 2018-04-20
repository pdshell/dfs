package com.jt.cart.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.cart.mapper.CartMapper;
import com.jt.common.service.BaseService;
import com.jt.dubbo.pojo.Cart;
import com.jt.dubbo.service.DubboCartService;

public class CartServiceImpl extends BaseService<Cart> 
implements DubboCartService {
	
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartByUserId(Long id) {
		Cart cart = new Cart();
		cart.setUserId(id);
		
		return cartMapper.select(cart);
	}
	
	@Override
	public void updateCartNum(Cart cart) {
		
		//后台封装数据
		cart.setUpdated(new Date());
		
		cartMapper.updateCartNum(cart);
		
	}
	@Override
	public void deleteCart(Cart cart) {
		
		super.deleteByWhere(cart);
		
		
	}

	@Override
	public void saveCart(Cart cart) {
		
		Cart cartDB = new Cart();
		cartDB.setUserId(cart.getUserId());
		cartDB.setItemId(cart.getItemId());
		
		Cart tempCart = super.queryByWhere(cartDB);
		
		if(tempCart !=null){
			
			int countNum = tempCart.getNum() + cart.getNum();
			tempCart.setNum(countNum);
			tempCart.setUpdated(new Date());
			
			cartMapper.updateByPrimaryKey(tempCart);	
		}else{
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}
	}

	
	
	
	
}
