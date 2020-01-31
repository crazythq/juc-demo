package com.thq.demo.juc;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 关键字synchronized的功能扩展：重入锁
 */
public class ReenterLock implements Runnable {
	private static ReentrantLock lock = new ReentrantLock();
	private static int i = 0;

	@Override
	public void run() {
		for (int j = 0; j < 1000000; j++) {
			lock.lock();
			lock.lock();
			try {
				i++;
			} finally {
				lock.unlock();
				lock.unlock();
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		ReenterLock rl = new ReenterLock();
		Thread t1 = new Thread(rl);
		Thread t2 = new Thread(rl);
		Thread t3 = new Thread(rl);

		t1.start();
		t2.start();
		t3.start();

		t1.join();
		t2.join();
		t3.join();

		System.out.println(i);
	}
}
