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

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.test.fixtures.TestFixtures;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
* @author Kazancev Leonid
* @author Alexey Grigorev
*/
public class EditComponentVmTest {

    // sut
    EditComponentVm editComponentVm;
    
    // dependencies
    @Mock ComponentService componentService;
    @Mock WindowManager windowManager;

    Component component = TestFixtures.randomComponent();

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        SelectedEntity<Component> selectedEntity = new SelectedEntity<Component>();
        selectedEntity.setEntity(component);
        
        editComponentVm = new EditComponentVm(componentService, selectedEntity);
        editComponentVm.setWindowManager(windowManager);
    }

    @Test
    public void save_componentSaved() {
        editComponentVm.save();
        verify(componentService).saveComponent(component);
    }
    
    @Test
    public void save_editWindowClosed() {
        editComponentVm.save();
        verify(windowManager).open(ComponentsVm.COMPONENTS_PAGE_LOCATION);
    }
    
    @Test
    public void cancel_componentIsNotSaved() {
        editComponentVm.cancel();
        verify(componentService, never()).saveComponent(component);
    }
    
    @Test
    public void cancel_editWindowClosed() {
        editComponentVm.cancel();
        verify(windowManager).open(ComponentsVm.COMPONENTS_PAGE_LOCATION);
    }
    
    @Test
    public void getComponent_componentFromSelectedEntityUsed() {
        assertSame(component, editComponentVm.getComponent());
    }
    
    @Test
    public void openWindowForEdit() {
        EditComponentVm.openWindowForEdit(windowManager);
        verify(windowManager).open(EditComponentVm.EDIT_COMPONENT_LOCATION);
    }
}
