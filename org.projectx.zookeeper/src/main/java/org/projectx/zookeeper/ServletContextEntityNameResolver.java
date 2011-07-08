package org.projectx.zookeeper;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

/**
 * An implementation of the {@link EntityNameResolver} based on a
 * {@link WebApplicationContext} and the
 * {@link ServletContext#getServletContextName() servlet name}
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public class ServletContextEntityNameResolver implements ApplicationContextAware,
    EntityNameResolver, InitializingBean {

  private String entityName;
  private WebApplicationContext webApplicationContext;

  @Override
  public String resolve() {
    return entityName;
  }

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext)
      throws BeansException {
    this.webApplicationContext = (WebApplicationContext) applicationContext;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    final ServletContext servletContext = webApplicationContext.getServletContext();
    this.entityName = servletContext.getServletContextName();
  }

}
