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

import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.common.model.permissions.GeneralPermission;
import org.jtalks.common.service.transactional.AbstractTransactionalEntityService;
import org.jtalks.common.validation.EntityValidator;
import org.jtalks.poulpe.logic.PermissionManager;
import org.jtalks.poulpe.model.dao.ComponentDao;
import org.jtalks.poulpe.model.dto.PermissionChanges;
import org.jtalks.poulpe.model.dto.PermissionsMap;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.service.PropertyLoader;

import java.util.List;
import java.util.Set;

/**
 * Transactional implementation of {@link ComponentService}. Transactions are provided by AOP.
 * 
 * @author Pavel Vervenko
 * @author Alexey Grigorev
 * @author Vyacheslav Zhivaev
 */
public class TransactionalComponentService extends AbstractTransactionalEntityService<Component, ComponentDao>
        implements ComponentService {
    private final PermissionManager permissionManager;
    private final EntityValidator validator;
    private PropertyLoader propertyLoader;

    /**
     * Creates new instance of the service
     * 
     * @param dao dao we use for Component
     * @param permissionManager the permission manager, instance of {@link PermissionManager}
     * @param validator used to validate entites
     */
    public TransactionalComponentService(ComponentDao dao, PermissionManager permissionManager,
            EntityValidator validator) {
        this.dao = dao;
        this.permissionManager = permissionManager;
        this.validator = validator;
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
    public void deleteComponent(Component component) {
        dao.delete(component);
    }

    /** {@inheritDoc} */
    @Override
    public void saveComponent(Component component) {
        validator.throwOnValidationFailure(component);

        if (component.getId() == 0) {
            propertyLoader.loadDefaults(component);
        }
        dao.saveOrUpdate(component);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<ComponentType> getAvailableTypes() {
        return dao.getAvailableTypes();
    }

    /**
     * Sets property loader. See {@link PropertyLoader}
     * 
     * @param propertyLoader property loader to set
     */
    public void setPropertyLoader(PropertyLoader propertyLoader) {
        this.propertyLoader = propertyLoader;
    }

    /**
     * Gets currently used property loader. See {@link PropertyLoader}
     * 
     * @return property loader
     */
    public PropertyLoader getPropertyLoader() {
        return propertyLoader;
    }

    /** {@inheritDoc} */
    @Override
    public Component getByType(ComponentType type) {
        return dao.getByType(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PermissionsMap<GeneralPermission> getPermissionsMapFor(Component component) {
        return permissionManager.getPermissionsMapFor(component);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeGrants(Component component, PermissionChanges changes) {
        permissionManager.changeGrants(component, changes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeRestrictions(Component component, PermissionChanges changes) {
        permissionManager.changeRestrictions(component, changes);
    }

}
