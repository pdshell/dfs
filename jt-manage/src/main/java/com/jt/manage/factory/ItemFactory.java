package com.jt.manage.factory;

import org.springframework.beans.factory.FactoryBean;

import com.jt.manage.pojo.Item;


public class ItemFactory implements FactoryBean<Item>{

	@Override
	public Item getObject() throws Exception {
		
		System.out.println("我是spring工厂开始实例化对象");
		return new Item();
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return Item.class;
	}

	//是否为单例对象
	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}
}













