package com.jt.callback;

public class Boss implements Callback{

	@Override
	public void callBack(String msg) {
		
		System.out.println("获取工人回馈信息:"+msg);
		System.out.println("好员工给你加薪");
		
	}

}
