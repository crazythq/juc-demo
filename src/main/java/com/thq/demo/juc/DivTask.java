package com.thq.demo.juc;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程异常模拟类
 */
public class DivTask implements Runnable {
	private int a, b;

	public DivTask(int a, int b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public void run() {
		double result = a / b;
		System.out.println(result);
	}

	public static void main(String[] args) {
		ThreadPoolExecutor pool = new TraceThreadPolExecutor(0, Integer.MAX_VALUE,
				0L, TimeUnit.SECONDS,
				new SynchronousQueue<>());
		//错误堆栈中可以看到是在哪里提交的任务
		for (int i = -1; i < 5; i++) {
			pool.execute(new DivTask(100, i));
		}
	}
}
