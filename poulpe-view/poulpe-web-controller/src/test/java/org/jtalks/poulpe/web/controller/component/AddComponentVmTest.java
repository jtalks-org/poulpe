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

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jtalks.poulpe.model.entity.BaseComponent;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.test.fixtures.TestFixtures;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Alexey Grigorev
 */
public class AddComponentVmTest {

    AddComponentVm addComponentVm;

    @Mock ComponentService componentService;
    @Mock WindowManager windowManager;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
        addComponentVm = new AddComponentVm(componentService, windowManager);
    }

    @Test
    public void cancelEdit() {
        addComponentVm.cancelEdit();
        verify(windowManager).open(AddComponentVm.COMPONENTS_WINDOW);
    }

    @Test
    public void createComponent() {
        givenBaseComponents();
        Component expected = TestFixtures.randomComponent();

        addComponentVm.createComponent(expected.getName(), expected.getDescription(), expected.getComponentType());

        verify(componentService).saveComponent(fieldsEqualTo(expected));
    }

    private void givenBaseComponents() {
        when(componentService.baseComponentFor(ComponentType.FORUM)).thenReturn(
                new BaseComponent(ComponentType.FORUM));
        when(componentService.baseComponentFor(ComponentType.ADMIN_PANEL)).thenReturn(
                new BaseComponent(ComponentType.ADMIN_PANEL));
        when(componentService.baseComponentFor(ComponentType.ARTICLE)).thenReturn(
                new BaseComponent(ComponentType.ARTICLE));
    }
    
    @Test
    public void createComponent_widowClosed() {
        givenBaseComponents();
        
        Component expected = TestFixtures.randomComponent();
        addComponentVm.createComponent(expected.getName(), expected.getDescription(), expected.getComponentType());
        
        verify(windowManager).open(AddComponentVm.COMPONENTS_WINDOW);
    }

    @Test
    public void getAvailableComponentTypes() {
        addComponentVm.getAvailableComponentTypes();
        verify(componentService).getAvailableTypes();
    }
    
    @Test
    public void openWindowForAdding() {
        AddComponentVm.openWindowForAdding(windowManager);
        verify(windowManager).open(AddComponentVm.ADD_COMPONENT_LOCATION);
    }

    private static Component fieldsEqualTo(final Component expected) {
        return argThat(new BaseMatcher<Component>() {
            @Override
            public boolean matches(Object o) {
                return EqualsBuilder.reflectionEquals(o, expected, "uuid");
            }

            @Override
            public void describeTo(Description d) {
                d.appendText(expected.toString());
            }
        });
    }
}
