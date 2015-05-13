package br.com.cubekode.jaguar.xray.filter;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import br.com.cubekode.jaguar.xray.CodeTrace;
import br.com.cubekode.jaguar.xray.ThreadTracker;

public class RequestTracker {

	private static final String ANONYMOUS = "anonymous";

	private static final List<String> INVALID_EXTENSIONS = Arrays.asList(".css", ".js", ".ecss", ".png", ".jpg", ".gif");

	public static CodeTrace traceRequest(HttpServletRequest request) {
		return ThreadTracker.traceExecution(buildRequestInfo(request));
	}

	public static String buildRequestInfo(HttpServletRequest request) {

		Principal principal = request.getUserPrincipal();

		String user = principal != null ? principal.getName() : null;
		String type = request.getMethod();
		String url = request.getRequestURL().toString();

		return (user == null || user.isEmpty() ? ANONYMOUS : user) + " - " + type + " - " + url;
	}

	public static boolean isTrackRequest(HttpServletRequest request) {

		String uri = request.getRequestURI().toLowerCase();

		if (uri.contains("/f/") || uri.endsWith(".xhtml")) {
			for (String ext : INVALID_EXTENSIONS) {
				if (uri.endsWith(ext)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
