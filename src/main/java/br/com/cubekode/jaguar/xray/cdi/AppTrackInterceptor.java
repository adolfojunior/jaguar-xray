package br.com.cubekode.jaguar.xray.cdi;

import java.io.Serializable;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import br.com.cubekode.jaguar.xray.CodeTrace;
import br.com.cubekode.jaguar.xray.ThreadTracker;

@XRayTrace
@Interceptor
public class AppTrackInterceptor implements Serializable {

	private static final long serialVersionUID = 1L;

	@AroundInvoke
	public Object intercept(InvocationContext ic) throws Exception {
		
		if (ThreadTracker.isTracking()) {
			return track(ic);
		}
		return ic.proceed();
	}

	private Object track(InvocationContext ic) throws Exception {
		
		CodeTrace trace = ThreadTracker.trace(ic.getMethod());
		
		try {
			return trace.finishReturn(ic.proceed());
		} catch (Exception e) {
			trace.finish(e);
			throw e;
		}
	}
}
