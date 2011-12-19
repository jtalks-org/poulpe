package org.jtalks.poulpe.model.dao.hibernate.constraints;

import static org.hibernate.criterion.DetachedCriteria.forClass;
import static org.hibernate.criterion.Projections.count;
import static org.hibernate.criterion.Restrictions.eq;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class UniqueConstraintValidator implements
		ConstraintValidator<Unique, String> {
	
	private Class<?> entity;
	private String field;

	/**
	 * retrieves field to check and Class instance
	 */
	public void initialize(Unique annotation) {
		this.entity = annotation.entity();
		this.field = annotation.field();
	}

	/**
	 * determines if the value is unique
	 * 
	 * @return true if no duplicates are found false if there are duplicates or
	 *         value is empty
	 */
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isEmpty(value)) {
			return false;
		}
		return query(value).intValue() == 0;
	}

	private Number query(String value) {
		Criteria criteria = getSession().createCriteria(entity);
		criteria.add(Restrictions.eq(field, value));
		criteria.setProjection(Projections.rowCount());
		Number count = (Number) criteria.list().get(0);
		return count;
	}

	/**
	 * Hibernate SessionFactory
	 */
	private SessionFactory sessionFactory;

	/**
	 * Get current Hibernate session.
	 * 
	 * @return current Session
	 */
	// TODO: if should be removed after issue with
	// sessionFactory(NullPointer exception) will be resolved
	protected Session getSession() {
		Session ses = null;
		if (sessionFactory != null) {
			ses = sessionFactory.getCurrentSession();
		}
		return ses;
	}

	/**
	 * Setter for Hibernate SessionFactory.
	 * 
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}