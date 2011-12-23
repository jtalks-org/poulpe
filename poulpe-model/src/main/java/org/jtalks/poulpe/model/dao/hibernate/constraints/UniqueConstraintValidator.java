package org.jtalks.poulpe.model.dao.hibernate.constraints;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.jtalks.common.model.entity.Entity;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO: add javadocs
 * 
 * @author Tatiana Birina
 * @author Alexey Grigorev
 * 
 * @see UniqueFields
 * @see <a
 * href="http://stackoverflow.com/questions/1972933/cross-field-validation-with-hibernate-validator-jsr-303">initial idea</a>
 * 
 */
public class UniqueConstraintValidator implements ConstraintValidator<UniqueFields, Entity> {

    public static final String MESSAGE = "field must be unique";

    @Autowired
    private SessionFactory sessionFactory;

    private List<String> fields;

    /**
     * retrieves field to check and Class instance
     */
    @Override
    public void initialize(UniqueFields annotation) {
        fields = Arrays.asList(annotation.fields());
        if (fields.isEmpty()) {
            throw new IllegalArgumentException("Expected the list of fields to contain at least one element");
        }
    }

    /**
     * determines if the value is unique
     * 
     * @return true if no duplicates are found false if there are duplicates or
     * value is empty
     */
    @Override
    public boolean isValid(Entity entity, ConstraintValidatorContext context) {
        UniquenessViolationFinder finder = new UniquenessViolationFinder(entity, fields, context, getSession());
        return finder.isValid();
    }

    /**
     * Get current Hibernate session.
     * 
     * @return current Session
     */
    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}