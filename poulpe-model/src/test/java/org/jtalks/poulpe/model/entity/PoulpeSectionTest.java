package org.jtalks.poulpe.model.entity;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.AssertJUnit.*;

/**
 * @author stanislav bashkirtsev
 */
public class PoulpeSectionTest {
    @Test(dataProvider = "provideFilledSection")
    public void testAddBranchIfAbsent(PoulpeSection section) throws Exception {
        int originalSize = section.getAmountOfBranches();
        PoulpeBranch branchToAdd = new PoulpeBranch("", "");
        section.addOrUpdateBranch(branchToAdd);
        assertEquals(section.getAmountOfBranches(), originalSize + 1);
        assertSame(section.getBranch(originalSize), branchToAdd);
    }

    @Test(dataProvider = "provideFilledSection")
    public void testAddBranchIfAbsent_withAlreadyPresent(PoulpeSection section) throws Exception {
        int originalBranchSize = section.getAmountOfBranches();
        PoulpeBranch branchToAdd = createEqualBranch(section.getBranch(0));

        section.addBranchIfAbsent(branchToAdd);
        assertEquals(section.getAmountOfBranches(), originalBranchSize);
        assertNotSame(section.getBranch(0), branchToAdd);
    }

    @Test(dataProvider = "provideFilledSection")
    public void testContainBranch(PoulpeSection section) throws Exception {
        assertFalse(section.containsBranch(new PoulpeBranch()));
        assertTrue(section.containsBranch(createEqualBranch(section.getBranch(0))));
    }

    private PoulpeBranch createEqualBranch(PoulpeBranch branch) {
        PoulpeBranch branchToAdd = new PoulpeBranch("", "");
        branchToAdd.setUuid(branch.getUuid());
        return branchToAdd;
    }

    @Test(dataProvider = "provideFilledSection")
    public void testGetLastBranch(PoulpeSection section) {
        List<PoulpeBranch> branches = section.getPoulpeBranches();
        assertSame(branches.get(branches.size() - 1), section.getLastBranch());
    }

    @DataProvider
    public Object[][] provideFilledSection() {
        PoulpeSection section = new PoulpeSection("section-name", "section-description");
        section.getBranches().add(new PoulpeBranch("branch-name1"));
        section.getBranches().add(new PoulpeBranch("branch-name2"));
        return new Object[][]{{section}};
    }
}
