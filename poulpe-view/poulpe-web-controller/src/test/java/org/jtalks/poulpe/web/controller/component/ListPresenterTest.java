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
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.DialogManager.Performable;
import org.jtalks.poulpe.web.controller.utils.ObjectCreator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.*;

/**
 * The test for {@link ListPresenter} class.
 * 
 * @author Dmitriy Sukharev
 * @author Alexey Grigorev
 */
public class ListPresenterTest {

    // SUT
    private ListPresenter presenter;
    
    // dependencies
    private ComponentService componentService;
    private ListView view;
    private DialogManager dialogManager;

    // test data
    private List<Component> components;
    private Component component;

    @BeforeMethod
    public void setUp() {
        presenter = new ListPresenter();

        componentService = mock(ComponentService.class);
        presenter.setComponentService(componentService);

        dialogManager = mock(DialogManager.class);
        presenter.setDialogManager(dialogManager);

        prepareTestData();
        
        view = mock(ListView.class);
        presenter.initView(view);
    }

    private void prepareTestData() {
        components = ObjectCreator.createComponents();
        when(componentService.getAll()).thenReturn(components);
        component = components.get(0);
    }

    @Test
    public void initListViewTest() {
        verify(view).createModel(components);
    }

    @Test
    public void deleteComponentTest() {
        givenSelectedElement();
        presenter.deleteComponent();
        verifyComfirmDialogShown();
    }

    private void givenSelectedElement() {
        when(view.hasSelectedItem()).thenReturn(true);
        when(view.getSelectedItem()).thenReturn(component);
    }
    
    private void verifyComfirmDialogShown() {
        verify(dialogManager).confirmDeletion(eq(component.getName()), any(Performable.class));
    }
    
    @Test
    public void deleteComponentNoSelectedItemTest() {
        givenNoSelectedElement();
        presenter.deleteComponent();
        verify(dialogManager).notify("item.no.selected.item");
    }

    private void givenNoSelectedElement() {
        when(view.hasSelectedItem()).thenReturn(false);
    }

    @Test
    public void executeDcTest() {
        givenSelectedElement();

        presenter.new DeletePerformable().execute();

        verify(componentService).deleteComponent(component);
        verify(view).updateList(components);
    }

    @Test
    public void testConfigureComponentNoSelectedItem() {
        givenNoSelectedElement();

        presenter.configureComponent();

        verify(dialogManager).notify("item.no.selected.item");
    }

}