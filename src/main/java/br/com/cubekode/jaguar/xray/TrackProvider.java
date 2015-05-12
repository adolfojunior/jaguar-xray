package br.com.cubekode.jaguar.xray;

import java.util.concurrent.atomic.AtomicLong;

public class TrackProvider {

	private AtomicLong count;

	protected TrackProvider() {
		this.count = new AtomicLong(1L);
	}

	protected CodeExecution createExecution(String info) {

		long timestamp = System.currentTimeMillis();

		return new CodeExecution(this, createExecutionId(timestamp), timestamp, info);
	}

	protected CodeNode createNode(CodeExecution execution, CodeNode parent, String info) {

		long timestamp = System.currentTimeMillis();

		return new CodeNode(execution, parent, execution.nextNodeCount(), timestamp, info);
	}

	protected String createExecutionId(long timestamp) {
		return count.incrementAndGet() + "_" + timestamp;
	}

	protected void finish(CodeExecution execution) {
		TrackPublisher.getInstance().add(execution);
	}
}
