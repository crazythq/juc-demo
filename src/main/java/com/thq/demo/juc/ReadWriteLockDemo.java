package com.thq.demo.juc;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁：ReadWriteLock
 */
public class ReadWriteLockDemo {
	private static Lock lock = new ReentrantLock();
	private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private static Lock readLock = readWriteLock.readLock();
	private static Lock writeLock = readWriteLock.writeLock();
	private int value;

	public Object handleRead(Lock lock) throws InterruptedException {
		try{
			lock.lock();
			// 模拟读操作
			// 读操作的耗时越多，读写锁的优势就越明显
			Thread.sleep(1000);
			return value;
		} finally {
			lock.unlock();
		}
	}

	public void handleWrite(Lock lock, int v) throws InterruptedException {
		try{
			lock.lock();
			// 模拟写操作
			Thread.sleep(1000);
			value = v;
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) {
		final ReadWriteLockDemo demo = new ReadWriteLockDemo();
		Runnable readRunnable = () -> {
			try {
				Object value = demo.handleRead(readLock);
				// Object value = demo.handleRead(lock);
				System.out.println(Thread.currentThread().getId() + ": value=" + value);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		Runnable writeRunnable = () -> {
			try {
				demo.handleWrite(writeLock, new Random().nextInt(100) + 1);
				// demo.handleWrite(lock, new Random().nextInt(100) + 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};

		for (int i = 0; i < 18; i++) {
			new Thread(writeRunnable).start();
		}
		for (int i = 0; i < 18; i++) {
			new Thread(readRunnable).start();
		}
	}
}
