package org.jtalks.poulpe.model.dao.hibernate.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jtalks.common.model.entity.Entity;

/**
 * TODO: add javadocs
 * 
 * @author Tatiana Birina
 * @author Alexey Grigorev
 * 
 * @see <a
 * href="http://stackoverflow.com/questions/1972933/cross-field-validation-with-hibernate-validator-jsr-303">idea</a>
 */
public class UniqueConstraintValidator implements ConstraintValidator<UniqueFields, Entity> {

    /**
     * Hibernate SessionFactory
     */
    private SessionFactory sessionFactory;

    private String[] fields;

    /**
     * retrieves field to check and Class instance
     */
    @Override
    public void initialize(UniqueFields annotation) {
        fields = annotation.fields();
    }

    /**
     * determines if the value is unique
     * 
     * @return true if no duplicates are found false if there are duplicates or
     * value is empty
     */
    @Override
    public boolean isValid(Entity entity, ConstraintValidatorContext context) {
        // TODO: for making test 'validateBranchNameFieldViolated' work,
        // builder from context may be used, further investigation needed
        // TODO: the same approach as in ComponentDuplicatesFinder might be used for
        // building a set of violated fields
        Number result = retrieveDuplicatesCount(entity);
        boolean violated = result.intValue() == 0;
        return violated;
    }

    private Number retrieveDuplicatesCount(Entity entity) {
        Criteria criteria = getSession().createCriteria(entity.getClass());

        for (String field : fields) {
            String property = extractField(entity, field);
            if (property != null) {
                criteria.add(Restrictions.eq(field, property));
            }
        }

        criteria.add(Restrictions.ne("id", entity.getId()));

        criteria.setProjection(Projections.rowCount());

        Number result = (Number) criteria.uniqueResult();
        return result;
    }

    private String extractField(Entity entity, String field) {
        try {
            return BeanUtils.getProperty(entity, field);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get current Hibernate session.
     * 
     * @return current Session
     */
    protected Session getSession() {
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