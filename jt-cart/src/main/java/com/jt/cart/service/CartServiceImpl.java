package com.jt.cart.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.cart.mapper.CartMapper;
import com.jt.cart.pojo.Cart;
import com.jt.common.service.BaseService;

@Service
public class CartServiceImpl extends BaseService<Cart> implements CartService {
	
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartByUserId(Long userId) {
		Cart cart = new Cart();
		cart.setUserId(userId);
		
		//通过通用Mapper查询数据
		return cartMapper.select(cart);
	}

	@Override
	public void updateCartNum(Cart cart) {
		
		cartMapper.updateCartNum(cart);
		
	}
	
	/**
	 * 新增购物车思路
	 * 1.首先根据 itemId和UserId查询购物车信息.
	 * 2.如果查询的结果不为null.表示之前已经添加了购物信息
	 *   应该将前后的商品数量求和,之后做更新操作
	 * 3.如果查询的结果为null.则进行入库操作
	 */
	/* (non-Javadoc)
	 * @see com.jt.cart.service.CartService#saveCart(com.jt.cart.pojo.Cart)
	 */
	@Override
	public void saveCart(Cart cart) {
		
		Cart cartDB = new Cart();
		cartDB.setUserId(cart.getUserId());
		cartDB.setItemId(cart.getItemId());
		
		//如果调用的是父级方法 需要加super进行标识
		Cart tempCart = super.queryByWhere(cartDB);
		
		if(tempCart !=null){
			int countNum = tempCart.getNum() + cart.getNum(); //数量求和
			tempCart.setNum(countNum);
			tempCart.setUpdated(new Date());
			//实现更新操作
			cartMapper.updateByPrimaryKeySelective(tempCart);
		}else{
			//表示购物车信息中没有改数据 则进行入库操作
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}
	}

	@Override
	public void deleteCart(Long userId, Long itemId) {
		Cart cart = new Cart();
		cart.setUserId(userId);
		cart.setItemId(itemId);
		cartMapper.delete(cart);
	}
	
	
	
	
	
}
