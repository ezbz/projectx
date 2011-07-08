package org.projectx.hibernate.dao.hibernate;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.junit.Test;
import org.projectx.hibernate.dao.PropertyNames;
import org.projectx.hibernate.dao.TestEntity;

/**
 * Test for the {@link AbstractOrderCallback} class
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public class OrderCallbackTest {
  private static final TestEntity TEST_ENTITY = new TestEntity(5L);

  @Test
  public void testDoInHibernate() throws HibernateException, SQLException {
    final TestOrderedCallback classUnderTest = new TestOrderedCallback(
        PropertyNames.EntityProperties.ID, true, TestEntity.class);
    final List<TestEntity> expectedResult = Collections.singletonList(TEST_ENTITY);
    List<TestEntity> actualResult = null;

    final Session session = createMock(Session.class);
    final Criteria criteria = createMock(Criteria.class);

    expect(session.createCriteria(TestEntity.class)).andReturn(criteria);
    expect(criteria.addOrder(isA(Order.class))).andReturn(criteria);
    expect(criteria.list()).andReturn(expectedResult);

    replay(criteria, session);

    actualResult = classUnderTest.doInHibernate(session);

    verify(criteria, session);

    assertTrue("prepare should've been invoked", classUnderTest.isPrepareInvoked());
    assertTrue("result is null", actualResult != null);
    assertEquals("result sizes don't match", actualResult.size(), expectedResult.size());
    assertEquals("results don't match", actualResult.get(0), expectedResult.get(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalCreationNullProperty() {
    new TestOrderedCallback(null, false, TestEntity.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalCreationEmptyProperty() {
    new TestOrderedCallback(StringUtils.EMPTY, false, TestEntity.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalCreationNullClass() {
    new TestOrderedCallback(PropertyNames.EntityProperties.ID, false, null);
  }

  private static final class TestOrderedCallback extends AbstractOrderCallback<TestEntity> {

    private boolean prepareInvoked;

    public TestOrderedCallback(final String property, final boolean isAscending,
        final Class<TestEntity> clazz) {
      super(property, isAscending, clazz);
    }

    @Override
    public void prepare(final Criteria criteria) {
      prepareInvoked = true;
    }

    public boolean isPrepareInvoked() {
      return prepareInvoked;
    }

  }
}
