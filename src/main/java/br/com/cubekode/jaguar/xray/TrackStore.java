package br.com.cubekode.jaguar.xray;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class LRUTrackExecution extends LinkedHashMap<String, CodeExecution> {

	private static final long serialVersionUID = 1L;

	private int maxSize;

	public LRUTrackExecution(int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<String, CodeExecution> eldest) {
		return size() > maxSize;
	}
}

public class TrackStore {

	private static final int MAX_LRU = 256;

	private static final TrackStore INSTANCE = new TrackStore();

	public static TrackStore getInstance() {
		return INSTANCE;
	}

	private Map<String, CodeExecution> map = Collections.synchronizedMap(new LRUTrackExecution(MAX_LRU));

	public void add(CodeExecution execution) {
		map.put(execution.getExecutionId(), execution);
	}

	public List<Map<String, Object>> all() {
		return convert(map.values());
	}

	public Map<String, Object> get(String id) {
		if (id == null || id.isEmpty()) {
			return null;
		}
		return map.get(id).toMap();
	}

	public List<Map<String, Object>> nodes(String id) {
		if (id == null || id.isEmpty()) {
			return Collections.emptyList();
		}
		return convert(map.get(id).getNodes());
	}

	public void clear() {
		map.clear();
	}

	private List<Map<String, Object>> convert(Collection<? extends CodeTrace> traces) {
		List<Map<String, Object>> all = new LinkedList<Map<String, Object>>();
		for (CodeTrace v : traces) {
			all.add(v.toMap());
		}
		return all;
	}
}
