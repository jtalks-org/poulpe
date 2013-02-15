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
package org.jtalks.poulpe.web.controller.component;

import org.jtalks.common.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentBase;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.model.fixtures.TestFixtures;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.component.dialogs.AddComponentVm;
import org.jtalks.poulpe.web.controller.component.dialogs.DeleteComponentDialog;
import org.jtalks.poulpe.web.controller.component.dialogs.EditComponentVm;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * @author Vermut
 * @author Alexey Grigorev
 * @author Kazancev Lenonid
 */
public class ComponentsVmTest {
    private static final String REINDEX_STARTED_FIELD = "showReindexStartedNotification";
    // sut
    ComponentsVm componentsVm;
    @Mock
    WindowManager windowManager;
    @Mock
    ComponentService componentService;
    SelectedEntity<Component> selectedEntity;

    @BeforeMethod
    public void beforeTest() {
        MockitoAnnotations.initMocks(this);
        selectedEntity = new SelectedEntity<Component>();
        componentsVm = new ComponentsVm(componentService, windowManager, selectedEntity, new ComponentList());
    }

    @Test
    public void getPoulpe() {
        componentsVm.getPoulpe();
        verify(componentService).getByType(ComponentType.ADMIN_PANEL);
    }

    @Test
    public void getJcommune() {
        componentsVm.getJcommune();
        verify(componentService).getByType(ComponentType.FORUM);
    }

    @Test
    public void reindexComponent() throws Exception {
        ComponentBase componentBase = new ComponentBase(ComponentType.FORUM);
        Jcommune jcommune = (Jcommune) componentBase.newComponent("Name", "Description");
        componentsVm.setSelected(jcommune);
        componentsVm.reindexComponent();
        verify(componentService).reindexComponent(jcommune);
    }

    @Test
    public void deleteComponent() throws NoSuchFieldException, IllegalAccessException {
        //given
        Component selected = givenSelectedComponent();
        DeleteComponentDialog deleteComponentDialog = mock(DeleteComponentDialog.class);
        setField("deleteComponentDialog", deleteComponentDialog);

        componentsVm.deleteComponent();
        verify(deleteComponentDialog).confirmDeletion(selected);
    }

    private Component givenSelectedComponent() {
        Component selected = TestFixtures.randomComponent();
        componentsVm.setSelected(selected);
        return selected;
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void deleteComponent_componentMustBeSelected() {
        componentsVm.deleteComponent();
    }

    @Test
    public void addNewPoulpe() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        ComponentBase componentBase = new ComponentBase(ComponentType.ADMIN_PANEL);
        doReturn(componentBase).when(componentService).baseComponentFor(ComponentType.ADMIN_PANEL);
        componentsVm.addNewPoulpe();

        verify(windowManager).open(AddComponentVm.ADD_COMPONENT_LOCATION);
    }

    @Test
    public void addNewJcommune() {
        ComponentBase componentBase = new ComponentBase(ComponentType.FORUM);
        doReturn(componentBase).when(componentService).baseComponentFor(ComponentType.FORUM);
        componentsVm.addNewJcommune();

        verify(windowManager).open(AddComponentVm.ADD_COMPONENT_LOCATION);
    }

    @Test
    public void configureComponent() {
        componentsVm.configureComponent();
        verify(windowManager).open(EditComponentVm.EDIT_COMPONENT_LOCATION);
    }

    @Test
    public void configureComponent_selectedComponentSet() {
        Component selected = givenSelectedComponent();
        componentsVm.configureComponent();
        assertEquals(selectedEntity.getEntity(), selected);
    }

    @Test
    public void isJcommuneAvailable() {
        componentsVm.isJcommuneAvailable();
        verify(componentService).getAvailableTypes();
    }

    @Test
    public void show() {
        ComponentsVm.show(windowManager);
        verify(windowManager).open(ComponentsVm.COMPONENTS_PAGE_LOCATION);
    }

    @Test
    public void isShowNotConfiguredNotification() throws NoSuchFieldException, IllegalAccessException {
        setShowReindexStartedNotification(true);
        boolean value = componentsVm.isShowReindexStartedNotification();

        assertTrue(value);
        assertFalse(componentsVm.isShowReindexStartedNotification());
    }

    private void setShowReindexStartedNotification(boolean value) throws NoSuchFieldException, IllegalAccessException {
        setField(REINDEX_STARTED_FIELD, value);
    }

    private void setField(String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Class clazz = componentsVm.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(componentsVm, value);
    }

}
