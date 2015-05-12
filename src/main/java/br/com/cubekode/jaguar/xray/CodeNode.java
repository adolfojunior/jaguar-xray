package br.com.cubekode.jaguar.xray;

import java.util.Map;

public class CodeNode extends CodeTrace {

	private CodeExecution execution;

	private int index;

	private int parentIndex;

	protected CodeNode(CodeExecution execution, CodeNode parent, int index, long timeStart, String info) {

		super(execution.getExecutionId(), timeStart, info);

		this.execution = execution;

		this.index = index;

		if (parent != null) {

			this.parentIndex = parent.getIndex();

			parent.nextNodeCount();
		}
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> m = super.toMap();
		m.put("index", getIndex());
		m.put("parentIndex", getParentIndex());
		return m;
	}

	@Override
	protected void end() {
		super.end();
		this.execution = null;
	}

	public int getIndex() {
		return index;
	}

	public int getParentIndex() {
		return parentIndex;
	}

	public void finish() {
		if (execution != null) {
			execution.popNode(this);
		} else {
			end();
		}
	}
	
	@Override
	public void finish(String error) {
		super.finish(error);
		if (execution != null) {
			execution.nextError();
		}
	}
}
