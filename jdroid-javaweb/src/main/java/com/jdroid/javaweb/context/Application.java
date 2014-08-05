package com.jdroid.javaweb.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import com.jdroid.javaweb.domain.Entity;

/**
 * 
 * @param <T>
 */
public class Application<T extends Entity> implements ApplicationContextAware {
	
	private static Application<?> INSTANCE;
	
	public Application() {
		INSTANCE = this;
	}
	
	public static Application<?> get() {
		return INSTANCE;
	}
	
	private AppContext appContext;
	private SecurityContextHolder<T> securityContextHolder;
	private ApplicationContext springApplicationContext;
	
	public AppContext getAppContext() {
		return appContext;
	}
	
	/**
	 * @return The {@link AbstractSecurityContext} instance
	 */
	public AbstractSecurityContext<T> getSecurityContext() {
		return securityContextHolder != null ? securityContextHolder.getContext() : null;
	}
	
	/**
	 * @param beanName The bean name
	 * @return The spring bean with the bean name
	 */
	public Object getBean(String beanName) {
		return springApplicationContext.getBean(beanName);
	}
	
	/**
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.springApplicationContext = applicationContext;
	}
	
	/**
	 * @param securityContextHolder The {@link SecurityContextHolder} to set
	 */
	public void setSecurityContextHolder(SecurityContextHolder<T> securityContextHolder) {
		this.securityContextHolder = securityContextHolder;
	}
	
	/**
	 * @param appContext the appContext to set
	 */
	public void setAppContext(AppContext appContext) {
		this.appContext = appContext;
	}
}
