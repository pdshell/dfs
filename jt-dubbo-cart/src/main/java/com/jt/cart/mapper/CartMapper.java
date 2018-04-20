package com.jt.cart.mapper;

import com.jt.common.mapper.SysMapper;
import com.jt.dubbo.pojo.Cart;
//·Ç³£ºÃ
public interface CartMapper extends SysMapper<Cart>{

	void updateCartNum(Cart cart);
	

}
