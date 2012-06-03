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
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertFalse;

/**
 *@author Kazancev Leonid
 */
public class EditComponentVmTest {
    @Mock
    private ComponentService componentService;

    private SelectedEntity<Component> selectedEntity = new SelectedEntity<Component>();

    @InjectMocks
    private EditComponentVm viewModel;

    @BeforeMethod
    public void setUp(){
        viewModel = spy(new EditComponentVm(componentService));
        MockitoAnnotations.initMocks(this);
        viewModel.setSelectedEntity(selectedEntity);
        selectedEntity.setEntity(new Jcommune());
    }

    @Test
    public void initData(){
        viewModel.initData();
        assertFalse(viewModel.getCurrentComponent() == null);
    }




}
