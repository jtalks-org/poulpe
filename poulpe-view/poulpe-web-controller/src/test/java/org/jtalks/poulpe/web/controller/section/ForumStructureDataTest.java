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
import org.jtalks.poulpe.web.controller.zkutils.ZkTreeModel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.zkoss.zul.TreeNode;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.jtalks.poulpe.web.controller.section.TreeNodeFactory.buildForumStructure;
import static org.testng.Assert.*;

/**
 * @author stanislav bashkirtsev
 */
public class ForumStructureDataTest {
    private ForumStructureData sut;

    @BeforeMethod
    public void setUp() throws Exception {
        sut = new ForumStructureData();
    }

    @Test
    public void testGetSelectedItemFirstTime() throws Exception {
        ForumStructureItem item = new ForumStructureItem();
        ForumStructureItem previous = sut.setSelectedItem(item);
        assertNull(previous.getItem());
    }

    @Test(dataProvider = "provideSutWithTree")
    public void testPutSelectedBranchToSectionFromDropdown_withNewBranchAddedToDestinationSection(ForumStructureData sut) {
        ForumStructureItem branchToPut = new ForumStructureItem(new PoulpeBranch());
        sut.setSelectedItem(branchToPut);
        sut.getSectionList().addToSelection(sut.getSectionList().get(1));//set destination section

        sut.putSelectedBranchToSectionInDropdown();
        assertSame(sut.getSectionTree().find(branchToPut).getParent(), sut.getSectionTree().getRoot().getChildAt(1));
    }

    @Test(dataProvider = "provideSutWithTree")
    public void testPutSelectedBranchToSectionFromDropdown_withExistingBranchAddedToDestinationSection(ForumStructureData sut) {
        ForumStructureItem branchToPut = sut.getSectionTree().getChild(0, 0).getData();
        sut.setSelectedItem(branchToPut);
        sut.getSectionList().addToSelection(sut.getSectionList().get(1));//set destination section

        sut.putSelectedBranchToSectionInDropdown();
        assertSame(sut.getSectionTree().find(branchToPut).getParent(), sut.getSectionTree().getRoot().getChildAt(1));
        assertNotSame(sut.getSectionTree().getChild(0, 0), branchToPut);
    }

    @Test(dataProvider = "provideSutWithTree")
    public void testPutSelectedBranchToSectionFromDropdown_verifyNodeIsOpenAndSelected(ForumStructureData sut) {
        ForumStructureItem branchToPut = sut.getSectionTree().getChild(0, 0).getData();
        sut.setSelectedItem(branchToPut);
        sut.getSectionList().addToSelection(sut.getSectionList().get(1));//set destination section

        sut.putSelectedBranchToSectionInDropdown();
        TreeNode<ForumStructureItem> movedBranch = sut.getSectionTree().find(branchToPut);
        assertSame(sut.getSectionTree().getSelection().iterator().next(), movedBranch);
        assertSame(sut.getSectionTree().getOpenObjects().iterator().next(), movedBranch.getParent());
    }

    @Test(dataProvider = "provideSutWithTree")
    public void testGetSectionSelectedInDropdown(ForumStructureData sut) throws Exception {
        ForumStructureItem toBeSelected = sut.getSectionList().get(1);
        sut.getSectionList().addToSelection(toBeSelected);
        assertSame(sut.getSectionSelectedInDropdown(), toBeSelected);
    }

    @Test
    public void testGetSelectedItemReturnsPreviousValue() throws Exception {
        ForumStructureItem item = new ForumStructureItem();
        sut.setSelectedItem(item);
        assertSame(item, sut.setSelectedItem(new ForumStructureItem()));
    }

    @Test(dataProvider = "provideTreeModelWithSectionsAndBranches")
    public void testAddSelectedSectionToTreeIfNew_notPersistentItemSelected(ZkTreeModel<ForumStructureItem> tree) {
        ForumStructureItem newSection = new ForumStructureItem(new PoulpeSection());
        sut.setSectionTree(tree);
        sut.setSelectedItem(newSection);

        sut.addSelectedSectionToTreeIfNew();
        assertSame(tree.getSelection().iterator().next().getData(), newSection);
    }

    @Test(dataProvider = "provideTreeModelWithSectionsAndBranches")
    public void testRemoveSelectedItem_selectedSection(ZkTreeModel<ForumStructureItem> treeModel) {
        TreeNode<ForumStructureItem> selectedNode = treeModel.getRoot().getChildAt(1);
        treeModel.addSelectionPath(new int[]{1});
        sut.setSelectedItem(selectedNode.getData());
        sut.setSectionTree(treeModel);

        assertSame(selectedNode.getData(), sut.removeSelectedItem());
        assertEquals(-1, treeModel.getRoot().getIndex(selectedNode));
    }

    @Test(dataProvider = "provideTreeModelWithSectionsAndBranches")
    public void testRemoveSelectedItem_selectedBranch(ZkTreeModel<ForumStructureItem> treeModel) {
        TreeNode<ForumStructureItem> selectedNode = treeModel.getRoot().getChildAt(1).getChildAt(1);
        treeModel.addSelectionPath(new int[]{1, 1});
        sut.setSelectedItem(selectedNode.getData());
        sut.setSectionTree(treeModel);

        assertSame(selectedNode.getData(), sut.removeSelectedItem());
        assertEquals(-1, treeModel.getRoot().getChildAt(1).getIndex(selectedNode));
    }

    @Test(dataProvider = "provideTreeModelWithSectionsAndBranches")
    public void testGetSectionSelectedInDropDown(ZkTreeModel<ForumStructureItem> treeModel) throws Exception {
        sut.setSectionTree(treeModel);
        ForumStructureItem itemSelectedInDropdown = treeModel.getRoot().getChildAt(1).getData();
        sut.getSectionList().addToSelection(itemSelectedInDropdown);
        assertSame(itemSelectedInDropdown.getItem(), sut.getSectionSelectedInDropdown().getItem());
    }

    @Test
    public void testShowSectionDialog_editingExisting() throws Exception {
        ForumStructureItem selectedItem = new ForumStructureItem();
        sut.setSelectedItem(selectedItem);
        sut.showSectionDialog(false);
        assertTrue(sut.isShowSectionDialog());
        assertSame(sut.getSelectedItem(), selectedItem);
    }

    @Test
    public void testShowSectionDialog_creatingNew() throws Exception {
        ForumStructureItem selectedItem = new ForumStructureItem();
        sut.setSelectedItem(selectedItem);

        sut.showSectionDialog(true);
        assertTrue(sut.isShowSectionDialog());
        assertNotSame(sut.getSelectedItem(), selectedItem);
        assertThat(sut.getSelectedItem().getItem(), instanceOf(PoulpeSection.class));
    }

    @Test(dataProvider = "provideTreeModelWithSectionsAndBranches")
    public void testShowBranchDialog_editingExisting(ZkTreeModel<ForumStructureItem> treeModel) {
        ForumStructureItem selectedBranch = treeModel.getRoot().getChildAt(0).getChildAt(1).getData();
        ForumStructureItem selectedSection = treeModel.getRoot().getChildAt(0).getData();
        sut.setSelectedItem(selectedBranch);
        treeModel.addSelectionPath(new int[]{0});
        sut.setSectionTree(treeModel);

        sut.showBranchDialog(false);
        assertTrue(sut.isShowBranchDialog());
        assertFalse(treeModel.isSelected(selectedSection));
        assertTrue(sut.getSectionList().isSelected(selectedSection));
        assertSame(sut.getSelectedItem(), selectedBranch);
    }

    @Test(dataProvider = "provideTreeModelWithSectionsAndBranches")
    public void testShowBranchDialog_createNew(ZkTreeModel<ForumStructureItem> treeModel) {
        ForumStructureItem selectedSection = treeModel.getRoot().getChildAt(0).getData();
        treeModel.addSelectionPath(new int[]{0, 1});
        sut.setSectionTree(treeModel);

        sut.showBranchDialog(true);
        assertTrue(treeModel.getSelection().isEmpty());
        assertTrue(sut.getSectionList().isSelected(selectedSection));
        assertTrue(sut.isShowBranchDialog());
        assertTrue(sut.getSelectedItem().isBranch());
        assertEquals(sut.getSelectedItem().getItem().getId(), 0);
    }

    @Test(dataProvider = "provideTreeModelWithSectionsAndBranches")
    public void testCloseDialogWithBranch(ZkTreeModel<ForumStructureItem> treeModel) throws Exception {
        treeModel.addSelectionPath(new int[]{0});
        sut.setSectionTree(treeModel);
        sut.showBranchDialog(true);
        sut.closeDialog();
        assertFalse(sut.isShowBranchDialog());
    }

    @Test(dataProvider = "provideTreeModelWithSectionsAndBranches")
    public void testIsShowBranchDialog(ZkTreeModel<ForumStructureItem> treeModel) throws Exception {
        treeModel.addSelectionPath(new int[]{1});
        sut.setSectionTree(treeModel);
        sut.showBranchDialog(true);
        assertTrue(sut.isShowBranchDialog());
        assertFalse(sut.isShowBranchDialog());
    }

    @Test
    public void testIsShowSectionDialog() throws Exception {
        sut.showSectionDialog(false);
        assertTrue(sut.isShowSectionDialog());
        assertFalse(sut.isShowSectionDialog());
    }

    @Test(dataProvider = "provideTreeModelWithSectionsAndBranches")
    public void testCloseDialogWithSection(ZkTreeModel<ForumStructureItem> treeModel) throws Exception {
        treeModel.addSelectionPath(new int[]{0});
        sut.setSectionTree(treeModel);
        sut.showSectionDialog(true);
        sut.closeDialog();
        assertFalse(sut.isShowSectionDialog());
    }

    @Test(dataProvider = "provideTreeModelWithSectionsAndBranches")
    public void testSetSectionTree(ZkTreeModel<ForumStructureItem> treeModel) throws Exception {
        sut.setSectionTree(treeModel);
        assertEquals(treeModel.getRoot().getChildCount(), sut.getSectionList().size());
    }

    @DataProvider
    public Object[][] provideTreeModelWithSectionsAndBranches() {
        Jcommune jcommune = new Jcommune();
        PoulpeSection sectionA = new PoulpeSection("SectionA");
        sectionA.addOrUpdateBranch(new PoulpeBranch("BranchA"));
        sectionA.addOrUpdateBranch(new PoulpeBranch("BranchB"));
        jcommune.addSection(sectionA);
        PoulpeSection sectionB = new PoulpeSection("SectionB");
        sectionB.addOrUpdateBranch(new PoulpeBranch("BranchD"));
        sectionB.addOrUpdateBranch(new PoulpeBranch("BranchE"));
        jcommune.addSection(sectionB);
        return new Object[][]{{new ZkTreeModel<ForumStructureItem>(buildForumStructure(jcommune))}};
    }

    /**
     * Provides the {@link ForumStructureData} with the tree already set.
     *
     * @return the {@link ForumStructureData} with the tree already set
     */
    @DataProvider
    public Object[][] provideSutWithTree() {
        ForumStructureData data = new ForumStructureData();
        data.setSectionTree((ZkTreeModel<ForumStructureItem>) provideTreeModelWithSectionsAndBranches()[0][0]);
        return new Object[][]{{data}};
    }


}
