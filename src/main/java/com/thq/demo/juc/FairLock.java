package com.thq.demo.juc;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 关键字synchronized的功能扩展：重入锁
 * -- 锁申请等待限时
 */
public class FairLock implements Runnable {
	private static ReentrantLock fairLock = new ReentrantLock(true);

	@Override
	public void run() {
		while (true) {
			try {
				fairLock.lock();
				System.out.println(Thread.currentThread().getName() + " 获得锁");
				// try {
				// 	Thread.sleep(500);
				// } catch (InterruptedException e) {}
			} finally {
				fairLock.unlock();
			}
		}
	}

	public static void main(String[] args) {
		FairLock fairLock = new FairLock();
		Thread t1 = new Thread(fairLock, "t1");
		Thread t2 = new Thread(fairLock, "t2");

		t1.start();
		t2.start();
	}
}
