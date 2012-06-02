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


    @Test
    public void testBanUsers() throws Exception {

    }

    @DataProvider
    public Object[][] provideGroupWithUsers() {
        Group bannedUsersGroup = new Group();
        bannedUsersGroup.getUsers().addAll(Arrays.asList(
                new PoulpeUser("a", "b", "c", "d"),
                new PoulpeUser("a", "b", "c", "d"),
                new PoulpeUser("a", "b", "c", "d")));
        return new Object[][]{{bannedUsersGroup}};
    }
}
