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
package org.jtalks.poulpe.service.transactional;

import java.util.List;
import java.util.Set;

import org.jtalks.common.service.transactional.AbstractTransactionalEntityService;
import org.jtalks.poulpe.model.dao.ComponentDao;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentBase;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.service.JCommuneNotifier;
import org.jtalks.poulpe.service.exceptions.EntityIsRemovedException;
import org.jtalks.poulpe.service.exceptions.EntityUniqueConstraintException;
import org.jtalks.poulpe.service.exceptions.JcommuneRespondedWithErrorException;
import org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException;
import org.jtalks.poulpe.service.exceptions.NoConnectionToJcommuneException;

/**
 * Transactional implementation of {@link ComponentService}. Transactions are provided by AOP.
 *
 * @author Pavel Vervenko
 * @author Alexey Grigorev
 * @author Vyacheslav Zhivaev
 */
public class TransactionalComponentService extends AbstractTransactionalEntityService<Component, ComponentDao>
        implements ComponentService {

    /**
     * JCommune notifier
     */
    private JCommuneNotifier jCommuneNotifier;

    /**
     * Creates new instance of the service
     *
     * @param dao               dao we use for Component
     */
    public TransactionalComponentService(ComponentDao dao) {
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Component> getAll() {
        return dao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteComponent(Component component)
        throws NoConnectionToJcommuneException, JcommuneRespondedWithErrorException,JcommuneUrlNotConfiguredException, EntityIsRemovedException {
        Component existInDbComponent = getByType(component.getComponentType());
        if (existInDbComponent == null || component.getId() != existInDbComponent.getId()) {
            throw new EntityIsRemovedException();
        }
        if (component instanceof Jcommune) {
            Jcommune jcommune = (Jcommune) component;
            jCommuneNotifier.notifyAboutComponentDelete(jcommune.getUrl());
        }
        dao.delete(component);
    }

    /**
     * {@inheritDoc}
     */
    @Deprecated
    @Override
    public void saveComponent(Component component) {
        dao.saveOrUpdate(component);
    }

    @Override
    public void reindexComponent(Jcommune jcommune)
        throws JcommuneRespondedWithErrorException, JcommuneUrlNotConfiguredException,NoConnectionToJcommuneException {
        String url = jcommune.getUrl();
        jCommuneNotifier.notifyAboutReindexComponent(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<ComponentType> getAvailableTypes() {
        return dao.getAvailableTypes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ComponentBase baseComponentFor(ComponentType componentType) {
        return dao.getBaseComponent(componentType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getByType(ComponentType type) {
        return dao.getByType(type);
    }

    /**
     * @return JCommune notifier
     */
    public JCommuneNotifier getjCommuneNotifier() {
        return jCommuneNotifier;
    }

    /**
     * @param jCommuneNotifier JCommune notifier
     */
    public void setjCommuneNotifier(JCommuneNotifier jCommuneNotifier) {
        this.jCommuneNotifier = jCommuneNotifier;
    }

    /** 
     * {@inheritDoc}
     * 
     * Before saving check if a component with the same type exists in database.
     * Throw exception if component already exists.  
     */
    @Override
    public void addComponent(Component component) throws EntityUniqueConstraintException {
        Component existInDbComponent = getByType(component.getComponentType());
        if (existInDbComponent != null && component.getId() != existInDbComponent.getId()) {
            throw new EntityUniqueConstraintException();
        }
        dao.saveOrUpdate(component);
    }

    /** 
     * {@inheritDoc}
     * 
     * Before updating check if the component exists in database.
     * Throw exception if component was removed by another user.  
     */
    @Override
    public void updateComponent(Component component) throws EntityIsRemovedException {
        Component existInDbComponent = getByType(component.getComponentType());
        if (existInDbComponent == null || component.getId() != existInDbComponent.getId()) {
            throw new EntityIsRemovedException();
        }
        dao.saveOrUpdate(component);
    }
}
