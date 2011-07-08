package org.projectx.hibernate.dao;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.projectx.hibernate.dao.hibernate.CriteriaCallback;
import org.projectx.hibernate.dao.hibernate.NamedQueryCallback;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * Test for {@link AbstractEntityHibernateDaoSupport} class
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public class EntityHibernateDaoSupportTest {
  private static final Long ID = 7L;
  private static final TestEntity TEST_ENTITY = new TestEntity(ID);
  private static final List<TestEntity> TEST_ENTITIES = Collections.singletonList(TEST_ENTITY);
  private HibernateTemplate hibernateTemplate;
  private TestDao dao;

  @Before
  public void setup() {
    hibernateTemplate = createMock(HibernateTemplate.class);
    dao = new TestDao(hibernateTemplate);
  }

  @Test
  public void testFindAll() {
    expect(hibernateTemplate.loadAll(TestEntity.class)).andReturn(TEST_ENTITIES);
    replay(hibernateTemplate);
    final List<TestEntity> result = dao.findAll();
    verify(hibernateTemplate);
    assertEquals("incorrect collection sizes", TEST_ENTITIES.size(), result.size());
    assertEquals("incorrect entiies", TEST_ENTITIES.get(0), result.get(0));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testFindEntity() {
    expect(hibernateTemplate.execute(isA(HibernateCallback.class))).andReturn(TEST_ENTITY);

    final CriteriaCallback callback = createMock(CriteriaCallback.class);
    replay(hibernateTemplate, callback);
    final TestEntity result = dao.findEntity(callback);
    verify(hibernateTemplate, callback);

    assertEquals("incorrect entiies", TEST_ENTITY, result);
  }

  @Test
  public void testFindByPrimaryKey() {
    expect(hibernateTemplate.get("TestEntity", ID)).andReturn(TEST_ENTITY);
    replay(hibernateTemplate);
    final TestEntity result = dao.findByPrimaryKey(ID);
    verify(hibernateTemplate);

    assertEquals("incorrect entiies", TEST_ENTITY, result);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testFindByPrimaryKeys() {
    expect(hibernateTemplate.execute(isA(HibernateCallback.class))).andReturn(
        Collections.singletonList(TEST_ENTITY));
    replay(hibernateTemplate);
    final List<TestEntity> result = dao.findByPrimaryKeys(Collections.singletonList(ID));
    verify(hibernateTemplate);

    assertEquals("incorrect entiies", TEST_ENTITY, result.get(0));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testFindByPrimaryKeysOrdered() {
    expect(hibernateTemplate.execute(isA(HibernateCallback.class))).andReturn(
        Collections.singletonList(TEST_ENTITY));
    replay(hibernateTemplate);
    final List<TestEntity> result = dao.findByPrimaryKeysOrdered(Collections.singletonList(ID));
    verify(hibernateTemplate);

    assertEquals("incorrect entiies", TEST_ENTITY, result.get(0));

    assertEquals(Collections.emptyList(),
        dao.findByPrimaryKeysOrdered(new LinkedList<Serializable>()));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testExecuteUpdate() {
    expect(hibernateTemplate.execute(isA(HibernateCallback.class))).andReturn(1);
    replay(hibernateTemplate);
    final NamedQueryCallback<TestEntity> callback = createMock(NamedQueryCallback.class);
    final int result = dao.executeUpdate(callback);
    verify(hibernateTemplate);

    assertEquals("incorrect number of records updated", 1, result);
  }

  @Test
  public void testUpdate() {
    hibernateTemplate.saveOrUpdate(isA(String.class), isA(TestEntity.class));
    expectLastCall();
    replay(hibernateTemplate);
    final TestEntity result = dao.update(TEST_ENTITY);
    verify(hibernateTemplate);

    assertEquals("incorrect entity updated", result, TEST_ENTITY);
  }

  @Test
  public void testDelete() {
    hibernateTemplate.delete(isA(TestEntity.class));
    expectLastCall();
    replay(hibernateTemplate);
    dao.delete(TEST_ENTITY);
    verify(hibernateTemplate);
  }

  @Test
  public void testDeleteByPK() {
    expect(hibernateTemplate.load(isA(String.class), isA(Long.class))).andReturn(TEST_ENTITY);
    hibernateTemplate.delete(isA(TestEntity.class));
    expectLastCall();
    replay(hibernateTemplate);
    dao.deleteByPK(ID);
    verify(hibernateTemplate);
  }

  @Test
  public void testDeleteByPKs() {
    expect(hibernateTemplate.load(isA(String.class), isA(Long.class))).andReturn(TEST_ENTITY);
    hibernateTemplate.delete(isA(TestEntity.class));
    expectLastCall();
    replay(hibernateTemplate);
    final Collection<Serializable> ids = new LinkedList<Serializable>();
    ids.add(ID);
    dao.deleteByPKs(ids);
    verify(hibernateTemplate);
  }

  @Test
  public void testEvictEntity() {
    expect(hibernateTemplate.load(isA(String.class), isA(Long.class))).andReturn(TEST_ENTITY);
    hibernateTemplate.evict(isA(TestEntity.class));
    expectLastCall();
    replay(hibernateTemplate);
    dao.evictEntity(TEST_ENTITY);
    verify(hibernateTemplate);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testCount() {
    final CriteriaCallback callback = createMock(CriteriaCallback.class);

    expect(hibernateTemplate.execute(isA(HibernateCallback.class))).andReturn(1);
    expectLastCall();
    replay(hibernateTemplate);
    dao.count(callback);
    verify(hibernateTemplate);
  }

  private static final class TestDao extends AbstractEntityHibernateDaoSupport<TestEntity> {

    public TestDao(final HibernateTemplate hibernateTemplate) {
      super(hibernateTemplate);
    }

    @Override
    public Class<TestEntity> getEntityClass() {
      return TestEntity.class;
    }
  }
}
