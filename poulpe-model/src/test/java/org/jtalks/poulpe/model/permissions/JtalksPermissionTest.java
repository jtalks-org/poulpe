package org.jtalks.poulpe.model.permissions;

import org.jtalks.poulpe.model.permissions.BranchPermission;
import org.jtalks.poulpe.model.permissions.JtalksPermission;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.AssertJUnit.assertNotSame;

/**
 * @author stanislav bashkirtsev
 */
public class JtalksPermissionTest {
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
     * This method looks at all the classes that contain {@link JtalksPermission}, looks up for its declared fields,
     * then filters out those that are not of type {@link JtalksPermission} and returns only permissions. If you
     * implement another such class which contains permissions, then you should add it to this method so that your class
     * will be tested as well.
     *
     * @return all the instances-constants of {@link JtalksPermission} from all the classes in the project
     */
    @DataProvider(name = "allProjectPermissions")
    protected Object[][] getAllProjectPermissions() {
        List<? extends JtalksPermission> permissions = BranchPermission.getAllAsList();
        return new Object[][]{{permissions}};
    }

}
