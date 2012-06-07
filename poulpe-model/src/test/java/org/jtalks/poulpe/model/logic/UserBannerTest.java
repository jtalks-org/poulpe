package org.jtalks.poulpe.model.logic;

import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author stanislav bashkirtsev
 * @author alexandr afanasev
 */
public class UserBannerTest {
    GroupDao groupDao;
    UserBanner sut;

    @BeforeMethod
    public void setUp() throws Exception {
        groupDao = mock(GroupDao.class);
        sut = new UserBanner(groupDao);
    }

    @Test(dataProvider = "provideGroupWithUsers")
    public void testGetBannedUsersGroup(Group bannedUsersGroup) throws Exception {
        doReturn(Arrays.asList(bannedUsersGroup)).when(groupDao).getMatchedByName("Banned Users");
        UserList bannedUsers = sut.getBannedUsers();
        assertEquals(bannedUsers.getUsers(), bannedUsersGroup.getUsers());
    }

    @Test
    public void testGetBannedUsersGroup_withEmpty() throws Exception {
        doReturn(new ArrayList()).when(groupDao).getMatchedByName("Banned Users");
        UserList bannedUsers = sut.getBannedUsers();
        assertTrue(bannedUsers.isEmpty());
        verify(groupDao).saveOrUpdate(any(Group.class));
    }


    @Test (dataProvider = "provideGroupWithUsers")
    public void testBanUsers(Group bannedUsersGroup) throws Exception {
          doReturn(Arrays.asList(bannedUsersGroup)).when(groupDao).getMatchedByName("Banned Users");
          PoulpeUser bannedUser= new PoulpeUser("c","c","c","c");
          sut.banUsers(new UserList(Arrays.asList(bannedUser)));
          bannedUsersGroup.getUsers().add(bannedUser);
          assertEquals(sut.getBannedUsers().getUsers(), bannedUsersGroup.getUsers());
          verify(groupDao).saveOrUpdate(bannedUsersGroup);
    }

    @Test (dataProvider = "provideGroupWithUsers")
    public void testRevokeBan(Group bannedUsersGroup) throws Exception {
        doReturn(Arrays.asList(bannedUsersGroup)).when(groupDao).getMatchedByName("Banned Users");
        PoulpeUser userToRevokeBan=new PoulpeUser("a","b","c","d");
        sut.revokeBan(new UserList(Arrays.asList(userToRevokeBan)));
        bannedUsersGroup.getUsers().removeAll(Arrays.asList(userToRevokeBan));
        assertEquals(sut.getBannedUsers().getUsers(),bannedUsersGroup.getUsers());
        verify(groupDao).saveOrUpdate(bannedUsersGroup);
    }

    @DataProvider
    public Object[][] provideGroupWithUsers() {
        Group bannedUsersGroup = new Group();
        bannedUsersGroup.getUsers().addAll(Arrays.asList(
                new PoulpeUser("a", "b", "c", "d"),
                new PoulpeUser("e", "f", "g", "h"),
                new PoulpeUser("i", "j", "k", "l")));
        return new Object[][]{{bannedUsersGroup}};
    }

}
