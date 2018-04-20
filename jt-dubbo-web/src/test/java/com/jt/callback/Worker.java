package com.jt.callback;

public class Worker {
	
	public void doWork(Callback callback){
		System.out.println("工人开始工作");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		callback.callBack("我做完了!!!");
	}
}
