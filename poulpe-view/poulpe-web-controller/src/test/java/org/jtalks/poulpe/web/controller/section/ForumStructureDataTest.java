package org.jtalks.poulpe.web.controller.section;

import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.zkoss.zul.DefaultTreeModel;
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

    @Test
    public void testGetSelectedItemReturnsPreviousValue() throws Exception {
        ForumStructureItem item = new ForumStructureItem();
        sut.setSelectedItem(item);
        assertSame(item, sut.setSelectedItem(new ForumStructureItem()));
    }

    @Test(dataProvider = "provideTreeModelWithSectionsAndBranches")
    public void testAddSelectedSectionToTreeIfNew_notPersistentItemSelected(DefaultTreeModel<ForumStructureItem> tree) {
        ForumStructureItem newSection = new ForumStructureItem(new PoulpeSection());
        sut.setSectionTree(tree);
        sut.setSelectedItem(newSection);

        sut.addSelectedSectionToTreeIfNew();
        assertSame(tree.getSelection().iterator().next().getData(), newSection);
    }

    @Test(dataProvider = "provideTreeModelWithSectionsAndBranches")
    public void testRemoveSelectedItem_selectedSection(DefaultTreeModel<ForumStructureItem> treeModel) {
        TreeNode<ForumStructureItem> selectedNode = treeModel.getRoot().getChildAt(1);
        treeModel.addSelectionPath(new int[]{1});
        sut.setSelectedItem(selectedNode.getData());
        sut.setSectionTree(treeModel);

        assertSame(selectedNode.getData(), sut.removeSelectedItem());
        assertEquals(-1, treeModel.getRoot().getIndex(selectedNode));
    }

    @Test(dataProvider = "provideTreeModelWithSectionsAndBranches")
    public void testRemoveSelectedItem_selectedBranch(DefaultTreeModel<ForumStructureItem> treeModel) {
        TreeNode<ForumStructureItem> selectedNode = treeModel.getRoot().getChildAt(1).getChildAt(1);
        treeModel.addSelectionPath(new int[]{1, 1});
        sut.setSelectedItem(selectedNode.getData());
        sut.setSectionTree(treeModel);

        assertSame(selectedNode.getData(), sut.removeSelectedItem());
        assertEquals(-1, treeModel.getRoot().getChildAt(1).getIndex(selectedNode));
    }

    @Test(dataProvider = "provideTreeModelWithSectionsAndBranches")
    public void testGetSectionSelectedInDropDown(DefaultTreeModel<ForumStructureItem> treeModel) throws Exception {
        sut.setSectionTree(treeModel);
        ForumStructureItem itemSelectedInDropdown = treeModel.getRoot().getChildAt(1).getData();
        sut.getSectionList().addToSelection(itemSelectedInDropdown);
        assertSame(itemSelectedInDropdown.getItem(), sut.getSectionSelectedInDropDown());
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
    public void testShowBranchDialog_editingExisting(DefaultTreeModel<ForumStructureItem> treeModel) {
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
    public void testShowBranchDialog_createNew(DefaultTreeModel<ForumStructureItem> treeModel) {
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
    public void testCloseDialogWithBranch(DefaultTreeModel<ForumStructureItem> treeModel) throws Exception {
        treeModel.addSelectionPath(new int[]{0});
        sut.setSectionTree(treeModel);
        sut.showBranchDialog(true);
        sut.closeDialog();
        assertFalse(sut.isShowBranchDialog());
    }

    @Test(dataProvider = "provideTreeModelWithSectionsAndBranches")
    public void testIsShowBranchDialog(DefaultTreeModel<ForumStructureItem> treeModel) throws Exception {
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
    public void testCloseDialogWithSection(DefaultTreeModel<ForumStructureItem> treeModel) throws Exception {
        treeModel.addSelectionPath(new int[]{0});
        sut.setSectionTree(treeModel);
        sut.showSectionDialog(true);
        sut.closeDialog();
        assertFalse(sut.isShowSectionDialog());
    }

    @Test(dataProvider = "provideTreeModelWithSectionsAndBranches")
    public void testSetSectionTree(DefaultTreeModel<ForumStructureItem> treeModel) throws Exception {
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
        return new Object[][]{{new DefaultTreeModel<ForumStructureItem>(buildForumStructure(jcommune))}};
    }

}
