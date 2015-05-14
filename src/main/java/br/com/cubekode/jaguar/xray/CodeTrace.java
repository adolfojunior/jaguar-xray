package br.com.cubekode.jaguar.xray;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class CodeTrace {

	private String executionId;
	private long timeStart;
	private long timeEnd;
	private int nodeCount;
	private String info;
	private boolean error;
	private String errorMessage;

	protected CodeTrace(String executionId, long timeStart, String info) {
		this.executionId = executionId;
		this.timeStart = timeStart;
		this.info = info;
	}

	public abstract void finish();

	public void finish(String error) {
		this.error = true;
		this.errorMessage = error;
		this.finish();
	}

	public Map<String, Object> toMap() {
		Map<String, Object> m = new LinkedHashMap<String, Object>();
		m.put("executionId", getExecutionId());
		m.put("timeStart", getTimeStart());
		m.put("timeEnd", getTimeEnd());
		m.put("duration", getDuration());
		m.put("nodeCount", getNodeCount());
		m.put("info", getInfo());
		m.put("error", isError());
		m.put("errorMessage", getErrorMessage());
		return m;
	}

	protected void endTime() {
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

	public boolean isError() {
		return error;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	protected int countNode() {
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
	
	protected void setError(boolean error) {
		this.error = error;
	}
	
	protected void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + toMap();
	}
}
