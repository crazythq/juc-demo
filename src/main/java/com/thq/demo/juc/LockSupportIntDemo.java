package com.thq.demo.juc;

import java.util.concurrent.locks.LockSupport;

/**
 * 线程阻塞工具类：LockSupport
 */
public class LockSupportIntDemo {
	private static final Object u = new Object();
	private static ChangeObjectThread t1 = new ChangeObjectThread("t1");
	private static ChangeObjectThread t2 = new ChangeObjectThread("t2");

	public static class ChangeObjectThread extends Thread {
		public ChangeObjectThread(String name) {
			super.setName(name);
		}

		public void run() {
			synchronized (u) {
				System.out.println("in " + getName());
				LockSupport.park();
				if (Thread.interrupted()) {
					System.out.println(getName() + " 被中断了");
				}
			}
			System.out.println(getName() + " 执行结束");
		}
	}

	public static void main(String[] args) throws InterruptedException {
		t1.start();
		Thread.sleep(100);
		t2.start();
		t1.interrupt();
		LockSupport.unpark(t2);
	}
}
