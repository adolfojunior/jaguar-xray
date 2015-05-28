package br.com.cubekode.jaguar.xray.cdi;

import javax.enterprise.util.AnnotationLiteral;

public class XRayTraceLiteral extends AnnotationLiteral<XRayTrace> implements XRayTrace {

	private static final XRayTrace INSTANCE = new XRayTraceLiteral();

	public static XRayTrace instance() {
		return INSTANCE;
	}
}