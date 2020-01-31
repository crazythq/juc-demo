package com.thq.demo.juc;

import java.util.concurrent.*;

/**
 * 可以追踪异常堆栈信息的线程池
 */
public class TraceThreadPolExecutor extends ThreadPoolExecutor {
	public TraceThreadPolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override
	public void execute(Runnable task) {
		super.execute(wrap(task, clientTrace(), Thread.currentThread().getName()));
	}

	@Override
	public Future<?> submit(Runnable task) {
		return super.submit(wrap(task, clientTrace(), Thread.currentThread().getName()));
	}

	private Exception clientTrace() {
		return new Exception("Client stack trace");
	}

	private Runnable wrap(final Runnable task, final Exception clientStack, String clientThreadName) {
		return () -> {
			try {
				task.run();
			} catch (Exception e) {
				clientStack.printStackTrace();
				throw e;
			}
		};
	}

}
