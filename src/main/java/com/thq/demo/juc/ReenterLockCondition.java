package com.thq.demo.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 重入锁的好搭档：Condition
 */
public class ReenterLockCondition implements Runnable {
	private static ReentrantLock lock = new ReentrantLock();
	private static Condition condition = lock.newCondition();

	@Override
	public void run() {
		try {
		lock.lock();
			condition.await();
			System.out.println("Thread is going on");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) {
		ReenterLockCondition rlc = new ReenterLockCondition();
		Thread t1 = new Thread(rlc);
		t1.start();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException ignored) {}
		// 通知线程t1继续执行
		lock.lock();
		condition.signal();
		lock.unlock();
	}
}
