package com.thq.demo.juc;

import java.util.concurrent.*;

/**
 * 线程池超负载了怎么办：拒绝策略
 */
public class RejectThreadPoolDemo {
	public static class MyTask implements Runnable {
		@Override
		public void run() {
			System.out.println(System.currentTimeMillis() + ": Thread ID:" + Thread.currentThread().getId());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		MyTask task = new MyTask();
		ExecutorService es = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<>(10),
				Executors.defaultThreadFactory(),
				new RejectedExecutionHandler() {
					@Override
					public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
						System.out.println(r.toString() + " is discard");
					}
				});
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			es.submit(task);
			try {
				Thread.sleep(10);
			} catch (InterruptedException ignored) {
			}
		}
		es.shutdown();
	}
}
