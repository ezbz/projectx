package org.projectx.zookeeper.election;

import org.projectx.zookeeper.EntityNameResolver;
import org.projectx.zookeeper.NodePathResolver;
import org.projectx.zookeeper.ZookeeperConstants;
import org.springframework.util.Assert;

/**
 * Default implementation of {@link NodePathResolver} which combines a root
 * path provided at construction time and an entity name provided at runtime.
 * 
 * <p>
 * Defines a convention where the constructed path is in the form:
 * <code>/electionRootPath/entityName/election/</code>.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * 
 */
public class DefaultElectionPathResolver implements NodePathResolver {

  private final String electionRootPath;
  private final EntityNameResolver entityNameResolver;

  public DefaultElectionPathResolver(final String electionRootPath,
      final EntityNameResolver entityNameResolver) {
    Assert.hasText(electionRootPath, "electionRootPath cannot be empty");
    Assert.notNull(entityNameResolver, "entityNameResolver cannot be null");
    this.electionRootPath = electionRootPath;
    this.entityNameResolver = entityNameResolver;
  }

  @Override
  public String resolve() {
    return new StringBuilder(electionRootPath).append(ZookeeperConstants.PATH_SEPARATOR)
                                              .append(entityNameResolver.resolve())
                                              .append(ZookeeperConstants.PATH_SEPARATOR)
                                              .append("election").toString();
  }
}
