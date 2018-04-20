package com.jt.callback;

public class TestCallBack {
	
	public static void main(String[] args) {
		Boss boss = new Boss();
		Worker worker = new Worker();
		worker.doWork(boss);
	}
}
