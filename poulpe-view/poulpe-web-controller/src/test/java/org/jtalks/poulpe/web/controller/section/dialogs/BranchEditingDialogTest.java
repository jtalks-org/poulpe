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
package org.jtalks.poulpe.web.controller.section.dialogs;

import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.ForumStructureService;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.test.fixtures.TestFixtures;
import org.jtalks.poulpe.web.controller.section.ForumStructureItem;
import org.jtalks.poulpe.web.controller.section.ForumStructureVm;
import org.jtalks.poulpe.web.controller.zkutils.ZkTreeModel;
import org.jtalks.poulpe.web.controller.zkutils.ZkTreeNode;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.jtalks.poulpe.test.fixtures.TestFixtures.branch;
import static org.jtalks.poulpe.web.controller.section.TreeNodeFactory.buildForumStructure;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.*;

/**
 * @author stanislav bashkirtsev
 */
public class BranchEditingDialogTest {
    private GroupService groupService;
    private BranchEditingDialog sut;

    @BeforeMethod
    public void setUp() throws Exception {
        groupService = mock(GroupService.class);
        sut = new BranchEditingDialog(groupService, mock(ForumStructureVm.class), mock(ForumStructureService.class));
        sut.renewSectionsFromTree(buildTreeModel());
    }

    @Test(dataProvider = "provideTreeModelWithSectionsAndBranches")
    public void testRenewSectionsFromTree(ZkTreeModel<ForumStructureItem> treeModel) throws Exception {
        sut.renewSectionsFromTree(treeModel);
        assertEquals(sut.getSectionList().size(), treeModel.getRoot().getChildCount());
    }

    @Test(dataProvider = "provideGroups", enabled = false)
    public void testGetCandidatesToModerate(List<Group> givenGroups) throws Exception {
        doReturn(givenGroups).when(groupService).getAll();

        sut.showBranchDialog(new PoulpeBranch());
        List<Group> candidatesToModerate = sut.getCandidatesToModerate();
        assertEquals(candidatesToModerate, givenGroups);
    }

    @Test(dataProvider = "provideBranchWithModeratingGroup", enabled = false)
    public void getModeratorsGroupShouldReturnGroupFromBranch(PoulpeBranch branch) {
        doReturn(Arrays.asList(branch.getModeratorsGroup())).when(groupService).getAll();
        sut.showBranchDialog(new PoulpeBranch());
        assertEquals(sut.getModeratingGroup(), branch.getModeratorsGroup());
    }

    @Test(enabled = false)
    public void isShowingDialogShouldChangeFlagAfterFirstInvocation() {
        sut.showBranchDialog(branch());
        assertTrue(sut.isShowDialog());
        assertFalse(sut.isShowDialog());
    }

    @DataProvider
    public Object[][] provideBranchWithModeratingGroup() {
        PoulpeBranch branch = branch();
        branch.setModeratorsGroup(TestFixtures.group());
        return new Object[][]{{branch}};
    }

    @DataProvider
    public Object[][] provideGroups() {
        List<Group> groups = Arrays.asList(TestFixtures.group(), TestFixtures.group());
        return new Object[][]{{groups}};
    }

    @DataProvider
    public Object[][] provideTreeModelWithSectionsAndBranches() {
        return new Object[][]{{buildTreeModel()}};
    }

    private static ZkTreeModel<ForumStructureItem> buildTreeModel() {
        Jcommune jcommune = TestFixtures.jcommuneWithSections();
        ZkTreeNode<ForumStructureItem> forumStructure = buildForumStructure(jcommune);
        return new ZkTreeModel<ForumStructureItem>(forumStructure);
    }
}
