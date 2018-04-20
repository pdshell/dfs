package com.jt.manage.factory;

import java.util.Calendar;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jt.manage.pojo.Item;
import com.sun.org.apache.xml.internal.resolver.Catalog;

public class TestFactory {
	
	//测试静态工厂
	@Test
	public void test01(){
		//创建spring容器
		ApplicationContext context = 
				new ClassPathXmlApplicationContext("/spring/applicationContext-factory.xml");
		System.out.println("容器创建成功");
		
		Calendar calendar = (Calendar) context.getBean("calendarA");
		
		System.out.println(calendar.getTime());
	}
	
	@Test
	public void test02(){
		//创建spring容器
		ApplicationContext context = 
				new ClassPathXmlApplicationContext("/spring/applicationContext-factory.xml");
		System.out.println("容器创建成功");
		
		Item item = (Item) context.getBean("itemA");
		
		System.out.println(item);
	}
	
	
	@Test
	public void test03(){
		//创建spring容器
		ApplicationContext context = 
				new ClassPathXmlApplicationContext("/spring/applicationContext-factory.xml");
		System.out.println("容器创建成功");
		Item item = (Item) context.getBean("item");
		System.out.println(item);
	}
}
