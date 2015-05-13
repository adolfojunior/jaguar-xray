package br.com.cubekode.jaguar.xray;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CodeExecution extends CodeTrace {

	private TrackProvider provider;

	private List<CodeNode> nodes;

	private Deque<CodeNode> stack;

	private int errorCount;

	protected CodeExecution(TrackProvider provider, String executionId, long timeStart, String info) {

		super(executionId, timeStart, info);

		this.provider = provider;

		this.nodes = new LinkedList<CodeNode>();
		this.stack = new LinkedList<CodeNode>();
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> m = super.toMap();
		m.put("errorCount", getErrorCount());
		return m;
	}

	public CodeNode pushNode(String info) {

		CodeNode node = provider.createNode(this, stack.peek(), info);

		nodes.add(node);
		stack.push(node);

		return node;
	}

	public CodeNode popNode(CodeNode node) {
		while (!stack.isEmpty()) {

			CodeNode poped = stack.pop();
			poped.endTime();

			if (poped == node) {
				return poped;
			}
		}
		return null;
	}

	public int getErrorCount() {
		return errorCount;
	}

	public List<CodeNode> getNodes() {
		return nodes;
	}

	public void finish() {
		endTime();
	}

	protected long countError() {
		return ++errorCount;
	}

	@Override
	protected void endTime() {
		super.endTime();
		if (stack != null) {
			popNode(null);
			stack = null;
		}
		if (provider != null) {
			provider.finish(this);
			provider = null;
		}
	}
}
