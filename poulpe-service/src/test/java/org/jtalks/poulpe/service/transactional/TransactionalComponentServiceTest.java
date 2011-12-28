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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;

import org.jtalks.poulpe.model.dao.ComponentDao;
import org.jtalks.poulpe.model.dao.ComponentDao.ComponentDuplicateField;
import org.jtalks.poulpe.model.dao.DuplicatedField;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.service.exceptions.NotUniqueFieldsException;
import org.jtalks.poulpe.validation.EntityValidator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Pavel Vervenko
 * @author Alexey Grigorev
 */
public class TransactionalComponentServiceTest {

    private ComponentDao componentDao;
    private TransactionalComponentService conponentService;

    Component component = new Component();
    Set<DuplicatedField> empty = Collections.emptySet();
    Set<DuplicatedField> name = Collections.<DuplicatedField> singleton(ComponentDuplicateField.NAME);
    private EntityValidator validator;

    @BeforeMethod
    public void setUp() throws Exception {
        componentDao = mock(ComponentDao.class);
        validator = mock(EntityValidator.class);
        conponentService = new TransactionalComponentService(componentDao, validator);
    }

    @Test
    public void testGetAll() {
        conponentService.getAll();
        verify(componentDao).getAll();
    }

    @Test
    public void testSaveComponent() throws NotUniqueFieldsException {
        givenNoDuplicatedFields();
        conponentService.saveComponentCheckUniqueness(component);
        verify(componentDao).saveOrUpdate(component);
    }

    private void givenNoDuplicatedFields() {
        when(componentDao.getDuplicateFieldsFor(component)).thenReturn(empty);
    }

    @Test(expectedExceptions = NotUniqueFieldsException.class)
    public void testSaveComponentException() throws NotUniqueFieldsException {
        givenNameFieldDuplicated();
        conponentService.saveComponentCheckUniqueness(component);
    }

    private void givenNameFieldDuplicated() {
        when(componentDao.getDuplicateFieldsFor(component)).thenReturn(name);
    }

    @Test
    public void testDeleteComponent() {
        conponentService.deleteComponent(component);
        verify(componentDao).delete(component.getId());
    }

    @Test
    void testGetAvailableTypes() {
        conponentService.getAvailableTypes();
        verify(componentDao).getAvailableTypes();
    }
}
