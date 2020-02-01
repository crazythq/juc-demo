package com.thq.demo.juc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ThreadLocal垃圾回收示例
 */
public class ThreadLocalDemo_GC {
	private static int N = 100000;
	private static volatile ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected void finalize() {
			System.out.println(this.toString() + " is gc");
		}
	};
	private static volatile CountDownLatch cd = new CountDownLatch(N);

	public static class ParseDate implements Runnable {
		private int i;

		ParseDate(int i) {
			this.i = i;
		}

		@Override
		public void run() {
			if (tl.get() == null) {
				tl.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") {
					@Override
					protected void finalize() {
						System.out.println(this.toString() + " is gc");
					}
				});
				System.out.println(Thread.currentThread().getId() + ":create SimpleDateFormat");
			}
			try {
				tl.get().parse("2019-12-13 19:20:" + i % 60);
			} catch (ParseException e) {
				e.printStackTrace();
			} finally {
				cd.countDown();
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		ExecutorService es = Executors.newFixedThreadPool(10);
		for (int i = 0; i < N; i++) {
			es.execute(new ParseDate(i));
		}
		cd.await();
		System.out.println("mission complete!!");
		tl = null;
		System.gc();
		System.out.println("first GC complete!!");
		// 在设置ThreadLocal的时候，会清除ThreadLocalMap中的无效对象
		tl = new ThreadLocal<>();
		cd = new CountDownLatch(N);
		for (int i = 0; i < N; i++) {
			es.execute(new ParseDate(i));
		}
		cd.await();
		Thread.sleep(1000);
		System.gc();
		System.out.println("second GC complete!!");
		es.shutdown();
	}
}
