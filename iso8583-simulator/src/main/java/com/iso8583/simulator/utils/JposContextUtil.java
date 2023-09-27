package com.iso8583.simulator.utils;

import org.jpos.util.NameRegistrar;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class JposContextUtil implements ApplicationContextAware {
  public static final String APP_CONTEXT = "SPRING_CONTEXT";

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    NameRegistrar.register(APP_CONTEXT, applicationContext);
  }

  public static ApplicationContext getContext() {
    Object obj = NameRegistrar.getIfExists(APP_CONTEXT);
    if (obj instanceof ApplicationContext) {
      return (ApplicationContext) obj;
    }
    return null;
  }

  public static final <T> T getBean(String name, Class<T> clazz) {
    ApplicationContext ctx = getContext();
    if (ctx == null) return null;

    return ctx.getBean(name, clazz);
  }


  public static final <T> T getBean(Class<T> clazz) {
    ApplicationContext ctx = getContext();
    if (ctx == null) return null;

    return ctx.getBean(clazz);
  }
}
