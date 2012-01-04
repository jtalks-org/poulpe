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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class EditGroupDialogPresenterTest {
    private EditGroupDialogPresenter dialogPresenter;
    @Mock
    private GroupService mockGroupService;
    @Mock
    private EditGroupDialogView mockView;
    private Group group;

    @BeforeMethod
    public void beforeMethod() {
        dialogPresenter = new EditGroupDialogPresenter();
        MockitoAnnotations.initMocks(this);
        group = new Group();
        dialogPresenter.setGroupService(mockGroupService);
    }

    @Test
    public void testValidateWhenNameIsNull() {
        String nullName = null;
        assertNotNull(dialogPresenter.validate(nullName));
    }

    @Test
    public void testValidateWhenNameIsNormal() {
        String normalName = "normal";
        assertNull(dialogPresenter.validate(normalName));
    }

    @Test
    public void testValidateWhenNameIsLong() {
        String longName = new String(new byte[255]);
        assertNotNull(dialogPresenter.validate(longName));
    }

    @Test
    public void testSaveOrUpdate() throws NotUniqueException {
        String name = "name";
        String desc = "description";
        dialogPresenter.initView(mockView, group);
        dialogPresenter.saveOrUpdateGroup(name, desc);
        verify(mockGroupService).saveGroup(group);
    }

    @Test
    public void testSaveOrUpdateGroupWithException() throws NotUniqueException {
        Group nullGroup = new Group();
        doThrow(new NotUniqueException()).when(mockGroupService).saveGroup(nullGroup);
        final String name = "name";
        final String desc = "description";

        dialogPresenter.initView(mockView, nullGroup);
        dialogPresenter.setGroupService(mockGroupService);
        dialogPresenter.saveOrUpdateGroup(name, desc);
        verify(mockView).notUniqueGroupName();
    }

}
