package com.thq.demo.juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 允许多个线程同时访问：信号量（Semaphore）
 */
public class SemapDemo implements Runnable {
	private static Semaphore semp = new Semaphore(5);

	@Override
	public void run() {
		try {
			semp.acquire();
			Thread.sleep(2000);
			System.out.println(Thread.currentThread().getId() + ": done!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semp.release();
		}
	}

	public static void main(String[] args) {
		ExecutorService exec = Executors.newFixedThreadPool(20);
		try {
			final SemapDemo demo = new SemapDemo();
			for (int i = 0; i < 20; i++) {
				exec.submit(demo);
			}
		} finally {
			exec.shutdown();
		}
	}
}
