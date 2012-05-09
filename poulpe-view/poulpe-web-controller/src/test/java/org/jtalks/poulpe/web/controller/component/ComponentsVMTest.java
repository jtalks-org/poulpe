package org.jtalks.poulpe.web.controller.component;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.DialogManager.Performable;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.zkutils.BindUtilsWrapper;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test for {@link org.jtalks.poulpe.web.controller.component.ComponentsVM}
 * 
 * @author Vermut
 *
 */
public class ComponentsVMTest {
	
	@Mock
	private ComponentService componentService;
	@Mock
	private DialogManager dialogManager;
	@Mock
	private BindUtilsWrapper bindWrapper;
	@Captor
	private ArgumentCaptor<Performable> deleteCallbackCaptor;
	@InjectMocks
	private ComponentsVM viewModel;

	@BeforeMethod
    public void beforeMethod() {
        viewModel = new ComponentsVM();
        MockitoAnnotations.initMocks(this);
    }
	
	@Test(dataProvider = "dataProvider")
	public void init(Set<ComponentType> availableTypes, List<Component> allComponents, boolean canCreateNewComponent) {
		when(componentService.getAvailableTypes()).thenReturn(availableTypes);
		when(componentService.getAll()).thenReturn(allComponents);
		viewModel.init();
		assertEquals(viewModel.isCanCreateNewComponent(), canCreateNewComponent);
		assertTrue(viewModel.getAvailableComponentTypes().containsAll(availableTypes));
		assertTrue(viewModel.getComponentList().containsAll(allComponents));
		assertFalse(viewModel.isEditWindowVisible());
	}
	
	@Test(enabled=false)
	public void configureComponent() {
		/*
		 * TODO needs to wrapper for Executions or changes in window opening
		 * mechanism, or may be power mock should be used.
		 */
		throw new RuntimeException("Test not implemented");
	}
	
	@Test(dataProvider = "dataProvider")
	public void deleteComponent(Set<ComponentType> availableTypes, List<Component> allComponents, boolean canCreateNewComponent) {
		when(componentService.getAvailableTypes()).thenReturn(availableTypes);
		when(componentService.getAll()).thenReturn(allComponents);
		viewModel.init();
		
		Component selected = allComponents.get(0);
		selected.setName("testDelete");
		viewModel.setSelected(selected);
		viewModel.deleteComponent();
		verify(dialogManager).confirmDeletion(eq("testDelete"), deleteCallbackCaptor.capture());
		Performable callback = deleteCallbackCaptor.getValue();
		
		// Nothing should be happen before user confirms deletion. 
		verify(componentService, never()).deleteComponent(selected);
		
		// confirm deletion
		callback.execute();
		verify(componentService).deleteComponent(selected);
		assertNull(viewModel.getSelected());
		assertEquals(viewModel.isCanCreateNewComponent(), canCreateNewComponent);
		verify(bindWrapper).postNotifyChange(null, null, viewModel, "selected");
		verify(bindWrapper).postNotifyChange(null, null, viewModel, "componentList");
		verify(bindWrapper).postNotifyChange(null, null, viewModel, "canCreateNewComponent");
	}
	
	@Test(dataProvider = "dataProvider")
	public void cancelEdit(Set<ComponentType> availableTypes, List<Component> allComponents, boolean canCreateNewComponent) {
		when(componentService.getAvailableTypes()).thenReturn(availableTypes);
		when(componentService.getAll()).thenReturn(allComponents);
		viewModel.init();
		
		viewModel.cancelEdit();
		assertNull(viewModel.getSelected());
		assertFalse(viewModel.isEditWindowVisible());
		verify(componentService, times(2)).getAvailableTypes();
		verify(componentService, times(2)).getAll();
	}

	@Test(dataProvider = "dataProvider")
	public void editComponent(Set<ComponentType> availableTypes, List<Component> allComponents, boolean canCreateNewComponent) {
		when(componentService.getAvailableTypes()).thenReturn(availableTypes);
		when(componentService.getAll()).thenReturn(allComponents);
		viewModel.init();
		
		Component selected = allComponents.get(0);
		viewModel.editComponent(selected);
		assertTrue(viewModel.getAvailableComponentTypes().contains(selected.getComponentType()));
		assertTrue(viewModel.isEditWindowVisible());
	}

	@Test(dataProvider = "dataProvider")
	public void newComponent(Set<ComponentType> availableTypes, List<Component> allComponents, boolean canCreateNewComponent) {
		when(componentService.getAvailableTypes()).thenReturn(availableTypes);
		when(componentService.getAll()).thenReturn(allComponents);
		viewModel.init();
		
		viewModel.setSelected(allComponents.get(0));
		viewModel.newComponent();
		
		assertFalse("selected component should be replaced when <new button> was pressed.", viewModel.getSelected().equals(allComponents.get(0)));
		assertTrue(viewModel.isEditWindowVisible());
	}

	@Test(dataProvider = "dataProvider")
	public void saveComponent(Set<ComponentType> availableTypes, List<Component> allComponents, boolean canCreateNewComponent) {
		when(componentService.getAvailableTypes()).thenReturn(availableTypes);
		when(componentService.getAll()).thenReturn(allComponents);
		viewModel.init();
		
		Component selected = allComponents.get(0);
		viewModel.editComponent(selected);
		viewModel.saveComponent();
		
		assertFalse(viewModel.isEditWindowVisible());
		verify(componentService).saveComponent(selected);
	}
	
	@SuppressWarnings("static-method")
    @DataProvider
	public Object[][] dataProvider() {
		Set<ComponentType> availableTypes = new HashSet<ComponentType>();
		availableTypes.add(ComponentType.ARTICLE);
		HashSet<ComponentType> noAvailableComponentTypes = new HashSet<ComponentType>();

		Component component1 = new Component();
		component1.setComponentType(ComponentType.ADMIN_PANEL);
		Component component2 = new Component();
		component2.setComponentType(ComponentType.FORUM);
		List<Component> allComponents = Arrays.asList(component1, component2);

		return new Object[][] { { availableTypes, allComponents, true },
		        { noAvailableComponentTypes, allComponents, false } };
	}

}
