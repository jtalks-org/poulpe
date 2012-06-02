package org.jtalks.poulpe.model.logic;

import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * @author stanislav bashkirtsev
 */
public class UserListTest {

    @Test(dataProvider = "provideUsers")
    public void testConstruction(List<PoulpeUser> users) throws Exception {
        UserList list = new UserList(users);
        assertEquals(list.getUsers(), users);
    }

    @Test(dataProvider = "provideUsers")
    public void testConstruction_withArray(List<PoulpeUser> users) throws Exception {
        UserList list = new UserList(users.toArray(new PoulpeUser[]{}));
        assertEquals(list.getUsers(), users);
    }

    @Test(dataProvider = "provideUsers")
    public void ofCommonUsersShouldCast(List<User> users) throws Exception {
        UserList sut = UserList.ofCommonUsers(users);
        assertEquals(sut.getUsers(), users);
    }

    @Test(expectedExceptions = ClassCastException.class)
    public void ofCommonUsersShouldThrowIfUsersAreNotCommon() throws Exception {
        UserList.ofCommonUsers(Arrays.asList(new User("", "", "", "")));
    }

    @DataProvider
    public Object[][] provideUsers() {
        List<PoulpeUser> usersToAdd = Arrays.asList(
                new PoulpeUser("a", "b", "c", "d"),
                new PoulpeUser("a", "b", "c", "d"),
                new PoulpeUser("a", "b", "c", "d"));
        return new Object[][]{{usersToAdd}};
    }
}
