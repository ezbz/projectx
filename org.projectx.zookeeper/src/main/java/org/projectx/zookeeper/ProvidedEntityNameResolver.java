package org.projectx.zookeeper;

import org.springframework.util.Assert;

/**
 * An implementation of the {@link EntityNameResolver} used for self-provisioned
 * entity name (e.g., property-based)
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public class ProvidedEntityNameResolver implements EntityNameResolver {

  private final String entityName;

  public ProvidedEntityNameResolver(final String entityName) {
    Assert.hasText(entityName, "entityName cannot be empty");
    this.entityName = entityName;
  }

  @Override
  public String resolve() {
    return entityName;
  }

}
