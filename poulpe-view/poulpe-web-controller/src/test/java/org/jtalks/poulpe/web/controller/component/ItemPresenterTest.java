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
import static org.jtalks.poulpe.web.controller.utils.ObjectCreator.createComponents;
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
 * @author Alexey Grigorev
 */
public class ItemPresenterTest {

    private ItemPresenter presenter;

    private ComponentService componentService;
    private ItemView view;
    private List<Component> components;
    private Component component;
    private DialogManager dialogManager;

    @Deprecated
    Set<DuplicatedField> name = Collections.<DuplicatedField> singleton(ComponentDuplicateField.NAME);
    @Deprecated
    Set<DuplicatedField> empty = Collections.emptySet();

    @BeforeMethod
    public void setUp() {
        presenter = new ItemPresenter();

        componentService = mock(ComponentService.class);
        presenter.setComponentService(componentService);

        dialogManager = mock(DialogManager.class);
        presenter.setDialogManager(dialogManager);

        view = mock(ItemView.class);

        prepareComponents();
        initView();
    }

    private void prepareComponents() {
        components = createComponents();
        component = components.get(0);
        int someRandomId = 12;
        component.setId(someRandomId);
    }

    private void initView() {
        presenter.setView(view);

        when(view.getName()).thenReturn(component.getName());
        when(view.getComponentId()).thenReturn(component.getId());
        when(view.getDescription()).thenReturn(component.getDescription());
        when(view.getComponentType()).thenReturn(component.getComponentType());

        when(componentService.getAll()).thenReturn(components);
    }

    @Test
    public void editTest() throws Exception {
        presenter.edit(component);
        verifyAllDataSet();
    }

    private void verifyAllDataSet() {
        verify(view).setComponentId(component.getId());
        verify(view).setName(component.getName());
        verify(view).setDescription(component.getDescription());
        verify(view).setComponentType(component.getComponentType());
    }

    @Test
    public void createTest() {
        presenter.create();
        verifyBlankWindowShown();
    }

    private void verifyBlankWindowShown() {
        verify(view).setComponentId(0);
        verify(view).setName(null);
        verify(view).setDescription(null);
        verify(view).setComponentType(null);
    }

    @Test(enabled = false)
    @Deprecated
    public void saveNewComponentTest() throws Exception {
        presenter.create();

        givenNoDiplicatedFields();
        presenter.saveComponent();

        assertComponentSaved();
        verify(view).hide();
    }

    @Deprecated
    private void givenNoDiplicatedFields() {
        when(componentService.getDuplicateFieldsFor(component)).thenReturn(empty);
    }

    @Deprecated
    private void assertComponentSaved() throws NotUniqueFieldsException {
        ArgumentCaptor<Component> captor = ArgumentCaptor.forClass(Component.class);

        verify(presenter.getComponentService()).saveComponentCheckUniqueness(captor.capture());
        Component value = captor.getValue();

        assertEquals(value.getName(), component.getName());
        assertEquals(value.getComponentType(), component.getComponentType());
    }

    @Test(enabled = false)
    @Deprecated
    public void saveComponentExistingTest() throws Exception {
        presenter.edit(component);

        givenNoDiplicatedFields();
        presenter.saveComponent();

        verify(presenter.getComponentService()).saveComponentCheckUniqueness(component);
        verify(view).hide();
    }

    @Test(enabled = false)
    @Deprecated
    public void saveComponentDuplicateTest() throws Exception {
        presenter.create();

        givenNameFieldDuplicated();
        presenter.saveComponent();

        verify(view).wrongFieldsDuplicatedFieldSet(name);
    }

    @Deprecated
    private void givenNameFieldDuplicated() throws NotUniqueFieldsException {
        doThrow(new NotUniqueFieldsException(name)).when(componentService).saveComponentCheckUniqueness(any(Component.class));
    }

    @Test(enabled = false)
    @Deprecated
    public void saveComponentDuplicateTestEdit() throws Exception {
        presenter.edit(component);

        givenFieldNameDuplicatedWhenEditing();
        presenter.saveComponent();

        verify(view).wrongFieldsDuplicatedFieldSet(name);
    }

    @Deprecated
    private void givenFieldNameDuplicatedWhenEditing() throws NotUniqueFieldsException {
        doThrow(new NotUniqueFieldsException(name)).when(componentService).saveComponentCheckUniqueness(component);
    }

    @Test(enabled = false)
    @Deprecated
    public void checkComponentTestNewComponent() {
        presenter.create();

        givenNoDiplicatedFields();
        presenter.checkComponent();

        verify(view, never()).wrongFieldsDuplicatedFieldSet(name);
    }

    @Test(enabled = false)
    @Deprecated
    public void checkComponentTestExistingComponent() {
        presenter.edit(component);

        givenNoDiplicatedFields();
        presenter.checkComponent();

        verify(view, never()).wrongFieldsDuplicatedFieldSet(name);
    }

    @Test(enabled = false)
    @Deprecated
    public void checkComponentDiplicate() {
        presenter.edit(component);
        givenDuplicatedField();
        presenter.checkComponent();

        verify(view).wrongFieldsDuplicatedFieldSet(name);
    }

    @Deprecated
    private void givenDuplicatedField() {
        when(componentService.getDuplicateFieldsFor(any(Component.class))).thenReturn(name);
    }

}