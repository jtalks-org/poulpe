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
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.common.model.entity.Property;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.Poulpe;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * @author Kazancev Leonid
 */
public class EditComponentVmTest {
   private static final String
            COMPONENT_NAME = "name",
            OTHER_COMPONENT_NAME = "notName",
            DESCRIPTION = "desc",
            PROP_NAME = ".name",
            PROP_CAPTION = ".caption",
            PROP_PREVIEW_SIZE = ".postPreviewSize",
            PROP_SESSION_TIMEOUT = ".session_timeout",
            NAME = "nameProp",
            OTHER_NAME = "otherName",
            POST_PREVIEW_SIZE = "100",
            SESSION_TIMEOUT = "100",
            CAPTION = "aCaption",
            JCOMMUNE_TYPE = "jcommune";


    @Mock
    private ComponentService componentService;
    @Mock
    private WindowManager windowManager;

    private SelectedEntity<Component> selectedEntity = new SelectedEntity<Component>();

    @InjectMocks
    private EditComponentVm viewModel;

    @BeforeMethod
    public void setUp() {
        viewModel = spy(new EditComponentVm(componentService));
        MockitoAnnotations.initMocks(this);
        viewModel.setWindowManager(windowManager);
        viewModel.setSelectedEntity(selectedEntity);
    }

    @Test
    public void initData() {
        selectedEntity.setEntity(getSomeForum());
        viewModel.initData();
        assertNotNull(viewModel.getCurrentComponent());
        assertEquals(viewModel.getName(), NAME);
        assertEquals(viewModel.getCaption(), CAPTION);
        assertEquals(viewModel.getPostPreviewSize(), POST_PREVIEW_SIZE);
        assertEquals(viewModel.getSessionTimeout(), SESSION_TIMEOUT);
        assertEquals(viewModel.getComponentName(), COMPONENT_NAME);
        assertEquals(viewModel.getDescription(), DESCRIPTION);
        
        selectedEntity.setEntity(getSomePoulpe());
        viewModel.initData();
        assertNotNull(viewModel.getCurrentComponent());
        assertNotSame(viewModel.getName(), NAME);
        assertNotSame(viewModel.getCaption(), CAPTION);
        assertNotSame(viewModel.getPostPreviewSize(), POST_PREVIEW_SIZE);
        assertNotSame(viewModel.getSessionTimeout(), SESSION_TIMEOUT);
        assertEquals(viewModel.getComponentName(), COMPONENT_NAME);
        assertEquals(viewModel.getDescription(), DESCRIPTION);
    }

    @Test
    public void getComponents() {
        viewModel.getComponents();
        verify(componentService).getAll();
    }

    @Test
    public void save() {
        selectedEntity.setEntity(getSomeForum());
        viewModel.initData();
        viewModel.setComponentName(OTHER_COMPONENT_NAME);
        viewModel.setName(OTHER_NAME);
        assertNotSame(viewModel.getCurrentComponent().getName(), OTHER_COMPONENT_NAME);
        assertNotSame(viewModel.getCurrentComponent().getProperty(JCOMMUNE_TYPE + PROP_NAME),OTHER_NAME);
        doNothing().when(windowManager).open(any(String.class));
        viewModel.save();
        verify(windowManager).open(any(String.class));
        assertEquals(viewModel.getCurrentComponent().getName(), OTHER_COMPONENT_NAME);
        assertEquals(viewModel.getCurrentComponent().getProperty(JCOMMUNE_TYPE+PROP_NAME),OTHER_NAME);
    }
    
    @Test
    public void cancel(){
        viewModel.setName(null);
        viewModel.save();
        assertFalse(viewModel.getValidationMessages().isEmpty());
        viewModel.cancel();
        assertTrue(viewModel.getValidationMessages().isEmpty());
        verify(windowManager).open(any(String.class));
     }


    private Poulpe getSomePoulpe() {
        Poulpe poulpe = new Poulpe();
        poulpe.setComponentType(ComponentType.FORUM);
        poulpe.setName(COMPONENT_NAME);
        poulpe.setDescription(DESCRIPTION);
        return poulpe;
    }

    private Jcommune getSomeForum() {
        Jcommune jcommune = new Jcommune();
        jcommune.setComponentType(ComponentType.FORUM);
        jcommune.setName(COMPONENT_NAME);
        jcommune.setDescription(DESCRIPTION);
        jcommune.setProperties(getSomeProperties());
        return jcommune;
    }

    private List<Property> getSomeProperties() {
        List<Property> props = new ArrayList<Property>(4);
        props.add(new Property(JCOMMUNE_TYPE + PROP_NAME, NAME));
        props.add(new Property(JCOMMUNE_TYPE + PROP_CAPTION, CAPTION));
        props.add(new Property(JCOMMUNE_TYPE + PROP_PREVIEW_SIZE, POST_PREVIEW_SIZE));
        props.add(new Property(JCOMMUNE_TYPE + PROP_SESSION_TIMEOUT, SESSION_TIMEOUT));

        return props;
    }

}
