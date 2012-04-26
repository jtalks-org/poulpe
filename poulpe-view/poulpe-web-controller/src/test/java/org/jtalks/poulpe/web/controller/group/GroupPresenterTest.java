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
package org.jtalks.poulpe.web.controller.group;

import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;

import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GroupPresenterTest {
    GroupPresenter presenter;
    
    @Mock DialogManager dialogManager;
    @Mock GroupService service;
    @Mock ZkGroupView viewMock;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
        presenter = new GroupPresenter();
        presenter.setDialogManager(dialogManager);
        presenter.setGroupService(service);
        presenter.initView(viewMock);
    }
    
    @Test
    public void testInitView() throws Exception {
        List<Group> emptyList = Collections.emptyList();
        verify(viewMock).updateView(emptyList);
    }
    
    @Test
    public void testOnEditGroup() {
        Group group = new Group();
        presenter.onEditGroup(group);
        verify(viewMock).openEditDialog(group);
    }

    @Test
    public void testOnAddGroup() {
        presenter.onAddGroup();
        verify(viewMock).openNewDialog();
    }

}
