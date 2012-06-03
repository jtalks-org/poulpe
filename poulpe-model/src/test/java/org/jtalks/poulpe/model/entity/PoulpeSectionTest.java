package org.jtalks.poulpe.model.entity;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.AssertJUnit.*;

/**
 * @author stanislav bashkirtsev
 * @author Guram Savinov
 */
public class PoulpeSectionTest {
    @Test(dataProvider = "provideFilledSection")
    public void testAddBranchIfAbsent(PoulpeSection section) {
        int originalSize = section.getAmountOfBranches();
        PoulpeBranch branchToAdd = new PoulpeBranch("", "");

        section.addBranchIfAbsent(branchToAdd);
        assertEquals(originalSize + 1, section.getAmountOfBranches());
        assertSame(branchToAdd, section.getBranch(originalSize));
        assertSame(section, branchToAdd.getPoulpeSection());
    }

    @Test(dataProvider = "provideFilledSection")
    public void testAddBranchIfAbsent_withNull(PoulpeSection section) {
        int originalBranchSize = section.getAmountOfBranches();

        section.addBranchIfAbsent(null);
        assertEquals(originalBranchSize, section.getAmountOfBranches());
    }

    @Test(dataProvider = "provideFilledSection")
    public void testAddBranchIfAbsent_withAlreadyPresent(PoulpeSection section) {
        int originalBranchSize = section.getAmountOfBranches();
        PoulpeBranch branchToAdd = createEqualBranch(section.getBranch(0));

        section.addBranchIfAbsent(branchToAdd);
        assertEquals(originalBranchSize, section.getAmountOfBranches());
        assertNotSame(branchToAdd, section.getBranch(0));
    }

    @Test(dataProvider = "provideFilledSection")
    public void testContainBranch(PoulpeSection section) {
        assertFalse(section.containsBranch(new PoulpeBranch()));
        assertTrue(section.containsBranch(createEqualBranch(section.getBranch(0))));
    }
    
    @Test(dataProvider = "provideFilledSection")
    public void testGetLastBranch(PoulpeSection section) {
        List<PoulpeBranch> branches = section.getPoulpeBranches();
        assertSame(branches.get(branches.size() - 1), section.getLastBranch());
    }
    
    @Test(dataProvider = "provideFilledSection")
    public void testAddBranchIfAbsentTo(PoulpeSection section) {
        final int indexForAddedBranch = 1;
        List<PoulpeBranch> allBranches = section.getPoulpeBranches();
        PoulpeBranch branchToAdd = new PoulpeBranch("", "");
        PoulpeBranch branchToShift = allBranches.get(indexForAddedBranch);
        
        section.addBranchIfAbsentTo(1, branchToAdd);
        assertSame(branchToAdd, allBranches.get(indexForAddedBranch));
        assertSame(branchToShift, allBranches.get(indexForAddedBranch + 1));
    }

    private PoulpeBranch createEqualBranch(PoulpeBranch branch) {
        PoulpeBranch branchToAdd = new PoulpeBranch("", "");
        branchToAdd.setUuid(branch.getUuid());
        return branchToAdd;
    }

    @DataProvider
    public Object[][] provideFilledSection() {
        PoulpeSection section = new PoulpeSection("section-name", "section-description");
        section.getBranches().add(new PoulpeBranch("branch-name1"));
        section.getBranches().add(new PoulpeBranch("branch-name2"));
        section.getBranches().add(new PoulpeBranch("branch-name3"));
        return new Object[][]{{section}};
    }
}
