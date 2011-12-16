package org.jtalks.poulpe.service.movetocommon;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Iterables.filter;
import static org.testng.AssertJUnit.*;

/**
 * @author stanislav bashkirtsev
 */
public class JtalksPermissionTest {
    @Test
    public void testConstructorWithString() throws Exception {
        assertEquals(new JtalksPermission("111", "").getMask(), 7);
    }

    @Test
    public void testGetInvertedForPermission() throws Exception {
        JtalksPermission permission = new JtalksPermission(10, "permission");
        assertEquals(permission.getInverted().getMask(), -11);
        assertEquals(permission.getInverted().getName(), "RESTRICTED_permission");
    }

    @Test
    public void testGetInvertedForRestriction() {
        JtalksPermission restriction = new JtalksPermission(-100, "RESTRICTED_permission");
        assertEquals(restriction.getInverted().getMask(), 99);
        assertEquals(restriction.getInverted().getName(), "permission");
    }

    /**
     * Looks for all the permission classes within JTalks, reads all their constants and compares to each other making
     * sure that there are no duplicates (those that have the same bit mask).
     *
     * @param permissions all the permissions in the project to compare to each other
     * @throws Exception these are tests, who would care
     */
    @Test(dataProvider = "allProjectPermissions")
    public void testNoIdenticalConstants(List<JtalksPermission> permissions) throws Exception {
        for (int i = 0; i < permissions.size(); i++) {
            for (int j = i + 1; j < permissions.size(); j++) {
                JtalksPermission first = permissions.get(i);
                JtalksPermission second = permissions.get(j);
                assertNotSame("Permissions with identical bit mask were encountered: "
                        + first.getName() + " & " + second.getName(), first.getMask(), second.getMask());
            }
        }
    }

    /**
     * The permission's mask are allowed to be anything, but they can't have '1' in the last bit (the sign). This bit is
     * for restrictions ({@link org.jtalks.poulpe.service.movetocommon.JtalksPermission#getInverted}.
     *
     * @param permissions all the permissions in the project to check their first bit
     * @throws Exception who cares about exceptions in tests?
     */
    @Test(dataProvider = "allProjectPermissions")
    public void testPermissionsMaskCorrect(List<JtalksPermission> permissions) throws Exception {
        for (JtalksPermission permission : permissions) {
            assertTrue("The mask is negative number which means permission's greatest bit is 1. Permissions has " +
                    "incorrect mask: " + permission.getName(), permission.getMask() >= 0);
        }
    }

    /**
     * This method looks at all the classes that contain {@link JtalksPermission}, looks up for its declared fields,
     * then filters out those that are not of type {@link JtalksPermission} and returns only permissions. If you
     * implement another such class which contains permissions, then you should add it to this method so that your class
     * will be tested as well.
     *
     * @return all the instances-constants of {@link JtalksPermission} from all the classes in the project
     */
    @DataProvider(name = "allProjectPermissions")
    protected Object[][] getAllProjectPermissions() {
        ArrayList<Field> fields = Lists.newArrayList(JtalksPermission.class.getDeclaredFields());
        List<JtalksPermission> permissions = Lists.transform(fields, new PermissionsFromFieldsTransformer());
        permissions = Lists.newArrayList(filter(permissions, Predicates.notNull()));
        return new Object[][]{{permissions}};
    }

    /**
     * Gets the value of the Field and returns it as  the {@link JtalksPermission}.
     */
    private static class PermissionsFromFieldsTransformer implements Function<Field, JtalksPermission> {
        /**
         * {@inheritDoc}
         */
        public JtalksPermission apply(Field item) {
            item.setAccessible(true);
            if (item.getType() == JtalksPermission.class) {
                try {
                    return (JtalksPermission) item.get(new JtalksPermission(1, "test"));
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            return null;
        }
    }
}
