package com.thq.demo.juc;

import java.util.concurrent.*;

/**
 * 线程池超负载了怎么办：拒绝策略
 */
public class MyThreadPoolDemo {
	public static class MyTask implements Runnable {
		public String name;

		public MyTask(String name){
			this.name = name;
		}

		@Override
		public void run() {
			System.out.println("正在执行: Thread ID:" + Thread.currentThread().getId() + ", TaskName=" + name);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		ExecutorService es = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<>()){
			@Override
			protected void beforeExecute(Thread t, Runnable r) {
				System.out.println("准备执行：" + ((MyTask) r).name);
			}

			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				System.out.println("执行完毕：" + ((MyTask) r).name);
			}

			@Override
			protected void terminated() {
				System.out.println("线程池退出");
			}
		};
		for (int i = 0; i < 5; i++) {
			MyTask task = new MyTask("TASK-"+i);
			es.execute(task);
			try {
				Thread.sleep(10);
			} catch (InterruptedException ignored) {
			}
		}
		es.shutdown();
	}
}
