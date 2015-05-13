package br.com.cubekode.jaguar.xray.jsf;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.cubekode.jaguar.xray.CodeTrace;
import br.com.cubekode.jaguar.xray.ThreadTracker;
import br.com.cubekode.jaguar.xray.filter.RequestTracker;
import br.com.cubekode.jaguar.xray.viewer.XRayViewer;

public class PhaseListenerTracker implements PhaseListener {

	private static final long serialVersionUID = 1L;

	private static final String APP_REQUEST_TRACE = "AppPhaseListenerTrack";

	private static final String TYPE_JSF = "JSF";

	@Override
	public void beforePhase(PhaseEvent event) {

		HttpServletRequest request = getRequest(event);

		if (XRayViewer.getInstance().isViewerUrl(request)) {
			
			processViewer(event);
			
		} else {

			if (getTrace(request, TYPE_JSF) == null) {
				setTrace(request, TYPE_JSF, RequestTracker.traceRequest(request));
			}

			String phase = event.getPhaseId().toString();

			setTrace(request, phase, ThreadTracker.trace(phase));
		}
	}

	private void processViewer(PhaseEvent event) {

		FacesContext facesContext = event.getFacesContext();
		ExternalContext externalContext = facesContext.getExternalContext();

		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		
		XRayViewer.getInstance().process(request, response);

		facesContext.responseComplete();
	}

	@Override
	public void afterPhase(PhaseEvent event) {

		HttpServletRequest request = getRequest(event);

		finishTrace(request, event.getPhaseId().toString());

		if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId()) || event.getFacesContext().getResponseComplete()) {
			finishTrace(request, TYPE_JSF);
		}
	}

	protected HttpServletRequest getRequest(PhaseEvent event) {
		return (HttpServletRequest) event.getFacesContext().getExternalContext().getRequest();
	}

	protected void finishTrace(HttpServletRequest request, String type) {

		CodeTrace trace = getTrace(request, type);

		if (trace != null) {
			trace.finish();
		}
		setTrace(request, type, null);
	}

	protected CodeTrace getTrace(HttpServletRequest request, String type) {
		return (CodeTrace) request.getAttribute(APP_REQUEST_TRACE + type);
	}

	protected void setTrace(HttpServletRequest request, String type, CodeTrace trace) {
		request.setAttribute(APP_REQUEST_TRACE + type, trace);
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}
}
