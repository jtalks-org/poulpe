package org.jtalks.poulpe.model.entity;

import com.google.common.collect.Lists;
import org.jtalks.common.model.entity.Group;
import org.springframework.util.SerializationUtils;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
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
    public void theEntityFieldsShouldBeSerialized(){
        PoulpeUser user = PoulpeUser.withId(1);
        byte[] serialize = SerializationUtils.serialize(user);
        PoulpeUser serializedUser = (PoulpeUser)SerializationUtils.deserialize(serialize);
        assertReflectionEquals(user, serializedUser);
    }

}
