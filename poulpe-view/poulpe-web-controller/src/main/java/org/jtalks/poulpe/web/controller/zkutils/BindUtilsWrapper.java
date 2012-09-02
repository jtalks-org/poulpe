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
package org.jtalks.poulpe.web.controller.zkutils;

import org.springframework.beans.BeanUtils;
import org.zkoss.bind.BindUtils;

import java.beans.PropertyDescriptor;

/**
 * This class represent the wrapper around {@link org.zkoss.bind.BindUtils}. BindUtils is utility which needs to help
 * developer to use zk bind, but because BindUtils contains static methods, it do our code hard to testing. This wrapper
 * needs to testing, it can be easy mocked with mockito or other mock frameworks.
 *
 * @author Vermut
 * @author Alexey Grigorev
 */
public class BindUtilsWrapper {

    /**
     * Post a notify change to corresponding event queue to notify a bean's property changing
     *
     * @param bean     the bean instance
     * @param property the property name of bean
     * @see org.zkoss.bind.BindUtils#postNotifyChange(String, String, Object, String)
     */
    public void postNotifyChange(Object bean, String property) {
        /*
         * @param queueName the queue name, null for default queue name
         * @param queueScope the queue scope, null for default queue scope (i.e. {@link EventQueues#DESKTOP})
         */
        BindUtils.postNotifyChange(null, null, bean, property);
    }

    /**
     * Notifies about changes of passed property names to vm component
     *
     * @param vm         zk view-model object
     * @param properties needed to be notified
     * @see #postNotifyChange(Object, String)
     */
    public void postNotifyChange(Object vm, String... properties) {
        for (String property : properties) {
            postNotifyChange(vm, property);
        }
    }

    /**
     * Notifies the VM about all the properties inside were changed so that ZK can re-read them and update its UI.
     *
     * @param vm a visual model to iterate through all the properties and trigger ZK Binder notifications
     */
    public void notifyAllPropsChanged(Object vm) {
        PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(vm.getClass());
        for (PropertyDescriptor prop : props) {
            postNotifyChange(vm, prop.getName());
        }
    }
}
