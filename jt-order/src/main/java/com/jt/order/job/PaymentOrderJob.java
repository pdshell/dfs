package com.jt.order.job;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.jt.order.mapper.OrderMapper;

public class PaymentOrderJob extends QuartzJobBean{
	
	/**
	 * 1.从上下文中获取Spring容器
	 * 2.从Spring容器中获取OrderMapper对象.
	 * 3.执行Mapper操作将超时的订单状态设置为6
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		//1.获取spring容器
		ApplicationContext applicationContext = 
		(ApplicationContext) 
		context.getJobDetail().getJobDataMap().get("applicationContext");
		
		//2.获取Mapper对象
		OrderMapper orderMapper = 
		applicationContext.getBean(OrderMapper.class);
		
		//3.设置超时订单状态为6    2天超时
		//import org.joda.time.DateTime; joda是时间操作工具包
		Date agoDate =  new DateTime().minusDays(2).toDate();
		orderMapper.updateState(agoDate);
		System.out.println("定时任务执行完成");
	}
}
