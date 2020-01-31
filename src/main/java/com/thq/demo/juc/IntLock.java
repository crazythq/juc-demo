package com.thq.demo.juc;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 关键字synchronized的功能扩展：重入锁
 * -- 中断响应
 */
public class IntLock implements Runnable {
	private static ReentrantLock lock1 = new ReentrantLock();
	private static ReentrantLock lock2 = new ReentrantLock();
	private int lock = 0;

	public IntLock(int lock){
		this.lock = lock;
	}

	@Override
	public void run() {
		try {
			if (lock == 1){
				lock1.lockInterruptibly();
				try {
					Thread.sleep(500);
				} catch (InterruptedException ignored) {
				}
				lock2.lockInterruptibly();
			} else {
				lock2.lockInterruptibly();
				try {
					Thread.sleep(500);
				} catch (InterruptedException ignored) {
				}
				lock1.lockInterruptibly();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (lock1.isHeldByCurrentThread()){
				lock1.unlock();
			}
			if (lock2.isHeldByCurrentThread()){
				lock2.unlock();
			}
			System.out.println(Thread.currentThread().getId() + ":线程退出");
		}
	}

	public static void main(String[] args) throws InterruptedException {
		IntLock intLock1 = new IntLock(1);
		IntLock intLock2 = new IntLock(2);
		Thread t1 = new Thread(intLock1);
		Thread t2 = new Thread(intLock2);

		t1.start();
		t2.start();

		Thread.sleep(1000);
		// 中断其中一个线程
		t2.interrupt();
	}
}
