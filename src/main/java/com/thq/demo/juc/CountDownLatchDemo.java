package com.thq.demo.juc;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 倒计数器：CountDownLatch
 */
public class CountDownLatchDemo implements Runnable {
	private static int N = 10;
	private static final CountDownLatch end = new CountDownLatch(N);
	private static final CountDownLatchDemo demo = new CountDownLatchDemo();

	@Override
	public void run() {
		try {
			//模拟检查任务
			Thread.sleep(new Random().nextInt(10) * 1000);
			System.out.println("check complete");
			end.countDown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ExecutorService exec = Executors.newFixedThreadPool(N);
		try {
			for (int i = 0; i < N; i++) {
				exec.submit(demo);
			}
			//等待检查
			end.await();
			//发射火箭
			System.out.println("Fire!");
		} catch (InterruptedException e){
			e.printStackTrace();
		} finally {
			exec.shutdown();
		}
	}
}
