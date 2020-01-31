package com.thq.demo.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 关键字synchronized的功能扩展：重入锁
 * -- 锁申请等待限时
 */
public class TimeLock implements Runnable {
	private static ReentrantLock lock = new ReentrantLock();

	@Override
	public void run() {
		try {
			if(lock.tryLock(3, TimeUnit.SECONDS)){
				Thread.sleep(3000);
			} else {
				System.out.println("get lock failed");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if(lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		TimeLock rl = new TimeLock();
		Thread t1 = new Thread(rl);
		Thread t2 = new Thread(rl);
		Thread t3 = new Thread(rl);

		t1.start();
		t2.start();
		t3.start();

	}
}
