package br.com.cubekode.jaguar.xray.viewer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.cubekode.jaguar.xray.TrackStore;
import br.com.cubekode.jaguar.xray.json.JsonData;

public class TrackViewer {

	private static final String URI_XRAY_VIEWER = "/jaguar-xray-viewer";

	public static boolean isViewerUrl(HttpServletRequest request) {
		return request.getRequestURI().contains(URI_XRAY_VIEWER);
	}

	public static void viewer(HttpServletRequest request, HttpServletResponse response) {
		new TrackViewer().process(request, response);
	}

	protected void process(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (request.getRequestURI().contains("/client")) {
				responseResource(response, "text/html", "/xray-resources/client.html");
			} else {
				response(response, execute(request));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected JsonData execute(HttpServletRequest request) {

		TrackStore store = TrackStore.getInstance();

		String id = request.getParameter("id");
		String parent = request.getParameter("parent");

		JsonData json = new JsonData();

		if (id == null || id.isEmpty()) {
			if (Boolean.parseBoolean(request.getParameter("clear"))) {
				store.clear();
			}
			json.value(store.all());
		} else if (parent == null || parent.isEmpty()) {
			json.value(store.get(id));
		} else if (parent.equals("all")) {
			json.value(store.nodes(id));
		} else {
			json.value(store.nodes(id, Long.parseLong(parent)));
		}
		return json;
	}

	protected void response(HttpServletResponse response, JsonData json) throws IOException {
		// Cache Headers
		contentType(response, "application/json");
		// Cross Domain
		crossDomain(response);
		// send json
		PrintWriter writer = response.getWriter();
		writer.append(json);
		writer.flush();
	}

	protected void responseResource(HttpServletResponse response, String type, String resource) throws IOException {

		InputStream in = getClass().getResourceAsStream(resource);

		if (in == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {

			contentType(response, type);

			try {
				OutputStream out = response.getOutputStream();
				byte[] buffer = new byte[4096];
				for (int count = in.read(buffer); count != -1; count = in.read(buffer)) {
					out.write(buffer, 0, count);
				}
				out.flush();
			} finally {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
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
