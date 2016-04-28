package org.harvest.crawler.framework;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 获取spring信息的工具类
 * 
 * @author Administrator
 * 
 */
public final class ToolSpring implements ApplicationContextAware {
	
	private Logger log = Logger.getLogger(ToolSpring.class);
	
	private static ApplicationContext applicationContext = null;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (ToolSpring.applicationContext == null) {
			ToolSpring.applicationContext = applicationContext;
			log.debug("get applicationContext start");
			System.out.println();
			System.out.println();
			System.out.println("---------------------------------------------------------------------");
			System.out.println("========ApplicationContext配置成功,在普通类可以通过调用ToolSpring.getAppContext()获取applicationContext对象,applicationContext=" + applicationContext + "========");
			System.out.println("---------------------------------------------------------------------");
			System.out.println();
			System.out.println();
			log.debug("get applicationContext over");
		}
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static Object getBean(String name) {
		System.out.println("================getBean =========================="+name);
		return getApplicationContext().getBean(name);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object getBean(Class arg0) {
		return getApplicationContext().getBean(arg0);
	}
}