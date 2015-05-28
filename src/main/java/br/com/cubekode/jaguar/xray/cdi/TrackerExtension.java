package br.com.cubekode.jaguar.xray.cdi;

import java.lang.annotation.Annotation;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

public class TrackerExtension implements Extension {

	public void onBeforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd, BeanManager beanManager) {
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

	protected void addInterceptorBinding(BeforeBeanDiscovery bbd, String typeName) {
		try {
			Class<? extends Annotation> annotationClass = findAnnotationClass(typeName);
			if (annotationClass != null) {
				bbd.addInterceptorBinding(annotationClass, XRayTraceLiteral.instance());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Annotation> findAnnotationClass(String typeName) throws ClassNotFoundException {

		Class<?> ann = Class.forName(typeName, true, Thread.currentThread().getContextClassLoader());

		if (ann.isAnnotation()) {
			return (Class<? extends Annotation>) ann;
		}
		return null;
	}
}
