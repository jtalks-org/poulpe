package org.jtalks.poulpe.model.entity;

import org.jtalks.common.model.entity.User;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class PoulpeGroupTest {

    @Test
    public void testSetUsers() {
        PoulpeGroup poulpeGroup = new PoulpeGroup();
        poulpeGroup.setUsers(new ArrayList<User>(createPoulpeUsers()));
        isUsersInGroup(poulpeGroup);
    }

    @Test
    public void testAddUsers(){
        PoulpeGroup poulpeGroup = new PoulpeGroup();
        poulpeGroup.addUsers(new ArrayList<User>(createPoulpeUsers()));
        isUsersInGroup(poulpeGroup);
    }

    @Test
    public void testRemoveUsers(){
        PoulpeGroup poulpeGroup = new PoulpeGroup();
        List<PoulpeUser> users = createPoulpeUsers();
        poulpeGroup.setUsers(new ArrayList<User>(users));
        List<PoulpeUser> removeUsers = new ArrayList<PoulpeUser>();
        final int REMOVE_COUNT = 3;

        for(int i=0; i<REMOVE_COUNT; i++){
            removeUsers.add(users.get(i));
        }
        poulpeGroup.removeUsers(new ArrayList<User>(removeUsers));

        for(PoulpeUser u: removeUsers){
            assertFalse(poulpeGroup.getUsers().contains(u));
            assertFalse(u.getGroups().contains(poulpeGroup));
        }

    }

    private void isUsersInGroup(PoulpeGroup poulpeGroup){
        List<User> users = poulpeGroup.getUsers();
        for (User u : users) {
            assertTrue(((PoulpeUser) u).getGroups().contains(poulpeGroup));
        }
    }

    private List<PoulpeUser> createPoulpeUsers() {
        final int USER_COUNT = 10;
        List<PoulpeUser> poulpeUsers = new ArrayList<PoulpeUser>();
        for (int i = 0; i < USER_COUNT; i++) {
            poulpeUsers.add(new PoulpeUser());
        }
        return poulpeUsers;
    }
}
