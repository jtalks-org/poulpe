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
package org.jtalks.poulpe.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.web.controller.topictype.TopicTypePresenter;
import org.jtalks.poulpe.web.controller.topictype.TopicTypePresenter.TopicTypeView;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Window;

/**
 * The class which is responsible for creating and closing application windows.
 * 
 * @author Dmitriy Sukharev
 * 
 */
public final class WindowManagerImpl implements WindowManager, ApplicationContextAware {

    /** The path to the web-page for adding / editing component. */
    private static final String EDIT_COMPONENT_URL = "/WEB-INF/pages/edit_component.zul";

    private ApplicationContext applicationContext;
    
    /** {@inheritDoc} */
    @Override
    public void showEditComponentWindow(long componentId, Object listener) {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("componentId", componentId);
        Window win = (Window) Executions.createComponents(EDIT_COMPONENT_URL, null, args);
        win.setAttribute("CLOSE_LISTENER", listener);        
        
        try {
            win.doModal();
        } catch (Exception e) {
            // It's extremely incredible that this exception will appear,
            // on the other hand it's bad to throw the Exception instance,
            // plus it complicates API. Therefore it's wrapped with
            // uncontrolled error.
            throw new AssertionError(e);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void closeWindow(Object object) {
        Window win = (Window) object;
        win.detach();

        Object attr = win.getAttribute("CLOSE_LISTENER");
        if (attr instanceof EventListener) {
            try {
                ((EventListener) attr).onEvent(null);
            } catch (Exception e) {
                // This has the same explanation as throwing AssertionError in
                // the showEditComponentWindow method.
                throw new AssertionError(e);
            }
        }
    }
       
    /** {@inheritDoc} */
    @Override
    public void openTopicTypeWindowForCreate(EditListener<TopicType> listener) {
        Window win = (Window) createComponent("topictype.zul");
        TopicTypePresenter  presenter = (TopicTypePresenter) getBean("topicTypePresenter", win);
        doModal(win);
        presenter.initializeForCreate((TopicTypeView) win, listener);
    }

    /** {@inheritDoc} */
    @Override
    public void openTopicTypeWindowForEdit(TopicType topicType, EditListener<TopicType> listener) {
        Window win = (Window) createComponent("topictype.zul");
        TopicTypePresenter  presenter = (TopicTypePresenter) getBean("topicTypePresenter", win);
        doModal(win);
        presenter.initializeForEdit((TopicTypeView) win, topicType, listener);
    }

    /** {@inheritDoc} */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    private Object getBean(String beanName, Component comp) {
        assertBeanDefined(beanName);
        assertBeanPrototype(beanName);
        Object presenter = applicationContext.getBean(beanName);
        Events.addEventListeners(comp, presenter);
        return presenter;
    }
    
    private Component createComponent(String pathToZulFile) {
        Component component = Executions.createComponents(pathToZulFile, null, null);
        Components.wireVariables(component, component);
        return component;
    }
    
    private void assertBeanDefined(String beanName) {
        Assert.isTrue(applicationContext.containsBean(beanName));
    }
    
    private void assertBeanPrototype(String beanName) {
        Assert.isTrue(applicationContext.isPrototype(beanName));
    }
    
    private void doModal(Window win) {
        try {
            win.doModal();
        } catch (Exception e) {
            // It's extremely incredible that this exception will appear,
            // on the other hand it's bad to throw the Exception instance,
            // plus it complicates API. Therefore it's wrapped with
            // uncontrolled error.
            throw new AssertionError(e);
        }
    }

}
