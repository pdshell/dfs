package com.jt.order.mapper;

import java.util.Date;

import com.jt.common.mapper.SysMapper;
import com.jt.order.pojo.Order;

public interface OrderMapper extends SysMapper<Order>{
	
	//将超时的订单 状态设置为6
	void updateState(Date agoDate);

}
