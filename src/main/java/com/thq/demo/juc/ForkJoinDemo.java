package com.thq.demo.juc;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * 分而治之：Fork/Join框架
 */
public class ForkJoinDemo extends RecursiveTask<Long> {
	private static final int THRESHOLD = 10000;
	private long start;
	private long end;

	public ForkJoinDemo(long start, long end) {
		this.start = start;
		this.end = end;
	}

	public Long compute() {
		long sum = 0;
		boolean canCompute = (end - start) < THRESHOLD;
		if (canCompute) {
			for (long i = start; i <= end; i++) {
				sum += i;
			}
		} else {
			//分成N个子任务
			int N = 100;
			long step = (start + end) / N;
			ArrayList<ForkJoinDemo> subTasks = new ArrayList<>();
			long pos = start;
			for (int i = 0; i < N; i++) {
				long lastOne = pos + step;
				if (lastOne > end) {
					lastOne = end;
				}
				ForkJoinDemo subTask = new ForkJoinDemo(pos, lastOne);
				pos += step + 1;
				subTasks.add(subTask);
				subTask.fork();
			}
			for (ForkJoinDemo task : subTasks) {
				sum += task.join();
			}
		}
		return sum;
	}

	public static void main(String[] args) {
		ForkJoinPool pool = new ForkJoinPool();
		ForkJoinDemo task = new ForkJoinDemo(0, 200000L);
		ForkJoinTask<Long> result = pool.submit(task);
		try {
			long res = result.get();
			System.out.println("sum=" + res);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}
