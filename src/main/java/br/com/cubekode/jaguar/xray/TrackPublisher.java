package br.com.cubekode.jaguar.xray;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;

public class TrackPublisher {

	private static final TrackPublisher INSTANCE = new TrackPublisher();

	public static TrackPublisher getInstance() {
		return INSTANCE;
	}

	/**
	 * Non blocking Queue
	 */
	private Queue<CodeExecution> storeQueue = new ConcurrentLinkedQueue<CodeExecution>();

	public TrackPublisher() {
		Executors.newSingleThreadExecutor().submit(new Runnable() {
			@Override
			public void run() {
				try {
					consume();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void add(CodeExecution execution) {
		if (execution != null) {
			storeQueue.offer(execution);
		}
	}

	protected void store(CodeExecution execution) {
		TrackStore.getInstance().add(execution);
	}

	private void consume() throws InterruptedException {
		while (true) {

			CodeExecution execution = storeQueue.poll();

			if (execution != null) {
				store(execution);
			} else {
				Thread.sleep(600);
			}
		}
	}
}
