package com.jt.cart.mapper;

import com.jt.common.mapper.SysMapper;
import com.jt.dubbo.pojo.Cart;
//�ǳ���
public interface CartMapper extends SysMapper<Cart>{

	void updateCartNum(Cart cart);
	

}
