/**
 * Copyright (C) 2011  jtalks.org Team
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
 * Also add information on how to contact you by electronic and paper mail.
 * Creation date: Apr 12, 2011 / 8:05:19 PM
 * The jtalks.org Project
 */
package org.jtalks.poulpe.web.controller.component;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jtalks.poulpe.model.dao.DuplicatedField;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.service.exceptions.NotFoundException;
import org.jtalks.poulpe.service.exceptions.NotUniqueFieldsException;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


/**
 * The test for {@link ItemPresenter} class.
 * 
 * @author Dmitriy Sukharev
 * 
 */
public class ItemPresenterTest {

    private ItemPresenter presenter;
    
    private static final int ID = 1;

    @BeforeTest public void setUp() {
        presenter = new ItemPresenter();
    }

    private Component getFakeComponent(long id, String name, String description, ComponentType type) {
        Component comp = new Component();
        comp.setId(id);
        comp.setName(name);
        comp.setDescription(description);
        comp.setComponentType(type);
        return comp;
    }

    private List<Component> getFakeComponents() {
        List<Component> list = new ArrayList<Component>();
        ComponentType[] types = ComponentType.values();
        for (int i = 0; i < types.length; i++) {
            list.add(getFakeComponent(i, "Fake" + i, "Desc" + i, types[i]));
        }
        return list;
    }

    /**
     * Tests view initialisation in case if the componentId is existing
     * (correct) id.
     * @throws NotFoundException
     */
    @Test public void initViewCorrectTest() throws NotFoundException {
        ComponentService componentService = mock(ComponentService.class);
        ItemViewImpl view = mock(ItemViewImpl.class);
        presenter.setComponentService(componentService);

        final int id = 0;
        List<Component> fake = getFakeComponents();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("componentId", (long) id);

        when(componentService.getAll()).thenReturn(fake);
        when(componentService.get((long) id)).thenReturn(fake.get(id));
        when(view.getArgs()).thenReturn(map);

        presenter.initView(view);

        verify(view).setCid(fake.get(id).getId());
        verify(view).setName(fake.get(id).getName());
        verify(view).setDescription(fake.get(id).getDescription());
        verify(view).setComponentType(fake.get(id).getComponentType().toString());
    }

    /** Tests view initialisation in case if componentId is -1L (new element) */
    @Test public void initViewNewTest() {
        ComponentService componentService = mock(ComponentService.class);
        ItemViewImpl view = mock(ItemViewImpl.class);
        WindowManager wm = mock(WindowManager.class);
        DialogManager dm = mock(DialogManager.class);
        presenter.setComponentService(componentService);
        presenter.setWindowManager(wm);
        presenter.setDialogManager(dm);

        final int id = -1;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("componentId", (long) id);

        when(view.getArgs()).thenReturn(map);

        presenter.initView(view);

        verify(view).setCid(0);
        verify(view).setName(null);
        verify(view).setDescription(null);
        verify(view).setComponentType(null);
    }

    /**
     * Tests view initialisation in case if there is no such object
     * @throws NotFoundException
     */
    @Test public void initViewRemovedTest() throws NotFoundException {
        ComponentService componentService = mock(ComponentService.class);
        ItemViewImpl view = mock(ItemViewImpl.class);
        WindowManager wm = mock(WindowManager.class);
        DialogManager dm = mock(DialogManager.class);
        presenter.setComponentService(componentService);
        presenter.setWindowManager(wm);
        presenter.setDialogManager(dm);

        final int id = 9999;
        List<Component> fake = getFakeComponents();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("componentId", (long) id);

        when(componentService.getAll()).thenReturn(fake);
        when(componentService.get((long) id)).thenThrow(new NotFoundException());
        when(view.getArgs()).thenReturn(map);

        presenter.initView(view);

        verify(dm).notify("item.doesnt.exist");
        verify(wm).closeWindow(view);
    }

    /**
     * Initialises view preparing the component {@code fake} to be saved.
     * 
     * @param fake the component to be saved
     * @param fakeList the list of component required to decide if {@code fake}
     *                 is a new, duplicate or existing component
     * @return initialised view (and changed presenter)
     * @throws NotFoundException
     */
    private ItemView compPreparation(Component fake, List<Component> fakeList, Set<DuplicatedField> dupSet)
            throws NotFoundException {
        ComponentService componentService = mock(ComponentService.class);
        ItemViewImpl view = mock(ItemViewImpl.class);
        WindowManager wm = mock(WindowManager.class);       // successful save closes window
        presenter.setComponentService(componentService);
        presenter.setWindowManager(wm);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("componentId", (long) ID);

        when(componentService.getAll()).thenReturn(fakeList);
        when(componentService.get((long) ID)).thenReturn(fakeList.get(ID));
        when(view.getArgs()).thenReturn(map);

        presenter.initView(view);

        when(view.getName()).thenReturn(fake.getName());
        when(view.getCid()).thenReturn(fake.getId());
        when(view.getDescription()).thenReturn(fake.getDescription());
        when(view.getComponentType()).thenReturn(fake.getComponentType().toString());
        
        when(componentService.getDuplicateFieldsFor(any(Component.class))).thenReturn(dupSet);

        return view;
    }

    /**
     * Tests component saving in case if it's new (extraordinary) one.
     * @throws NotFoundException
     * @throws NotUniqueFieldsException 
     */
    @Test public void saveComponentNewTest() throws NotFoundException, NotUniqueFieldsException {
        List<Component> fakeList = getFakeComponents();
        Component fake = getFakeComponent(0, "extraordinary", "new", ComponentType.ARTICLE);
        ItemView view = compPreparation(fake, fakeList, null);

        presenter.saveComponent();

        verify(presenter.getComponentService()).saveComponent(argThat(new ComponentMatcher(fake)));
        verify(presenter.getWindowManager()).closeWindow(view);
    }

    /**
     * Tests component saving in case if it's existing one (from the component list).
     * @throws NotFoundException
     * @throws NotUniqueFieldsException 
     */
    @Test public void saveComponentExistingTest() throws NotFoundException, NotUniqueFieldsException {
        List<Component> fakeList = getFakeComponents();
        final Component fake = fakeList.get(ID);
        ItemView view = compPreparation(fake, fakeList, null);

        presenter.saveComponent();

        verify(presenter.getComponentService()).saveComponent(argThat(new ComponentMatcher(fake)));
        verify(presenter.getWindowManager()).closeWindow(view);
    }

    /**
     * Tests component saving in case if it's duplicate one (new, but with duplicate name).
     * @throws IllegalAccessException 
     * @throws NoSuchFieldException 
     * @throws IllegalArgumentException 
     * @throws SecurityException 
     * @throws NotUniqueFieldsException 
     */
    @Test public void saveComponentDuplicateTest() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, NotUniqueFieldsException {
        ComponentService componentService = mock(ComponentService.class);
        presenter.setComponentService(componentService);
        ItemViewImpl view = mock(ItemViewImpl.class);
        
        Component fake = getFakeComponent(0, "duplicate", "new", ComponentType.ARTICLE);
        initView(view, fake);
        HashSet<DuplicatedField> set = new HashSet<DuplicatedField>();
        doThrow(new NotUniqueFieldsException(set)).when(componentService).saveComponent(any(Component.class));
        
        presenter.saveComponent();

        verify(view).wrongFields("item.already.exist", set);
    }
    
    /**
     * Tests checking component (checking if component name isn't duplicate).
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    @Test public void checkComponent() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        List<Component> fakeList = getFakeComponents();
        ComponentService componentService = mock(ComponentService.class);
        presenter.setComponentService(componentService);
        ItemViewImpl view = mock(ItemViewImpl.class);
        
        HashSet<DuplicatedField> set = new HashSet<DuplicatedField>();
        
        // new component
        Component fake = getFakeComponent(0, "extraordinary", "new", ComponentType.ARTICLE);
        initView(view, fake);
        when(componentService.getDuplicateFieldsFor(any(Component.class))).thenReturn(null);
        presenter.checkComponent();
        verify(view, never()).wrongFields("item.already.exist", set);
        
        // existing
        fake = fakeList.get(ID);
        initView(view, fake);
        when(componentService.getDuplicateFieldsFor(any(Component.class))).thenReturn(null);
        presenter.checkComponent();
        verify(view, never()).wrongFields("item.already.exist", set);
        
        // duplicate
        fake = getFakeComponent(0, fakeList.get(ID).getName(), "new", ComponentType.ARTICLE);
        initView(view, fake);
        when(componentService.getDuplicateFieldsFor(any(Component.class))).thenReturn(set);
//      when(componentService.getDuplicateFieldsFor(fake)).thenReturn(set);
        presenter.checkComponent();
        verify(view, times(1)).wrongFields("item.already.exist", set);
    }
    
    private void initView(ItemViewImpl view, Component fake) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = ItemPresenter.class.getDeclaredField("view");
        field.setAccessible(true);
        field.set(presenter, view);
        when(view.getName()).thenReturn(fake.getName());
        when(view.getCid()).thenReturn(fake.getId());
        when(view.getDescription()).thenReturn(fake.getDescription());
        when(view.getComponentType()).thenReturn(fake.getComponentType().toString());
    }
//
//    // TODO I hope getCidByName will be deleted (as well as the following 3 tests)
//    @Test public void getCidByNameTest() {
//        ComponentService componentService = mock(ComponentService.class);
//        presenter.setComponentService(componentService);
//        List<Component> fakeList = new ArrayList<Component>();
//        fakeList.add(getFakeComponent(0, "ac", "desc1", ComponentType.ARTICLE));
//        fakeList.add(getFakeComponent(1, "ae", null, ComponentType.FORUM));
//
//        when(componentService.getAll()).thenReturn(fakeList);
//
//        assertEquals(presenter.getCidByName("ac"), 0);
//        assertEquals(presenter.getCidByName("ae"), 1);
//        assertEquals(presenter.getCidByName("ad"), -1);
//    }
//
//    @Test(expectedExceptions = IllegalArgumentException.class)
//    public void getCidByNameException1Test() {
//        presenter.getCidByName(null);
//    }
//
//    @Test(expectedExceptions = IllegalArgumentException.class)
//    public void getCidByNameException2Test() {
//        presenter.getCidByName("");
//    }

}