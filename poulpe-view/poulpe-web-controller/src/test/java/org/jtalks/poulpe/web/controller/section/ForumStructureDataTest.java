package org.jtalks.poulpe.web.controller.section;

import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.TreeNode;

import java.util.Arrays;
import java.util.List;

import static org.jtalks.poulpe.web.controller.section.TreeNodeFactory.buildForumStructure;
import static org.mockito.Mockito.doReturn;
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
