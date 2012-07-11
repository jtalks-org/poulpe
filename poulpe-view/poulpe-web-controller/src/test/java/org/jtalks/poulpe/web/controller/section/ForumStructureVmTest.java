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

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.ForumStructureService;
import org.jtalks.poulpe.test.fixtures.TestFixtures;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;

/**
 * @author stanislav bashkirtsev
 * @author Guram Savinov
 */
public class ForumStructureVmTest {

    private ForumStructureVm viewModel;
    private ForumStructureTreeModel treeModel;
    private ForumStructureService forumStructureService;
    private WindowManager windowManager;
    private SelectedEntity<PoulpeBranch> selectedBranchForPermissions;

    @SuppressWarnings("unchecked")
    @BeforeMethod
    public void setUp() {
        treeModel = mock(ForumStructureTreeModel.class);
        forumStructureService = mock(ForumStructureService.class);
        windowManager = mock(WindowManager.class);
        selectedBranchForPermissions = mock(SelectedEntity.class);
        viewModel = new ForumStructureVm(forumStructureService, windowManager, selectedBranchForPermissions);
    }

    @DataProvider
    public Object[][] provideRandomJcommuneWithSections() {
        Jcommune jcommune = TestFixtures.jcommuneWithSections();
        return new Object[][]{{jcommune}};
    }
}
