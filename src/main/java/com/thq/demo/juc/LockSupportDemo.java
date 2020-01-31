package com.thq.demo.juc;

import java.util.concurrent.locks.LockSupport;

/**
 * 线程阻塞工具类：LockSupport
 */
public class LockSupportDemo {
	private static final Object u = new Object();
	private static ChangeObjectThread t1 = new ChangeObjectThread("t1");
	private static ChangeObjectThread t2 = new ChangeObjectThread("t2");

	public static class ChangeObjectThread extends Thread {
		public ChangeObjectThread(String name){
			super.setName(name);
		}

		public void run(){
			synchronized (u) {
				System.out.println("in "+getName());
				LockSupport.park();
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		t1.start();
		Thread.sleep(100);
		t2.start();
		LockSupport.unpark(t1);
		LockSupport.unpark(t2);
		t1.join();
		t2.join();
	}
}
