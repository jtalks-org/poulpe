package org.jtalks.poulpe.validation.unique;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.model.dao.hibernate.ObjectsFactory;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Alexey Grigorev
 */
@ContextConfiguration(locations = { "classpath:/org/jtalks/poulpe/model/entity/applicationContext-dao.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class UniquenessViolatorsRetrieverTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory;
    private Session session;
    private UniquenessViolatorsRetriever retriever;

    @BeforeMethod
    public void setUp() {
        session = sessionFactory.getCurrentSession();

        retriever = new UniquenessViolatorsRetriever();
        retriever.setSessionFactory(sessionFactory);
    }

    @Test
    public void noDuplicatesForItself() {
        EntityWrapper forum = wrap(forum());

        List<EntityWrapper> duplicates = retriever.duplicatesFor(forum);

        assertTrue(duplicates.isEmpty());
    }

    private Component forum() {
        Component component = ObjectsFactory.createComponent(ComponentType.FORUM);
        session.save(component);
        return component;
    }

    public static EntityWrapper wrap(Entity entity) {
        return new EntityWrapper(entity);
    }

    @Test
    public void oneItemDuplicatesName() {
        Component forum = forum();
        EntityWrapper another = wrap(new Component(forum.getName(), "desc", ComponentType.ARTICLE));

        List<EntityWrapper> duplicates = retriever.duplicatesFor(another);

        assertEquals(duplicates.size(), 1);
    }
    
    @Test
    public void oneItemDuplicatesNameAndType() {
        Component forum = forum();
        EntityWrapper another = wrap(new Component(forum.getName(), "desc", forum.getComponentType()));

        List<EntityWrapper> duplicates = retriever.duplicatesFor(another);

        assertEquals(duplicates.size(), 1);
    }
    
    @Test
    public void twoItemDuplicateNameAndType() {
        Component forum = forum();
        Component article = article();
        EntityWrapper another = wrap(new Component(forum.getName(), "desc", article.getComponentType()));

        List<EntityWrapper> duplicates = retriever.duplicatesFor(another);

        assertEquals(duplicates.size(), 2);
    }
    
    private Component article() {
        Component comp = ObjectsFactory.createComponent(ComponentType.ARTICLE);
        session.save(comp);
        return comp;
    }
    
}
