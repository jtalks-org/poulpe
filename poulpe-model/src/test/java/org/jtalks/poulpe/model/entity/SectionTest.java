package org.jtalks.poulpe.model.entity;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jtalks.poulpe.model.dao.hibernate.ObjectsFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SectionTest {
    private Section section;
    private Branch branch = new Branch();

    @BeforeMethod
    public void setUp() {
        section = ObjectsFactory.createSection();
    }
    
    @Test
    public void testAddBranch() {
        section.addBranch(branch);
        assertEquals(section.getBranches().size(), 1);
    }
    
    @Test
    public void testDeleteBranch(){
        section.addBranch(branch);
        section.deleteBranch(branch);
        
        assertTrue(section.getBranches().isEmpty());
    }
}
