package br.com.cubekode.jaguar.xray.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.cubekode.jaguar.xray.CodeTrace;
import br.com.cubekode.jaguar.xray.viewer.TrackViewer;

public class AppTrackFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
		} else {
			chain.doFilter(request, response);
		}
	}

	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (TrackViewer.isViewerUrl(request)) {
			
			TrackViewer.viewer(request, response);
			
		} else if (AppTrackRequest.isTrackRequest(request)) {
			
			doTraceFilter(request, response, chain);
			
		} else {
			
			chain.doFilter(request, response);
		}
	}

	protected void doTraceFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		CodeTrace trace = AppTrackRequest.traceRequest(request);
		try {
			chain.doFilter(request, response);
		} finally {
			trace.finish();
		}
	}

	@Override
	public void destroy() {
	}
}
