package org.jtalks.poulpe.web.controller.section.mvvm;

import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.ComponentService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.TreeNode;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

/**
 * @author stanislav bashkirtsev
 */
public class ForumStructureVmTest {
    private ComponentService componentService;
    private ForumStructureVm vm;

    @BeforeMethod
    public void setUp() throws Exception {
        componentService = mock(ComponentService.class);
        vm = new ForumStructureVm(componentService);
    }

    @Test(dataProvider = "provideRandomJcommuneWithSections")
    public void testGetSections(Jcommune jcommune) throws Exception {
        when(componentService.getByType(ComponentType.FORUM)).thenReturn(jcommune);
        vm.initForumStructure();
        TreeModel treeModel = vm.getSections();

        TreeNode root = (TreeNode) treeModel.getRoot();
        assertEquals(root.getChildCount(), jcommune.getSections().size());
        assertEquals(root.getChildAt(1).getChildCount(), jcommune.getSections().get(1).getBranches().size());
        Object branch1OfSection0 = root.getChildAt(0).getChildAt(1).getData();
        assertSame(branch1OfSection0, jcommune.getSections().get(0).getBranches().get(1));
    }

    @DataProvider
    public Object[][] provideRandomJcommuneWithSections() {
        Jcommune jcommune = new Jcommune();
        PoulpeSection sectionA = new PoulpeSection("SectionA");
        sectionA.addOrUpdateBranch(new PoulpeBranch("BranchA"));
        sectionA.addOrUpdateBranch(new PoulpeBranch("BranchB"));
        jcommune.addSection(sectionA);
        PoulpeSection sectionB = new PoulpeSection("SectionB");
        sectionB.addOrUpdateBranch(new PoulpeBranch("BranchD"));
        sectionB.addOrUpdateBranch(new PoulpeBranch("BranchE"));
        jcommune.addSection(sectionB);
        return new Object[][]{{jcommune}};
    }
}
