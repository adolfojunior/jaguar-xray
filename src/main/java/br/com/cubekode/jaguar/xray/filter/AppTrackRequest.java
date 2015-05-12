package br.com.cubekode.jaguar.xray.filter;

import java.security.Principal;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import br.com.cubekode.jaguar.xray.CodeTrace;
import br.com.cubekode.jaguar.xray.TrackThread;

public class AppTrackRequest {

	private static final String ANONYMOUS = "anonymous";

	public static CodeTrace traceRequest(HttpServletRequest request) {
		return TrackThread.traceExecution(requestInfo(request));
	}

	public static String requestInfo(HttpServletRequest request) {

		Principal principal = request.getUserPrincipal();

		String user = principal != null ? principal.getName() : ANONYMOUS;
		String type = request.getMethod();
		String url = request.getRequestURL().toString();

		return (user == null || user.isEmpty() ? ANONYMOUS : user) + " - " + type + " - " + url;
	}

	public static boolean isTrackRequest(HttpServletRequest request) {
		String uri = request.getRequestURI();
		if (uri.contains("/f/") || uri.contains(".xhtml")) {
			for (String ext : Arrays.asList(".css", ".js", ".ecss", ".png", ".jpg", ".gif")) {
				if (uri.endsWith(ext)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
