package org.jtalks.poulpe.web.controller.section.dialogs;

import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.web.controller.section.ForumStructureItem;
import org.jtalks.poulpe.web.controller.zkutils.ZkTreeModel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.jtalks.poulpe.web.controller.section.TreeNodeFactory.buildForumStructure;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.*;

/**
 * @author stanislav bashkirtsev
 */
public class BranchEditingDialogTest {
    private GroupDao groupDao;
    private BranchEditingDialog sut;

    @BeforeMethod
    public void setUp() throws Exception {
        groupDao = mock(GroupDao.class);
        sut = new BranchEditingDialog(groupDao);
        sut.renewSectionsFromTree((ZkTreeModel<ForumStructureItem>) provideTreeModelWithSectionsAndBranches()[0][0]);
    }

    @Test(dataProvider = "provideTreeModelWithSectionsAndBranches")
    public void testRenewSectionsFromTree(ZkTreeModel<ForumStructureItem> treeModel) throws Exception {
        sut.renewSectionsFromTree(treeModel);
        assertEquals(sut.getSectionList().size(), treeModel.getRoot().getChildCount());
    }

    @Test(dataProvider = "provideGroups")
    public void testGetCandidatesToModerate(List<Group> givenGroups) throws Exception {
        doReturn(givenGroups).when(groupDao).getAll();

        List<Group> candidatesToModerate = sut.showDialog().getCandidatesToModerate();
        assertEquals(candidatesToModerate, givenGroups);
    }

    @Test(dataProvider = "provideBranchWithModeratingGroup")
    public void getModeratorsGroupShouldReturnGroupFromBranch(PoulpeBranch branch) {
        doReturn(Arrays.asList(branch.getModeratorsGroup())).when(groupDao).getAll();
        sut.showDialog().setEditedBranch(new ForumStructureItem(branch));
        assertEquals(sut.getModeratingGroup(), branch.getModeratorsGroup());
    }

    /**
     * If the branch doesn't contain a moderating group yet, null should be returned.
     */
    @Test
    public void getModeratorsGroupShouldNull() {
        sut.setEditedBranch(new ForumStructureItem(new PoulpeBranch("test-branch")));
        assertNull(sut.getModeratingGroup());
    }

    @Test
    public void isShowingDialogShouldChangeFlagAfterFirstInvocation() {
        sut.showDialog();
        assertTrue(sut.isShowDialog());
        assertFalse(sut.isShowDialog());
    }

    @Test
    public void testGetSectionSelectedInDropdown() throws Exception {
        ForumStructureItem toBeSelected = sut.getSectionList().get(1);
        sut.getSectionList().addToSelection(toBeSelected);
        assertSame(sut.getSectionSelectedInDropdown(), toBeSelected);
    }

    @DataProvider
    public Object[][] provideBranchWithModeratingGroup() {
        PoulpeBranch branch = new PoulpeBranch("branch");
        branch.setModeratorsGroup(new Group("group"));
        return new Object[][]{{branch}};
    }

    @DataProvider
    public Object[][] provideGroups() {
        return new Object[][]{{Arrays.asList(new Group("g1"), new Group("g2"))}};
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
}
