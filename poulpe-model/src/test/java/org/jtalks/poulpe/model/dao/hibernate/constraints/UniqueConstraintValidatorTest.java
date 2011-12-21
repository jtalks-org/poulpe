package org.jtalks.poulpe.model.dao.hibernate.constraints;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.jtalks.poulpe.model.dao.hibernate.ObjectsFactory;
import org.jtalks.poulpe.model.entity.Branch;
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
public class UniqueConstraintValidatorTest extends AbstractTransactionalTestNGSpringContextTests {
    
    @Autowired
    private Validator validator;

    @Autowired
    private SessionFactory sessionFactory;

    private Session session;

    @BeforeMethod
    public void setUp() {
        session = sessionFactory.getCurrentSession();
    }

    @Test
    public void validateBranch() {
        Branch branch = branch();
        Branch duplicatedName = new Branch(branch.getName());
        Set<ConstraintViolation<Branch>> set = validator.validate(duplicatedName);
        
        assertFalse(set.isEmpty());
    }

    private Branch branch() {
        Branch branch = ObjectsFactory.createBranch();
        session.save(branch.getSection());
        session.save(branch);
        return branch;
    }
    
    // TODO: make it work
    @Test(enabled = false)
    public void validateBranchNameFieldViolated() {
        Branch branch = branch();
        Branch duplicatedName = new Branch(branch.getName());
        Set<ConstraintViolation<Branch>> set = validator.validate(duplicatedName);
        
        assertEquals(set.size(), 1);
        
        ConstraintViolation<Branch> violation = set.iterator().next();
        assertEquals(violation.getPropertyPath(), "name");
    }
    
    @Test
    public void validateTheSameBranch() {
        Branch branch = branch();
        Set<ConstraintViolation<Branch>> set = validator.validate(branch);
        
        assertTrue(set.isEmpty());
    }
}
