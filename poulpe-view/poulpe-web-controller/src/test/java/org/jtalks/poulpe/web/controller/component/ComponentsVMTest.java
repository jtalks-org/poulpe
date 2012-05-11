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

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.DialogManager.Performable;
import org.jtalks.poulpe.web.controller.zkutils.BindUtilsWrapper;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Test for {@link org.jtalks.poulpe.web.controller.component.ComponentsVM}
 * 
 * @author Vermut
 *
 */
public class ComponentsVMTest {
	
	private static final boolean CAN_CREATE_NEW_COMPONENT = true;
	private static final boolean EDIT_WINDOW_VISIBLE = true;
	private static final Component EMPTY_SELECTION = null;
	
	@Mock
	private ComponentService componentService;
	@Mock
	private DialogManager dialogManager;
	@Mock
	private BindUtilsWrapper bindWrapper;
	@Captor
	private ArgumentCaptor<Performable> deleteCallbackCaptor;
	@InjectMocks
	private ComponentsVm viewModel;
	
	private List<Component> listOfTwoComponent;
	private List<Component> listOfTreeComponent;
	private Set<ComponentType> setOfAvailableComponentTypeWithOneMemeber;
	private Set<ComponentType> emptySetOfAvailableComponentTypes;
	
	@BeforeTest
	public void beforeTest() {
		viewModel = new ComponentsVm();
		MockitoAnnotations.initMocks(this);
		
		Component component1 = new Component();
		component1.setComponentType(ComponentType.ADMIN_PANEL);
		Component component2 = new Component();
		component2.setComponentType(ComponentType.FORUM);
		Component component3 = new Component();
		component3.setComponentType(ComponentType.ARTICLE);
		listOfTwoComponent = Arrays.asList(component1, component2);
		listOfTreeComponent = Arrays.asList(component1, component3);
		
		setOfAvailableComponentTypeWithOneMemeber = new HashSet();
		setOfAvailableComponentTypeWithOneMemeber.add(ComponentType.ARTICLE);
		emptySetOfAvailableComponentTypes = new HashSet();
	}
	
	@Test(enabled=false)
	public void configureComponent() {
		/*
		 * TODO needs to wrapper for Executions or changes in window opening
		 * mechanism, or may be power mock should be used.
		 */
		throw new RuntimeException("Test not implemented");
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
		Performable callback = deleteCallbackCaptor.getValue();
		
		// Nothing should be happen before user confirms deletion. 
		verify(componentService, never()).deleteComponent(selected);
		
		// confirm deletion
		prepareMocksWithTwoExistedComponentsAndAbleToCreateNewComponent();
		callback.execute();
		verify(componentService).deleteComponent(selected);
		assertNull(viewModel.getSelected());
		verify(componentService).getAvailableTypes();
		verify(bindWrapper).postNotifyChange(null, null, viewModel, ComponentsVm.SELECTED);
		verify(bindWrapper).postNotifyChange(null, null, viewModel, ComponentsVm.COMPONENT_LIST);
		verify(bindWrapper).postNotifyChange(null, null, viewModel, ComponentsVm.CAN_CREATE_NEW_COMPPONENT);
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
	public void editComponent() {
		prepareMocksWithTwoExistedComponentsAndAbleToCreateNewComponent();
		viewModel.init();
		
		Component selected = viewModel.getComponentList().get(0);
		viewModel.editComponent(selected);
		
		// type of editable component should be available in comboboc
		HashSet expectedAvailableComponentType = new HashSet(setOfAvailableComponentTypeWithOneMemeber);
		expectedAvailableComponentType.add(selected.getComponentType());
		
		verifyStateOfViewModel(CAN_CREATE_NEW_COMPONENT, EDIT_WINDOW_VISIBLE, listOfTwoComponent,
				expectedAvailableComponentType, selected);
	}

	@Test
	public void newComponent() {
		prepareMocksWithTwoExistedComponentsAndAbleToCreateNewComponent();
		viewModel.init();
		
		Component randomExistedComponent = viewModel.getComponentList().get(0);
		viewModel.setSelected(randomExistedComponent);
		viewModel.showAddComponentDialog();
		
		assertFalse("selected component should be replaced when <new button> was pressed.", viewModel.getSelected().equals(randomExistedComponent));
		assertTrue(viewModel.isEditWindowVisible());
	}

	@Test
	public void saveComponent() {
		prepareMocksWithTwoExistedComponentsAndAbleToCreateNewComponent();
		viewModel.init();
		viewModel.showAddComponentDialog();
		Component creatable = viewModel.getSelected();
		prepareMocksWithThreeExistedComponentsAndUnableToCreateNewComponent();
		viewModel.saveComponent();
		
		verify(componentService).saveComponent(creatable);
		verifyStateOfViewModel(!CAN_CREATE_NEW_COMPONENT, !EDIT_WINDOW_VISIBLE, listOfTreeComponent,
				emptySetOfAvailableComponentTypes, creatable);
	}
	
	private void verifyStateOfViewModel(boolean canCreateNewComponent, boolean isEditWindowVisible, 
			List<Component> allComponents, Collection<ComponentType> availableTypes, Component selected) {
		assertEquals(canCreateNewComponent, viewModel.isCanCreateNewComponent());
		assertEquals(isEditWindowVisible, viewModel.isEditWindowVisible());
		assertTrue(viewModel.getAvailableComponentTypes().containsAll(availableTypes));
		assertTrue(availableTypes.containsAll(viewModel.getAvailableComponentTypes()));
		assertTrue(viewModel.getComponentList().containsAll(allComponents));
		assertTrue(allComponents.containsAll(viewModel.getComponentList()));
		assertEquals(selected, viewModel.getSelected());
	}
	
	private void prepareMocksWithTwoExistedComponentsAndAbleToCreateNewComponent() {
		reset(componentService);
		when(componentService.getAvailableTypes()).thenReturn(new HashSet<ComponentType>(setOfAvailableComponentTypeWithOneMemeber));		
		when(componentService.getAll()).thenReturn(new ArrayList<Component>(listOfTwoComponent));
	}
	
	private void prepareMocksWithThreeExistedComponentsAndUnableToCreateNewComponent() {
		reset(componentService);
		when(componentService.getAvailableTypes()).thenReturn(new HashSet<ComponentType>(emptySetOfAvailableComponentTypes));
		when(componentService.getAll()).thenReturn(new ArrayList<Component>(listOfTreeComponent));
	}

}
