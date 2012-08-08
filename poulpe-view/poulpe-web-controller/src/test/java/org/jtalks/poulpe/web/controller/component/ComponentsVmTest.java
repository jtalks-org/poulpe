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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

import java.util.Collections;
import java.util.Set;

import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.test.fixtures.TestFixtures;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.DialogManager.Performable;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.zkutils.BindUtilsWrapper;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Sets;

/**
 * @author Vermut
 * @author Alexey Grigorev
 */
public class ComponentsVmTest {

    // sut
    ComponentsVm componentsVm;
    
    // dependencies
    @Mock WindowManager windowManager;
    @Mock ComponentService componentService;
    @Mock DialogManager dialogManager;
    @Mock BindUtilsWrapper bindWrapper;
    SelectedEntity<Component> selectedEntity;
    
    @Captor ArgumentCaptor<DialogManager.Performable> deleteCallbackCaptor;

    @BeforeMethod
    public void beforeTest() {
        MockitoAnnotations.initMocks(this);
        selectedEntity = new SelectedEntity<Component>();
        componentsVm = new ComponentsVm(componentService, dialogManager, windowManager, selectedEntity);
        componentsVm.setBindWrapper(bindWrapper);
    }
    
    @Test
    public void getComponents() {
        componentsVm.getComponents();
        verify(componentService).getAll();
    }
    
    @Test
    public void deleteComponent() {
        Component selected = givenSelectedComponent();
        componentsVm.deleteComponent();
        verify(dialogManager).confirmDeletion(eq(selected.getName()), any(DialogManager.Performable.class));
    }

    private Component givenSelectedComponent() {
        Component selected = TestFixtures.randomComponent();
        componentsVm.setSelected(selected);
        return selected;
    }
    
    @Test
    public void deleteComponent_componentDeletedAfterConfirmation() {
        Component selected = givenUserConfirmedDeletion();
        verify(componentService).deleteComponent(selected);
    }

    private Component givenUserConfirmedDeletion() {
        Component selected = givenSelectedComponent();
        
        componentsVm.deleteComponent();
        captureDeletePerfomable(selected).execute();
        
        return selected;
    }

    private Performable captureDeletePerfomable(Component selected) {
        verify(dialogManager).confirmDeletion(eq(selected.getName()), deleteCallbackCaptor.capture());
        return deleteCallbackCaptor.getValue();
    }
    
    @Test
    public void deleteComponent_notifyChange() {
        givenUserConfirmedDeletion();
        verify(bindWrapper).postNotifyChange(componentsVm, ComponentsVm.SELECTED, ComponentsVm.COMPONENTS,
                ComponentsVm.CAN_CREATE_NEW_COMPONENT);
    }
    
    @Test(expectedExceptions = IllegalStateException.class)
    public void deleteComponent_componentMustBeSelected() {
        componentsVm.deleteComponent();
    }
    
    @Test
    public void addNewComponent() {
        componentsVm.addNewComponent();
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
    public void isAbleToCreateNewComponent_able() {
        givenAvailableTypes();
        boolean actual = componentsVm.isAbleToCreateNewComponent();
        assertTrue(actual);
    }

    private void givenAvailableTypes() {
        Set<ComponentType> all = Sets.newHashSet(ComponentType.values());
        when(componentService.getAvailableTypes()).thenReturn(all);
    }
    
    @Test
    public void isAbleToCreateNewComponent_notAble() {
        givenNoAvailableTypes();
        boolean actual = componentsVm.isAbleToCreateNewComponent();
        assertFalse(actual);
    }
    
    private void givenNoAvailableTypes() {
        Set<ComponentType> empty = Collections.emptySet();
        when(componentService.getAvailableTypes()).thenReturn(empty);
    }
}
