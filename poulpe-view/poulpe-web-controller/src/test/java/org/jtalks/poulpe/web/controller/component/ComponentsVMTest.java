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
package org.jtalks.poulpe.web.controller.component;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.*;

import java.util.*;

import org.jtalks.poulpe.model.entity.*;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.test.fixtures.TestFixtures;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.zkutils.BindUtilsWrapper;
import org.mockito.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.Sets;

/**
 * Test for {@link org.jtalks.poulpe.web.controller.component.ComponentsVm}
 * 
 * @author Vermut
 * 
 */
public class ComponentsVMTest {

    private static final boolean CAN_CREATE_NEW_COMPONENT = true;
    private static final boolean EDIT_WINDOW_VISIBLE = true;
    private static final Component EMPTY_SELECTION = null;

    @Mock WindowManager windowManager;
    @Mock ComponentService componentService;
    @Mock SelectedEntity<Component> selectedEntity;
    @Mock DialogManager dialogManager;
    @Mock BindUtilsWrapper bindWrapper;
    @Captor ArgumentCaptor<DialogManager.Performable> deleteCallbackCaptor;
    
    @InjectMocks ComponentsVm viewModel;

    private List<Component> listOfTwoComponent;
    private List<Component> listOfTreeComponent;
    private Set<ComponentType> setOfAvailableComponentTypeWithOneMemeber;

    @BeforeTest
    public void beforeTest() {
        viewModel = spy(new ComponentsVm());
        MockitoAnnotations.initMocks(this);

        Component component1 = TestFixtures.component(ComponentType.ADMIN_PANEL);
        Component component2 = TestFixtures.component(ComponentType.FORUM);
        Component component3 = TestFixtures.component(ComponentType.ARTICLE);
        listOfTwoComponent = Arrays.asList(component1, component2);
        listOfTreeComponent = Arrays.asList(component1, component3);

        setOfAvailableComponentTypeWithOneMemeber = Sets.newHashSet(ComponentType.ARTICLE);
    }

    @Test
    public void deleteComponent() {
        prepareMocksWithThreeExistedComponentsAndUnableToCreateNewComponent();
        viewModel.init();

        Component selected = viewModel.getComponentList().get(0);
        selected.setName("testDelete");
        viewModel.setSelected(selected);
        viewModel.deleteComponent();
        verify(dialogManager).confirmDeletion(eq("testDelete"), deleteCallbackCaptor.capture());
        DialogManager.Performable callback = deleteCallbackCaptor.getValue();

        // Nothing should be happen before user confirms deletion.
        verify(componentService, never()).deleteComponent(selected);

        // confirm deletion
        prepareMocksWithTwoExistedComponentsAndAbleToCreateNewComponent();
        callback.execute();
        verify(componentService).deleteComponent(selected);
        assertNull(viewModel.getSelected());
        verify(componentService).getAvailableTypes();
        verify(bindWrapper).postNotifyChange(viewModel, ComponentsVm.SELECTED, ComponentsVm.COMPONENT_LIST, ComponentsVm.CAN_CREATE_NEW_COMPPONENT);
        verifyStateOfViewModel(CAN_CREATE_NEW_COMPONENT, !EDIT_WINDOW_VISIBLE, listOfTwoComponent,
                setOfAvailableComponentTypeWithOneMemeber, EMPTY_SELECTION);
    }

    @Test
    public void cancelEdit() {
        prepareMocksWithTwoExistedComponentsAndAbleToCreateNewComponent();
        viewModel.init();
        viewModel.showAddComponentDialog();
        viewModel.cancelEdit();

        verifyStateOfViewModel(CAN_CREATE_NEW_COMPONENT, !EDIT_WINDOW_VISIBLE, listOfTwoComponent,
                setOfAvailableComponentTypeWithOneMemeber, EMPTY_SELECTION);
    }

    @Test
    public void showAddComponentDialog() {
        viewModel.showAddComponentDialog();
        assertTrue(viewModel.isAddNewComponentWindowVisible());
    }

    @Test
    public void createJcommune() {
        givenBaseComponents();
        
        setComponentAttributes();
        viewModel.setSelected(forumComponent());
        viewModel.setComponentType(viewModel.getSelected().getComponentType());
        viewModel.createComponent();
        verify(componentService).saveComponent(any(Jcommune.class));
        assertTrue(viewModel.getComponentName() == null);
        assertTrue(viewModel.getComponentDescription() == null);
        assertTrue(viewModel.getSelected() == null);

        setComponentAttributes();
        viewModel.setSelected(poulpeComponent());
        viewModel.setComponentType(viewModel.getSelected().getComponentType());
        viewModel.createComponent();
        verify(componentService, times(2)).saveComponent(any(Poulpe.class));
        assertTrue(viewModel.getComponentName() == null);
        assertTrue(viewModel.getComponentDescription() == null);
        assertTrue(viewModel.getSelected() == null);

        setComponentAttributes();
        viewModel.setSelected(articleComponent());
        viewModel.setComponentType(viewModel.getSelected().getComponentType());
        viewModel.createComponent();
        verify(componentService, times(3)).saveComponent(any(Component.class));
        assertTrue(viewModel.getComponentName() == null);
        assertTrue(viewModel.getComponentDescription() == null);
        assertTrue(viewModel.getSelected() == null);
    }

    private void givenBaseComponents() {
        when(componentService.baseComponentFor(ComponentType.FORUM)).thenReturn(new BaseComponent(ComponentType.FORUM));
        when(componentService.baseComponentFor(ComponentType.ADMIN_PANEL)).thenReturn(new BaseComponent(ComponentType.ADMIN_PANEL));
        when(componentService.baseComponentFor(ComponentType.ARTICLE)).thenReturn(new BaseComponent(ComponentType.ARTICLE));
    }

    @Test
    public void clearComponent() {
        setComponentAttributes();
        viewModel.clearComponent();
        assertTrue(viewModel.getComponentName() == null);
        assertTrue(viewModel.getComponentDescription() == null);
        assertTrue(viewModel.getComponentType() == null);
        assertTrue(viewModel.getSelected() == null);
    }

    private void verifyStateOfViewModel(boolean canCreateNewComponent, boolean isEditWindowVisible,
            List<Component> allComponents, Collection<ComponentType> availableTypes, Component selected) {
        assertEquals(canCreateNewComponent, viewModel.isCanCreateNewComponent());
        assertEquals(isEditWindowVisible, viewModel.isAddNewComponentWindowVisible());
        assertTrue(viewModel.getAvailableComponentTypes().containsAll(availableTypes));
        assertTrue(availableTypes.containsAll(viewModel.getAvailableComponentTypes()));
        assertTrue(viewModel.getComponentList().containsAll(allComponents));
        assertTrue(allComponents.containsAll(viewModel.getComponentList()));
        assertEquals(selected, viewModel.getSelected());
    }

    private void prepareMocksWithTwoExistedComponentsAndAbleToCreateNewComponent() {
        reset(componentService);
        when(componentService.getAvailableTypes()).thenReturn(
                new HashSet<ComponentType>(setOfAvailableComponentTypeWithOneMemeber));
        when(componentService.getAll()).thenReturn(new ArrayList<Component>(listOfTwoComponent));
    }

    private void prepareMocksWithThreeExistedComponentsAndUnableToCreateNewComponent() {
        reset(componentService);
        Set<ComponentType> emptySet = Collections.emptySet();
        when(componentService.getAvailableTypes()).thenReturn(emptySet);
        when(componentService.getAll()).thenReturn(new ArrayList<Component>(listOfTreeComponent));
    }

    private Component forumComponent() {
        return TestFixtures.component(ComponentType.FORUM);
    }

    private Component poulpeComponent() {
        return TestFixtures.component(ComponentType.ADMIN_PANEL);
    }

    private Component articleComponent() {
        return TestFixtures.component(ComponentType.ARTICLE);
    }

    private void setComponentAttributes() {
        viewModel.setComponentName("name");
        viewModel.setComponentDescription("desc");
        viewModel.setComponentType(ComponentType.FORUM);
    }
}
