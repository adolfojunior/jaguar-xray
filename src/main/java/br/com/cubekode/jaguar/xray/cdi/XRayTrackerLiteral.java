package br.com.cubekode.jaguar.xray.cdi;

import javax.enterprise.util.AnnotationLiteral;

public class XRayTrackerLiteral extends AnnotationLiteral<XRayTracker> implements XRayTracker {

	private static final XRayTracker INSTANCE = new XRayTrackerLiteral();

	public static XRayTracker instance() {
		return INSTANCE;
	}
}