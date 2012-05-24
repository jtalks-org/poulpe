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

import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.ForumStructureService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.section.dialogs.ConfirmBranchDeletionDialogVm;
import org.jtalks.poulpe.web.controller.zkutils.ZkTreeModel;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Random;

import static org.jtalks.poulpe.web.controller.section.TreeNodeFactory.buildForumStructure;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * @author stanislav bashkirtsev
 */
public class ForumStructureVmTest {
    @Mock
    private ForumStructureService forumStructureService;
    @Mock
    private ForumStructureData data;

    private ForumStructureVm sut;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        sut = new ForumStructureVm(forumStructureService, mock(WindowManager.class), mock(SelectedEntity.class));
        sut.setViewData(data);
        sut = spy(sut);
    }

    @AfterMethod
    public void afterMethod() {
        Mockito.validateMockitoUsage();
    }

    @Test
    public void testSaveSection() throws Exception {
        PoulpeSection selectedSection = new PoulpeSection("section", "description");
        doReturn(selectedSection).when(data).getSelectedEntity(PoulpeSection.class);
        doNothing().when(sut).storeNewSection(selectedSection);
        sut.saveSection();
        verify(data).addSelectedSectionToTreeIfNew();
        verify(data).closeDialog();
        verify(sut).storeNewSection(selectedSection);
    }

    @Test(enabled = false)
    public void testSaveBranch() throws Exception {
        doNothing().when(sut).storeSelectedBranch();
        sut.saveBranch();
        verify(data).putSelectedBranchToSectionInDropdown();
        verify(sut).storeSelectedBranch();
    }

    @Test(dataProvider = "provideRandomJcommuneWithSections", enabled = false)
    public void testStoreSelectedBranch(Jcommune jcommune) throws Exception {
        PoulpeBranch selectedBranch = jcommune.getSections().get(0).getPoulpeBranches().get(0);
        doReturn(jcommune).when(data).getRootAsJcommune();
        doReturn(selectedBranch).when(data).getSelectedEntity(PoulpeBranch.class);
        doReturn(jcommune.getSections().get(1)).when(data).getSectionSelectedInDropdown();
        sut.storeSelectedBranch();
        assertSame(jcommune.getSections().get(1), selectedBranch.getSection());
        assertTrue(jcommune.getSections().get(1).getPoulpeBranches().contains(selectedBranch));
        assertFalse(jcommune.getSections().get(0).getPoulpeBranches().contains(selectedBranch));
    }

    @Test(dataProvider = "provideRandomJcommuneWithSections", enabled = false)
    public void testStoreSelectedBranch_withNewBranch(Jcommune jcommune) throws Exception {
        PoulpeBranch selectedBranch = new PoulpeBranch("test");
        doReturn(jcommune).when(data).getRootAsJcommune();
        doReturn(selectedBranch).when(data).getSelectedEntity(PoulpeBranch.class);
        doReturn(jcommune.getSections().get(1)).when(data).getSectionSelectedInDropdown();
        sut.storeSelectedBranch();
        assertSame(jcommune.getSections().get(1), selectedBranch.getSection());
        assertTrue(jcommune.getSections().get(1).getPoulpeBranches().contains(selectedBranch));
        assertFalse(jcommune.getSections().get(0).getPoulpeBranches().contains(selectedBranch));
    }

    @Test
    public void testShowNewSection() throws Exception {
        sut.showNewSectionDialog(true);
        verify(data).showSectionDialog(true);
    }

    @Test
    public void testShowNewBranch() throws Exception {
        sut.showNewBranchDialog(true);
        verify(data).showBranchDialog(true);
    }

    @Test(dataProvider = "provideRandomJcommuneWithSections")
    public void testStoreNewSection(Jcommune jcommune) throws Exception {
        PoulpeSection selectedSection = new PoulpeSection("section", "description");
        doReturn(jcommune).when(data).getRootAsJcommune();

        sut.storeNewSection(selectedSection);
        assertSame(jcommune.getSections().get(jcommune.getSections().size() - 1), selectedSection);
        verify(forumStructureService).saveJcommune(jcommune);
    }

    @Test(enabled = false)
    public void testDeleteSelected_section() throws Exception {
        ForumStructureItem item = new ForumStructureItem(new PoulpeSection());
        doNothing().when(sut).deleteSelectedSection(item.getSectionItem());
        doReturn(item).when(data).removeSelectedItem();

        sut.deleteSelected();
        verify(sut).deleteSelectedSection(item.getSectionItem());
    }

    @Test(enabled = false)
    public void testDeleteSelected_branch() throws Exception {
        data.setSelectedItem(new ForumStructureItem(new PoulpeBranch()));
        ConfirmBranchDeletionDialogVm confirmDialog = new ConfirmBranchDeletionDialogVm();
        doReturn(confirmDialog).when(data).getConfirmBranchDeletionDialogVm();
        sut.deleteSelected();
        assertTrue(confirmDialog.isShowDialog());
    }

    @Test(enabled = false)
    public void testConfirmDeleteBranch() throws Exception {
        ForumStructureItem item = new ForumStructureItem(new PoulpeBranch());
        doNothing().when(sut).deleteSelectedBranch(item.getBranchItem());
        doReturn(item).when(data).removeSelectedItem();

        sut.confirmBranchDeletion();
        verify(sut).deleteSelectedBranch(item.getBranchItem());
    }

    @Test(dataProvider = "provideRandomJcommuneWithSections")
    public void testDeleteSelectedSection(Jcommune jcommune) throws Exception {
        PoulpeSection selectedSection = new PoulpeSection();
        doReturn(jcommune).when(forumStructureService).deleteSectionWithBranches(selectedSection);

        sut.deleteSelectedSection(selectedSection);
        verify(data).setSectionTree(any(ZkTreeModel.class));
    }

    @Test(enabled = false)
    public void testDeleteSelectedSection() throws Exception {
        PoulpeBranch branch = new PoulpeBranch();

        sut.deleteSelectedBranch(branch);
        verify(forumStructureService).removeBranch(branch);
    }

    @Test(dataProvider = "provideRandomJcommuneWithSections", enabled = false)
    public void testDeleteSelected_branch(Jcommune jcommune) throws Exception {
        doReturn(jcommune).when(data).getRootAsJcommune();
        PoulpeBranch selectedBranch = jcommune.getSections().get(1).getPoulpeBranches().get(0);
        doReturn(new ForumStructureItem(selectedBranch)).when(data).removeSelectedItem();

        sut.deleteSelected();
        assertFalse(jcommune.getSections().get(1).getPoulpeBranches().contains(selectedBranch));
        assertNull(selectedBranch.getSection());
        verify(forumStructureService).saveJcommune(jcommune);
    }

    @DataProvider
    public Object[][] provideRandomJcommuneWithSections() {
        Jcommune jcommune = new Jcommune();
        PoulpeSection sectionA = new PoulpeSection("SectionA");
        sectionA.addOrUpdateBranch(createBranch(sectionA, "BranchA"));
        sectionA.addOrUpdateBranch(createBranch(sectionA, "BranchB"));
        jcommune.addSection(sectionA);
        PoulpeSection sectionB = new PoulpeSection("SectionB");
        sectionB.addOrUpdateBranch(createBranch(sectionB, "BranchD"));
        sectionB.addOrUpdateBranch(createBranch(sectionB, "BranchE"));
        jcommune.addSection(sectionB);
        return new Object[][]{{jcommune}};
    }

    private PoulpeBranch createBranch(PoulpeSection section, String branchName) {
        PoulpeBranch branch = new PoulpeBranch(branchName);
        branch.setId(new Random().nextLong());
        branch.setSection(section);
        return branch;
    }
}
