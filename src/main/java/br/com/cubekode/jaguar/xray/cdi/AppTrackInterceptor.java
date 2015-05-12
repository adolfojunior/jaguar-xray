package br.com.cubekode.jaguar.xray.cdi;

import java.io.Serializable;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import br.com.cubekode.jaguar.xray.CodeTrace;
import br.com.cubekode.jaguar.xray.TrackThread;

@AppTrack
@Interceptor
public class AppTrackInterceptor implements Serializable {

	private static final long serialVersionUID = 1L;

	@AroundInvoke
	public Object intercept(InvocationContext ic) throws Exception {

		String error = null;
		CodeTrace trace = TrackThread.trace(ic.getMethod());

		try {
			return ic.proceed();
		} catch (Exception e) {
			error = e.getClass().getName() + ":" + e.toString();
			throw e;
		} finally {
			if (trace != null) {
				if (error != null) {
					trace.finish(error);
				} else {
					trace.finish();
				}
			}
		}
	}
}
