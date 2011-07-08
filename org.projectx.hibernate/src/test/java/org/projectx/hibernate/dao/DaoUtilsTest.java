package org.projectx.hibernate.dao;

import static org.easymock.EasyMock.createControl;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections15.CollectionUtils;
import org.easymock.IMocksControl;
import org.junit.Test;

/**
 * Test for {@link DaoUtils} class
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 * 
 */
public class DaoUtilsTest {
  private final IMocksControl control = createControl();

  @Test
  public void testOrderEntitiesByPrimaryKeys() {
    final List<Persistable> entities = new ArrayList<Persistable>();
    final Persistable persistable1 = control.createMock(Persistable.class);
    final Persistable persistable2 = control.createMock(Persistable.class);
    final Persistable persistable3 = control.createMock(Persistable.class);
    CollectionUtils.addAll(entities, persistable1, persistable2, persistable3);

    final String pk1 = new String("pk1");
    final String pk2 = new String("pk2");
    final String pk3 = new String("pk3");
    expect(persistable1.getPrimaryKey()).andReturn(pk1);
    expect(persistable2.getPrimaryKey()).andReturn(pk2);
    expect(persistable3.getPrimaryKey()).andReturn(pk3);

    final List<String> primaryKeys = new LinkedList<String>();
    CollectionUtils.addAll(primaryKeys, pk1, pk3, pk2);

    control.replay();
    final List<Persistable> orderedList = DaoUtils.orderEntitiesByPrimaryKeys(entities, primaryKeys);
    control.verify();

    assertEquals(orderedList.get(0), persistable1);
    assertEquals(orderedList.get(1), persistable3);
    assertEquals(orderedList.get(2), persistable2);
  }
}
