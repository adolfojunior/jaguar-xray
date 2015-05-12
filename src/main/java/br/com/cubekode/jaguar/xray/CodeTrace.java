package br.com.cubekode.jaguar.xray;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class CodeTrace {

	private String executionId;
	private long timeStart;
	private long timeEnd;
	private String info;
	private String error;
	private int nodeCount;

	protected CodeTrace(String executionId, long timeStart, String info) {
		this.executionId = executionId;
		this.timeStart = timeStart;
		this.info = info;
	}

	public abstract void finish();

	public void finish(String error) {
		this.error = error;
		finish();
	}

	public Map<String, Object> toMap() {
		Map<String, Object> m = new LinkedHashMap<String, Object>();
		m.put("executionId", getExecutionId());
		m.put("timeStart", getTimeStart());
		m.put("timeEnd", getTimeEnd());
		m.put("duration", getDuration());
		m.put("nodeCount", getDuration());
		m.put("info", getInfo());
		m.put("error", getError());
		return m;
	}

	protected void end() {
		if (timeEnd == 0) {
			timeEnd = System.currentTimeMillis();
		}
	}

	public boolean isFinished() {
		return timeEnd > 0;
	}

	public String getExecutionId() {
		return executionId;
	}

	public long getTimeStart() {
		return timeStart;
	}

	public long getTimeEnd() {
		return timeEnd;
	}

	public long getDuration() {
		return timeEnd >= timeStart ? timeEnd - timeStart : -1;
	}

	public int getNodeCount() {
		return nodeCount;
	}

	public String getInfo() {
		return info;
	}

	public String getError() {
		return error;
	}

	protected int nextNodeCount() {
		return ++this.nodeCount;
	}

	protected void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	protected void setTimeStart(long timeStart) {
		this.timeStart = timeStart;
	}

	protected void setTimeEnd(long timeEnd) {
		this.timeEnd = timeEnd;
	}

	protected void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + toMap();
	}
}
