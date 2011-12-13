package org.jtalks.poulpe.model.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jtalks.common.model.entity.User;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class BranchModeratorsTest {
    
    private Branch branch = new Branch();
    
    private User user1 = new User("username1", "email1", "password1");
    private User user2 = new User("username2", "email2", "password2");
    private User user3 = new User("username3", "email3", "password3");
    
    @BeforeMethod
    public void setUp() {
        List<User> users = new ArrayList<User>(Arrays.asList(user1));
        branch.setModerators(users);
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
