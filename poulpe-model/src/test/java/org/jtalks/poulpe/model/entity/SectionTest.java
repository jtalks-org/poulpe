package org.jtalks.poulpe.model.entity;

import org.jtalks.poulpe.model.dao.hibernate.ObjectsFactory;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class SectionTest {
    private Section section;
    private List<Branch> branches = new ArrayList<Branch>();

    @Test
    public void testDeleteBranch(){
        section = ObjectsFactory.createSection();
        assertEquals(section.getBranches().size(), 0);
        section.addBranch(new Branch());
        section.addBranch(new Branch());
        assertEquals(section.getBranches().size(), 2);
        section.deleteBranch(section.getBranches().get(1));
        assertEquals(section.getBranches().size(), 1);

    }
}
