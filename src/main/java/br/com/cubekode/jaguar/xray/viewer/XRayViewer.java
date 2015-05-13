package br.com.cubekode.jaguar.xray.viewer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.cubekode.jaguar.xray.TrackStore;
import br.com.cubekode.jaguar.xray.json.JsonData;

public class XRayViewer {

	private static final String JAGUAR_XRAY_RESOURCES = "/jaguar-xray-resources";
	private static final XRayViewer INSTANCE = new XRayViewer();

	public static XRayViewer getInstance() {
		return INSTANCE;
	}

	public boolean isViewerUrl(HttpServletRequest request) {
		String uri = request.getRequestURI().toLowerCase();
		return uri.contains("/jaguarxray") || uri.contains("/jaguar-xray");
	}

	public void process(HttpServletRequest request, HttpServletResponse response) {
		try {
			String uri = request.getRequestURI().toLowerCase();

			if (uri.endsWith("/list")) {

				responseJson(response, TrackStore.getInstance().all());

			} else if (uri.endsWith("/nodes")) {

				responseJson(response, TrackStore.getInstance().nodes(request.getParameter("id")));

			} else if (uri.endsWith("/clear")) {

				TrackStore.getInstance().clear();

				responseJson(response, true);

			} else {
				responseResource(response, "text/html", JAGUAR_XRAY_RESOURCES.concat("/client.html"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void responseJson(HttpServletResponse response, Object value) throws IOException {
		// Cache Headers
		contentType(response, "application/json");
		// Cross Domain
		crossDomain(response);
		// send json
		PrintWriter writer = response.getWriter();
		writer.append(new JsonData().value(value));
		writer.flush();
	}

	protected void responseResource(HttpServletResponse response, String type, String resource) throws IOException {

		contentType(response, type);

		final InputStream inputStrean = getClass().getResourceAsStream(resource);

		if (inputStrean != null) {
			try {
				response(response, inputStrean);
			} finally {
				try {
					inputStrean.close();
				} catch (IOException e) {
				}
			}
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	protected void response(HttpServletResponse response, InputStream in) throws IOException {
		int count = 0;
		byte[] buffer = new byte[4096];
		OutputStream out = response.getOutputStream();
		while ((count = in.read(buffer)) != -1) {
			out.write(buffer, 0, count);
		}
		out.flush();
	}

	private void crossDomain(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, HEAD, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Access-Control-Max-Age", "0");
	}

	private void contentType(HttpServletResponse response, String type) {
		response.setHeader("Content-Type", type);
		response.setHeader("Cache-control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "-1");
	}
}
