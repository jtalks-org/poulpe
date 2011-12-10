package org.jtalks.poulpe.model.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ExpectedExceptions;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@ContextConfiguration(locations = {"classpath:/org/jtalks/poulpe/model/entity/applicationContext-dao.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class GroupHibernateDaoTest  extends AbstractTransactionalTestNGSpringContextTests{
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private GroupDao dao;
    
    private Session session;

    @BeforeMethod
    public void setUp() throws Exception {
        session = sessionFactory.getCurrentSession();
    }
    
    
    @Test
    /**
     * Straightforward saving of the group
     */
    public void testSave(){
        Group testGroup = ObjectsFactory.createGroup();
        session.save(testGroup);
        
        assertNotSame(testGroup.getId(),0,"ID is not created");
        
        session.evict(testGroup);
        
        Group savedGroup = (Group)session.get(Group.class, testGroup.getId());
        
        assertReflectionEquals(testGroup, savedGroup);        
        
    }
    
    @Test(expectedExceptions=DataIntegrityViolationException.class)
    /**
     * Try to save the group with a null name
     */    
    public void testSaveGroupWithNullName(){
        Group testGroup = new Group();
        dao.saveOrUpdate(testGroup);        
    }
    
    public void testGetById(){
        Group group = ObjectsFactory.createGroup();
        session.save(group);        

        Group result = dao.get(group.getId());

        assertNotNull(result);
        assertEquals(result.getId(), group.getId());
    }
    
    @Test
    public void testGetInvalidId() {
        Group result = dao.get(-567890L);
        assertNull(result);
    }
}
