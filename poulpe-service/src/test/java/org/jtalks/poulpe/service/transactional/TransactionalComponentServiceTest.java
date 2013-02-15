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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import org.jtalks.poulpe.model.dao.ComponentDao;
import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.fixtures.TestFixtures;
import org.jtalks.poulpe.service.JCommuneNotifier;
import org.jtalks.poulpe.service.exceptions.EntityIsRemovedException;
import org.jtalks.poulpe.service.exceptions.JcommuneRespondedWithErrorException;
import org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException;
import org.jtalks.poulpe.service.exceptions.NoConnectionToJcommuneException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author Pavel Vervenko
 * @author Alexey Grigorev
 * @author Vyacheslav Zhivaev
 */
public class TransactionalComponentServiceTest {
    private TransactionalComponentService componentService;
    @Mock
    private ComponentDao componentDao;
    @Mock
    private JCommuneNotifier jCommuneNotifier;

    private Component component = TestFixtures.randomComponent();
    private Jcommune jcommune = spy((Jcommune) TestFixtures.component(ComponentType.FORUM));

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        componentService = new TransactionalComponentService(componentDao);
        componentService.setjCommuneNotifier(jCommuneNotifier);
    }

    @Test
    public void testAddComponent() throws Exception {
        when(componentService.getByType(component.getComponentType())).thenReturn(component);
        componentService.addComponent(component);
        verify(componentDao).saveOrUpdate(component);
    }

    @Test
    public void testGetAll() {
        componentService.getAll();
        verify(componentDao).getAll();
    }

    @Test
    public void testDeleteComponent() throws NoConnectionToJcommuneException,
            JcommuneUrlNotConfiguredException, JcommuneRespondedWithErrorException, EntityIsRemovedException {
        doNothing().when(jCommuneNotifier).notifyAboutComponentDelete(anyString());
        when(componentService.getByType(jcommune.getComponentType())).thenReturn(jcommune);
        doNothing().when(componentDao).delete(jcommune);
        componentService.deleteComponent(jcommune);
    }

    @Test
    public void getByType(){
        when(componentDao.getByType(jcommune.getComponentType())).thenReturn(jcommune);
        assertEquals(componentService.getByType(jcommune.getComponentType()),jcommune);
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
        componentService.setjCommuneNotifier(jCommuneNotifier);
        doReturn("").when(jcommune).getUrl();
        componentService.reindexComponent(jcommune);
        verify(componentService.getjCommuneNotifier()).notifyAboutReindexComponent(jcommune.getUrl());
    }
}
