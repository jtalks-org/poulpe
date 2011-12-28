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
package org.jtalks.poulpe.validation.config;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Cannot be used because of Chinese programmers who throw exceptions from their
 * zk beans making a lot of shitty warnings.<br>
 * <br>
 * 
 * The configuration is supposed to be like this:
 * 
 * <pre>
 *   <bean id="validator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean">
 *     <property name="constraintValidatorFactory" ref="validatorFactory" />
 *   </bean>
 *   
 *   <bean id="validatorFactory" class="org.jtalks.poulpe.validation.ContextAwareConstraintValidatorFactory" />
 *   
 *   <bean id="uniqueConstraintValidator" class="org.jtalks.poulpe.validation.UniqueConstraintValidator">
 *       <property name="uniquenessViolatorsRetriever" ref="uniquenessViolatorsRetriever" />
 *   </bean>
 *   
 *   <bean id="uniquenessViolatorsRetriever" class="org.jtalks.poulpe.validation.UniquenessViolatorsRetriever">
 *     <property name="sessionFactory" ref="sessionFactory" />
 *   </bean>
 * </pre>
 * 
 * But due to Chinese programmers I will have to pass validators explicitly in
 * the context, making impossible using prototypes when needed.
 * 
 * @author Alexey Grigorev
 */
public class ContextAwareConstraintValidatorFactory implements ConstraintValidatorFactory, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        return applicationContext.getBean(key);
    }
}
