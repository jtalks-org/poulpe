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
package org.jtalks.poulpe.web.controller.section;

import static org.jtalks.poulpe.web.controller.utils.ObjectsFactory.fakeBranch;
import static org.jtalks.poulpe.web.controller.utils.ObjectsFactory.sectionWithBranches;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.zul.Button;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.Tree;

/**
 * @author Alexey Grigorev
 */
public class ZkSectionTreeComponentTest {
    
    private ZkSectionTreeComponent sectionTreeComponent;
    private ZkSectionTreeComponent spy;

    @Mock SectionPresenter presenter;
    @Mock ZkHelper zkInitializer;
    @Mock Tree sectionTree;
    
    private PoulpeSection section = sectionWithBranches();
    private PoulpeBranch branch = fakeBranch();

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
        
        sectionTreeComponent = new ZkSectionTreeComponent(zkInitializer);
        sectionTreeComponent.setSectionTree(sectionTree);
        sectionTreeComponent.init(section, presenter);
        
        spy = spy(sectionTreeComponent);
    }

    @Test
    public void initialization() {
        verify(zkInitializer).wireByConvention();
        verify(zkInitializer).wireToZul(ZkSectionTreeComponent.ZUL_REF);
    }
    
    @Test
    public void sectionTreeInitialized() {
        verify(sectionTree).setModel(any(DefaultTreeModel.class));
    }

    @Test
    public void newBranchDialog() {
        sectionTreeComponent.newBranchDialog();
        verify(presenter).openNewBranchDialog(sectionTreeComponent);
    }

    @Test
    public void editDialog() {
        sectionTreeComponent.editDialog();
        verify(presenter).openEditDialog(sectionTreeComponent);
    }

    @Test
    public void moderationButton() {
        doReturn(section).when(spy).getSelectedOrFirstElement();
        spy.moderationDialog();
        verify(presenter).openModerationWindow(section);
    }

    @Test
    public void deleteDialog() {
        doReturn(section).when(spy).getSelectedOrFirstElement();
        spy.deleteDialog();
        verify(presenter).openDeleteDialog(section);
    }

    @Test
    public void disablePermissionsButtonWhenSection() {
        Button moderatorsButton = mockModeratorsButton();
        Button permissionsButton = mockPermissionButton();
        sectionTreeComponent.disableModeratorsButtonIfNeeded(section);
        sectionTreeComponent.disablePermissionsButtonIfNeeded(section);
        verify(moderatorsButton).setDisabled(true);
        verify(permissionsButton).setDisabled(true);
    }
    
    @Test
    public void enablePermissionsButtonWhenBranch() {
        Button moderatorsButton = mockModeratorsButton();
        Button permissionsButton = mockPermissionButton();
        sectionTreeComponent.disableModeratorsButtonIfNeeded(branch);
        sectionTreeComponent.disablePermissionsButtonIfNeeded(branch);
        verify(moderatorsButton).setDisabled(false);
        verify(permissionsButton).setDisabled(false);
    }

    private Button mockModeratorsButton() {
        Button moderatorsButton = mock(Button.class);
        when(sectionTree.getFellow("moderatorsButton")).thenReturn(moderatorsButton);
        return moderatorsButton;
    }

    private Button mockPermissionButton() {
        Button permissionsButton = mock(Button.class);
        when(sectionTree.getFellow("permissionsButton")).thenReturn(permissionsButton);
        return permissionsButton;
    }
}
