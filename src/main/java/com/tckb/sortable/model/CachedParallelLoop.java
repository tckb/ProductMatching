package com.tckb.sortable.model;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created on 22/11/16.
 *
 * @author tckb
 */
public final class CachedParallelLoop {
	private static final int NR_CORES = Runtime.getRuntime().availableProcessors();
	private final ExecutorService executor;

	public CachedParallelLoop() {
		executor = Executors.newScheduledThreadPool(2 * NR_CORES);
	}

	public static void main(String[] argv) {
		new CachedParallelLoop().withIndex(0, 9, i -> System.out.println(i * 10));
	}

	public void withIndex(int start, int stop, final Each body) {
		int chunksize = (stop - start + NR_CORES - 1) / NR_CORES;
		int loops = (stop - start + chunksize - 1) / chunksize;
		final CountDownLatch latch = new CountDownLatch(loops);
		for (int i = start; i < stop; ) {
			final int lo = i;
			i += chunksize;
			final int hi = (i < stop) ? i : stop;
			executor.submit(() -> {
				for (int i1 = lo; i1 < hi; i1++) { body.run(i1); }
				latch.countDown();
			});
		}
		try {
			latch.await();
		} catch (InterruptedException ignored) {}
	}

	public void finish() {
		executor.shutdown();
	}

	public interface Each {
		void run(int i);
	}
}
