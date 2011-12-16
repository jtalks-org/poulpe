package org.jtalks.poulpe.model.entity;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.dao.hibernate.ObjectsFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

public class BranchModeratorsTest {
    
    private Branch branch = new Branch();
    
    private User user1 = ObjectsFactory.createUser();
    private User user2 = ObjectsFactory.createUser();
    private User user3 = ObjectsFactory.createUser();
    
    @BeforeMethod
    public void setUp() {
        branch.setModerators(Lists.newArrayList(user1));
    }
    
    @Test
    public void isModeratedBy() {
        assertTrue(branch.isModeratedBy(user1));
    }
    
    @Test
    public void addModerator() {
        branch.addModerator(user3);
        assertTrue(branch.isModeratedBy(user3));
    }
    
    @Test
    public void addModerators() {
        branch.addModerators(user2, user3);
        assertTrue(branch.isModeratedBy(user2));
        assertTrue(branch.isModeratedBy(user3));
    }

    @Test
    public void removeModerator() {
        branch.removeModerator(user1);
        assertFalse(branch.isModeratedBy(user1));
    }

}
