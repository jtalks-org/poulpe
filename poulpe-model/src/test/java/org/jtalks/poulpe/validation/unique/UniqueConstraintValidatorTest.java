package org.jtalks.poulpe.validation.unique;

import static ch.lambdaj.Lambda.collect;
import static ch.lambdaj.Lambda.on;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Collection;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.jtalks.poulpe.model.dao.hibernate.ObjectsFactory;
import org.jtalks.poulpe.model.entity.Branch;
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
    
    @Test
    public void validateUniqueBranch() {
        branch();
        
        Branch duplicatedName = ObjectsFactory.createBranch();
        Set<ConstraintViolation<Branch>> set = validator.validate(duplicatedName);

        assertTrue(set.isEmpty());
    }

    @Test
    public void validateBranchNameFieldViolated() {
        Branch branch = branch();
        Branch duplicatedName = new Branch(branch.getName());
        Set<ConstraintViolation<Branch>> constraints = validator.validate(duplicatedName);

        Collection<String> fields = extractFieldNames(constraints);

        assertTrue(fields.contains("name"));
    }

    @Test
    public void validateTheSameBranch() {
        Branch branch = branch();
        Set<ConstraintViolation<Branch>> set = validator.validate(branch);

        assertTrue(set.isEmpty());
    }

    private Component forum() {
        Component comp = ObjectsFactory.createComponent(ComponentType.FORUM);
        session.save(comp);
        return comp;
    }

    private Component article() {
        Component comp = ObjectsFactory.createComponent(ComponentType.ARTICLE);
        session.save(comp);
        return comp;
    }

    @Test
    public void validateTwoFieldsOneSavedObject() {
        Component forum = forum();
        Component anotherForum = new Component(forum.getName(), "desc", forum.getComponentType());

        Set<ConstraintViolation<Component>> constraints = validator.validate(anotherForum);

        Collection<String> fields = extractFieldNames(constraints);

        assertTrue(fields.contains("name"));
        assertTrue(fields.contains("componentType"));
    }
    
    @Test
    public void validateWhenOnlyOneFieldIsSet() {
        Component forum = forum();
        Component anotherForum = new Component(forum.getName(), "desc", null);

        Set<ConstraintViolation<Component>> constraints = validator.validate(anotherForum);

        Collection<String> fields = extractFieldNames(constraints);

        assertTrue(fields.contains("name"));
        assertFalse(fields.contains("componentType"));
    }

    @Test
    public void validateTwoFieldsTwoSavedObjects() {
        Component forum = forum();
        Component article = article();
        
        Component articleNameForumType = new Component(article.getName(), "desc", forum.getComponentType());

        Set<ConstraintViolation<Component>> constraints = validator.validate(articleNameForumType);

        Collection<String> fields = extractFieldNames(constraints);

        assertTrue(fields.contains("name"));
        assertTrue(fields.contains("componentType"));
    }
    
    public static <T> Collection<String> extractFieldNames(Set<ConstraintViolation<T>> set) {
        return collect(set, on(ConstraintViolation.class).getPropertyPath().toString());
    }
    
    @Test
    public void validateTwoFieldsOneSavedObjectGetMessages() {
        Component forum = forum();
        Component anotherForum = new Component(forum.getName(), "desc", forum.getComponentType());

        Set<ConstraintViolation<Component>> constraints = validator.validate(anotherForum);
        
        Collection<String> messages = extractMessages(constraints);
        assertTrue(messages.contains(Component.NOT_UNIQUE_NAME));
        assertTrue(messages.contains(Component.NOT_UNIQUE_TYPE));
    }
    
    public static <T> Collection<String> extractMessages(Set<ConstraintViolation<T>> set) {
        return collect(set, on(ConstraintViolation.class).getMessage());
    }
}
