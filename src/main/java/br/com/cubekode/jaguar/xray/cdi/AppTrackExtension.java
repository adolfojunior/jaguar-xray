package br.com.cubekode.jaguar.xray.cdi;

import java.lang.annotation.Annotation;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

public class AppTrackExtension implements Extension {

	public void onBeforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd) {
		// add jCompany interceptors
		addInterceptorBinding(bbd, "com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil");
		addInterceptorBinding(bbd, "com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB");
		addInterceptorBinding(bbd, "com.powerlogic.jcompany.commons.config.stereotypes.SPlcFacade");
		addInterceptorBinding(bbd, "com.powerlogic.jcompany.commons.config.stereotypes.SPlcFacadeEjb");
		addInterceptorBinding(bbd, "com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository");
		addInterceptorBinding(bbd, "com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject");
		addInterceptorBinding(bbd, "com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObjectEjb");
		addInterceptorBinding(bbd, "com.powerlogic.jcompany.commons.config.stereotypes.SPlcJpaManager");
	}
	
	@SuppressWarnings("unchecked")
	protected void addInterceptorBinding(BeforeBeanDiscovery bbd, String typeName) {
		try {
			Class<? extends Annotation> ann = (Class<? extends Annotation>) Class.forName(typeName);
			bbd.addInterceptorBinding(ann, AppTrackLiteral.instance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
