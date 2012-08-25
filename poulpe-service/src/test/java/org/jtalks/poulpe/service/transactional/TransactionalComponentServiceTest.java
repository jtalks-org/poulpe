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

import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationError;
import org.jtalks.common.validation.ValidationException;
import org.jtalks.poulpe.logic.PermissionManager;
import org.jtalks.poulpe.model.dao.ComponentDao;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.service.JcommuneHttpNotifier;
import org.jtalks.poulpe.service.exceptions.JcommuneRespondedWithErrorException;
import org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException;
import org.jtalks.poulpe.service.exceptions.NoConnectionToJcommuneException;
import org.jtalks.poulpe.test.fixtures.TestFixtures;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Pavel Vervenko
 * @author Alexey Grigorev
 * @author Vyacheslav Zhivaev
 */
public class TransactionalComponentServiceTest {
    private TransactionalComponentService componentService;

    @Mock
    ComponentDao componentDao;
    @Mock
    EntityValidator validator;
    @Mock
    PermissionManager permissionManager;
    @Mock
    JcommuneHttpNotifier jcommuneHttpNotifier;

    Component component = TestFixtures.randomComponent();
    Jcommune jcommune = spy((Jcommune) TestFixtures.component(ComponentType.FORUM));

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        componentService = new TransactionalComponentService(componentDao, validator);
    }

    @Test
    public void testSaveComponent() {
        componentService.saveComponent(component);

        verify(validator).throwOnValidationFailure(component);
        verify(componentDao).saveOrUpdate(component);
    }

    @Test
    public void testGetAll() {
        componentService.getAll();
        verify(componentDao).getAll();
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testSaveComponentException() {
        givenConstraintsViolations();
        componentService.saveComponent(component);
    }

    private void givenConstraintsViolations() {
        Set<ValidationError> empty = Collections.emptySet();
        doThrow(new ValidationException(empty)).when(validator).throwOnValidationFailure(any(Component.class));
    }

    @Test
    public void testDeleteComponent() throws NoConnectionToJcommuneException, IOException {
        //componentService.deleteComponent(component);
        //verify(componentDao).delete(component);
    }

    @Test
    public void testGetAvailableTypes() {
        componentService.getAvailableTypes();
        verify(componentDao).getAvailableTypes();
    }

    @Test
    public void baseComponentFor() {
        ComponentType componentType = TestFixtures.randomComponentType();
        componentService.baseComponentFor(componentType);
        verify(componentDao).getBaseComponent(componentType);

    }

    @Test
    public void reindexComponent() throws JcommuneRespondedWithErrorException, JcommuneUrlNotConfiguredException,
            NoConnectionToJcommuneException {
        componentService.setjCommuneNotifier(jcommuneHttpNotifier);
        doReturn("").when(jcommune).getUrl();
        componentService.reindexComponent(jcommune);
        verify(jcommuneHttpNotifier).notifyAboutReindexComponent(jcommune.getUrl());
    }
}
