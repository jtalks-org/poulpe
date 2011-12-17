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

import static org.testng.Assert.*;
import static org.jtalks.poulpe.web.controller.utils.ObjectCreator.getFakeComponents;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jtalks.poulpe.model.dao.ComponentDao.ComponentDuplicateField;
import org.jtalks.poulpe.model.dao.DuplicatedField;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.service.exceptions.NotUniqueFieldsException;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.component.items.ItemPresenter;
import org.jtalks.poulpe.web.controller.component.items.ItemView;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The test for {@link ItemPresenter} class.
 * @author Dmitriy Sukharev
 */
public class ItemPresenterTest {

    private ItemPresenter presenter;

    private static final int componentId = 1;
    private ComponentService componentService;
    private ItemView view;
    private List<Component> components;
    private Component component;
    private DialogManager dialogManager;

    @BeforeMethod
    public void setUp() {
        presenter = new ItemPresenter();
        
        components = getFakeComponents();
        component = components.get(0);
        component.setId(componentId);
        
        view = mock(ItemView.class);
        
        componentService = mock(ComponentService.class);
        presenter.setComponentService(componentService);
        
        dialogManager = mock(DialogManager.class);
        presenter.setDialogManager(dialogManager);
    }

    @Test
    public void initViewCorrectTest() throws Exception  {

        when(componentService.getAll()).thenReturn(components);
        //when(componentService.get(Long.valueOf(componentId))).thenReturn(component);

        presenter.initForEditing(view, component);

        verify(view).setComponentId(component.getId());
        verify(view).setName(component.getName());
        verify(view).setDescription(component.getDescription());
        verify(view).setComponentType(component.getComponentType().toString());
    }

    @Test
    public void initViewNewNullTest() {
        presenter.initForCreating(view);

        verify(view).setComponentId(0);
        verify(view).setName(null);
        verify(view).setDescription(null);
        verify(view).setComponentType(null);
    }


    /**
     * Tests view initialisation in case if there is no such object
     */
    /*@Test(enabled = false) // seems that this test is unneeded
    public void initViewRemovedTest() throws NotFoundException {
        when(componentService.getAll()).thenReturn(components);
        when(componentService.get((long) componentId)).thenThrow(new NotFoundException());

        presenter.initView(view, Long.valueOf(componentId));

        verify(dialogManager).notify("item.doesnt.exist");
        verify(view).hide();
    }*/

    Set<DuplicatedField> name = Collections.<DuplicatedField> singleton(ComponentDuplicateField.NAME);
    Set<DuplicatedField> empty = Collections.emptySet();

    /**
     * Tests component saving in case if it's new (extraordinary) one.
     */
    @Test
    public void saveComponentNewTest() throws Exception {
        
        initViewNew();
        presenter.initForCreating(view);
        
        when(componentService.getDuplicateFieldsFor(component)).thenReturn(empty);

        presenter.saveComponent();

        ArgumentCaptor<Component> captor = ArgumentCaptor.forClass(Component.class);
        verify(presenter.getComponentService()).saveComponent(captor.capture());
        Component value = captor.getValue();
        assertEquals(value.getName(), component.getName());
        assertEquals(value.getComponentType(), component.getComponentType());
        
        verify(view).hide();
    }

    private void initViewNew() {
        when(componentService.getAll()).thenReturn(components);

        when(view.getName()).thenReturn(component.getName());
        when(view.getComponentId()).thenReturn(component.getId());
        when(view.getDescription()).thenReturn(component.getDescription());
        when(view.getComponentType()).thenReturn(component.getComponentType().toString());

    }

    /**
     * Tests component saving in case if it's existing one (from the component
     * list).
     */
    @Test
    public void saveComponentExistingTest() throws Exception {
        initViewNew();
        presenter.initForEditing(view, component);
        
        when(componentService.getDuplicateFieldsFor(component)).thenReturn(empty);
        
        presenter.saveComponent();
        
        verify(presenter.getComponentService()).saveComponent(component);
        verify(view).hide();
    }
    
    
    @Test
    public void saveComponentDuplicateTest() throws Exception {
        initView();
        doThrow(new NotUniqueFieldsException(name)).when(componentService).saveComponent(any(Component.class));
        presenter.saveComponent();
        verify(view).wrongFields(name);
    }

    @Test
    public void checkComponentTestNewComponent() {
        initView();
        when(componentService.getDuplicateFieldsFor(component)).thenReturn(empty);
        presenter.checkComponent();
        verify(view, never()).wrongFields(name);
    }
    
    @Test
    public void checkComponentTestExistingComponent() {
        initView();
        when(componentService.getDuplicateFieldsFor(component)).thenReturn(empty);
        presenter.checkComponent();
        verify(view, never()).wrongFields(name);
    }
    
    @Test
    public void checkComponentDiplicate() {
        initView();
        when(componentService.getDuplicateFieldsFor(any(Component.class))).thenReturn(name);
        presenter.checkComponent();
        verify(view).wrongFields(name);
    }

    private void initView() {
        presenter.setView(view);

        when(view.getName()).thenReturn(component.getName());
        when(view.getComponentId()).thenReturn(component.getId());
        when(view.getDescription()).thenReturn(component.getDescription());
        when(view.getComponentType()).thenReturn(component.getComponentType().toString());
    }

}