package org.jtalks.poulpe.model.entity;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.entity.User;
import org.springframework.util.SerializationUtils;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.testng.Assert.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

/**
 * @author stanislav bashkirtsev
 */
public class PoulpeUserTest {
    @Test
    public void userShouldBeInGroup() {
        PoulpeUser user = new PoulpeUser();
        user.setGroups(Lists.newArrayList(groupWithId(1), groupWithId(100), groupWithId(200)));
        assertTrue(user.isInGroupWithId(100));
    }

    @Test
    public void userShouldNotBeInGroup() {
        PoulpeUser user = new PoulpeUser();
        user.setGroups(Lists.newArrayList(groupWithId(1), groupWithId(100), groupWithId(200)));
        assertFalse(user.isInGroupWithId(99));
    }

    public Group groupWithId(long id) {
        Group group = new Group();
        group.setId(id);
        return group;
    }

    @Test
    public void theEntityFieldsShouldBeSerialized() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PoulpeUser user = new PoulpeUser("username","email","password","salt");
        user.setId(1);
        user.setAvatar(new byte[]{1});
        user.setVersion(1L);
        user.setFirstName("First Name");
        user.setLastName("Last Name");
        user.setBanReason("Ban Reason");
        user.setEnabled(true);
        user.setRole("Role");
        Method setLastLogin = User.class.getDeclaredMethod("setLastLogin", DateTime.class);
        Method setEncodedUsername = User.class.getDeclaredMethod("setEncodedUsername", String.class);
        setLastLogin.setAccessible(true);
        setEncodedUsername.setAccessible(true);
        setLastLogin.invoke(user,DateTime.now());
        setEncodedUsername.invoke(user,"Encoded Username");

        byte[] serialize = SerializationUtils.serialize(user);
        PoulpeUser serializedUser = (PoulpeUser)SerializationUtils.deserialize(serialize);
        assertReflectionEquals(user, serializedUser);
    }

    @Test
    public void groupsFieldIsNotSerializable(){
        List<Group> groups = PoulpeGroup.createGroupsWithNames("The Group");
        PoulpeUser userInGroup = PoulpeUser.withId(1);
        userInGroup.setGroups(groups);

        byte[] serialize = SerializationUtils.serialize(userInGroup);
        PoulpeUser serializedUser = (PoulpeUser) SerializationUtils.deserialize(serialize);

        assertNull(serializedUser.getGroups(), "After deserialiation, the transient field `List<Group> groups` must be null");
    }
}
