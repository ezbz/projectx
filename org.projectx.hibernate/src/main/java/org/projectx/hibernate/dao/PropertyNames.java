package org.projectx.hibernate.dao;

/**
 * A class representing common properties assosciated with {@link Persistable}
 * entities.
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public abstract class PropertyNames {
  protected PropertyNames() {
  }

  public class PersistableProperties {
    public static final String PK = "pk";
  }

  public class EntityProperties {
    public static final String ID = "id";
  }

  public class NamedEntityProperties {
    public static final String NAME = "name";
  }
}
