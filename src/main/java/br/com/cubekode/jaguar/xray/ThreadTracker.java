package br.com.cubekode.jaguar.xray;

import java.lang.reflect.Method;


public final class ThreadTracker {

	private static TrackProvider PROVIDER = new TrackProvider();

	private static ThreadLocal<CodeExecution> THREAD_EXEC = new ThreadLocal<CodeExecution>();

	public static TrackProvider getProvider() {
		return PROVIDER;
	}

	public static CodeTrace traceExecution(String info) {

		CodeExecution execution = peekExecution();

		if (execution == null || execution.isFinished()) {
			return createExecution(info);
		}
		return trace(info);
	}

	public static CodeExecution peekExecution() {
		return THREAD_EXEC.get();
	}

	public static CodeTrace createExecution(String info) {

		CodeExecution execution = getProvider().createExecution(info);

		THREAD_EXEC.set(execution);

		return execution;
	}

	public static CodeTrace trace(String info) {

		CodeExecution execution = peekExecution();

		if (execution != null) {
			if (!execution.isFinished()) {
				return execution.pushNode(info);
			} else {
				THREAD_EXEC.remove();
			}
		}
		return null;
	}

	public static CodeTrace trace(Method method) {
		
		StringBuilder info = new StringBuilder(256);
		
		info.append(method.getReturnType().getSimpleName());
		info.append(" ");
		info.append(method.getDeclaringClass().getName());
		info.append(".");
		info.append(method.getName());
		info.append("(");
		
		Class<?>[] params = method.getParameterTypes();
		for (int i = 0; i < params.length; i++) {
			if (i > 0) {
				info.append(",");
			}
			info.append(params[i].getSimpleName());
		}
		info.append(")");
		
		return trace(info.toString());
	}
}
