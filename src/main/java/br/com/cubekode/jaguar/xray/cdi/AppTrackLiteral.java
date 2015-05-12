package br.com.cubekode.jaguar.xray.cdi;

import javax.enterprise.util.AnnotationLiteral;

public class AppTrackLiteral extends AnnotationLiteral<AppTrack> implements AppTrack {

	private static final AppTrack INSTANCE = new AppTrackLiteral();

	public static AppTrack instance() {
		return INSTANCE;
	}
}