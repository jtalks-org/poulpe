/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.poulpe.model.dao.hibernate.constraints;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintViolation;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Component;

/**
 * Helper class for using in {@link UniqueConstraintValidator}. It was extracted
 * out for refactoring purposes and now is used for checking if the given entity
 * violates uniqueness of fields set by {@link UniqueFields} annotation.
 * 
 * @author Alexey Grigorev
 * @author Tatiana Birina
 * 
 * @see UniqueConstraintValidator
 * @see UniqueFields
 */
class UniquenessViolationFinder {
    private final Entity entity;

    private final List<String> fields;
    private final Map<String, Object> fieldValues;

    private final ConstraintValidatorContext context;
    private final Session session;

    /**
     * Prepares the object for the further usage, after that call
     * {@link #isValid()}.<br>
     * <br>
     * 
     * <b>Note</b>: Once created, it automatically disables the default constant
     * violation, so in the result will be only those violations which are
     * manually added via {@link ConstraintViolationBuilder}
     * 
     * @param entity the candidate for violating uniqueness
     * @param fields whose values are to check for duplications, must not be
     * empty (in {@link UniqueConstraintValidator} it should be checked)
     * @param context to which possible {@link ConstraintViolation} will be
     * added
     * @param session hibernate session for building queries
     * 
     * @see UniquenessViolationFinder
     */
    public UniquenessViolationFinder(Entity entity, List<String> fields, ConstraintValidatorContext context,
            Session session) {
        this.entity = entity;
        this.fields = fields;

        this.fieldValues = convertBeanToMap(entity, fields);

        this.session = session;

        this.context = context;
        context.disableDefaultConstraintViolation();
    }

    /**
     * Determines whether the given entity violates the uniqueness of values
     * already stored in the database.
     * 
     * @return {@code true} if it doesn't violate the uniqueness, {@code false}
     * otherwise
     */
    public boolean isValid() {
        List<?> list = retrieveDuplicates();
        if (list.isEmpty()) {
            return true;
        }

        findViolatedFields(list, context);
        return false;
    }

    /**
     * Looks for entities with the same values as in the given bean, also
     * removing from the result the bean itself (by its id).<br>
     * <br>
     * 
     * For example, for {@link Component} and for {@link Branch} the equivalents
     * in hql would be
     * 
     * <pre>
     * from Component c where (c.name = :name or c.componentType = :type) and c.id != :id
     * from Branch b where (b.name = :name) and b.id != :id
     * </pre>
     * 
     * @return list of entities whose uniqueness is violated
     */
    private List<?> retrieveDuplicates() {
        Criteria criteria = session.createCriteria(entity.getClass());

        criteria.add(atLeastOneFieldViolated());
        criteria.add(Restrictions.ne("id", entity.getId()));

        return criteria.list();
    }

    /**
     * Builds 'or' criterion for the list of fields. The equivalent in hql, for
     * example, for two fields 'name' and 'type' would be
     * 
     * <pre>
     * where s.name = :name or s.type = :type
     * </pre>
     * 
     * Another examples for one and three fields respectively:
     * 
     * <pre>
     * where s.name = :name
     * where s.name = :name or s.type = :type or s.whatever = :whatever
     * </pre>
     * 
     * @return {@link Criterion} ready for using via {@link Criteria}
     */
    private Criterion atLeastOneFieldViolated() {
        Iterator<String> iterator = fields.iterator();
        String first = iterator.next();
        Criterion or = Restrictions.eq(first, fieldValues.get(first));

        while (iterator.hasNext()) {
            String next = iterator.next();
            Criterion eq = Restrictions.eq(next, fieldValues.get(next));
            or = Restrictions.or(or, eq);
        }

        return or;
    }

    /**
     * Goes through the given list of beans looking for values whose uniqueness
     * are violated. When finds one, adds the name of the violated fields using
     * {@link ConstraintViolationBuilder} obtained from
     * {@link ConstraintValidatorContext}, so afterwards (after the validation
     * process) it will be placed onto resulted set of
     * {@link ConstraintViolation} objects.
     * 
     * @param list recently retrieved list of beans
     * @param context to which constraint violation will be added
     */
    private void findViolatedFields(List<?> list, ConstraintValidatorContext context) {
        ConstraintViolationBuilder builder = context
                .buildConstraintViolationWithTemplate(UniqueConstraintValidator.MESSAGE);

        for (String field : fields) {
            Object actual = fieldValues.get(field);

            for (Object bean : list) {
                Object toCompare = extractField(bean, field);

                if (actual.equals(toCompare)) {
                    builder.addNode(field).addConstraintViolation();
                    break;
                }
            }
        }
    }

    // TODO: possible candidates for moving away (say, to the utils project)

    /**
     * Retrieves each field from the specified bean putting then into a
     * {@link Map}
     * 
     * @param bean bean object from which the properties will be extracted
     * @param fields property names to be extracted
     * @return extracted name-value pairs
     */
    public static Map<String, Object> convertBeanToMap(Object bean, List<String> fields) {
        Map<String, Object> values = new HashMap<String, Object>();

        for (String field : fields) {
            Object value = extractField(bean, field);
            values.put(field, value);
        }

        return values;
    }

    /**
     * Retrieves the value of the specified property of the specified bean
     * 
     * @param bean object from which the property will be extracted
     * @param field property name to be extracted
     * @return property value
     * @exception RuntimeException when something happens while extracting
     * property
     */
    public static Object extractField(Object bean, String field) {
        try {
            return PropertyUtils.getProperty(bean, field);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}