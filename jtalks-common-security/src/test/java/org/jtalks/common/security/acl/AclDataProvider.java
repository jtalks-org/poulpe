package org.jtalks.common.security.acl;

import com.google.common.collect.Lists;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.model.permissions.BranchPermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.testng.annotations.DataProvider;

import java.util.List;

/**
 * @author stanislav bashkirtsev
 */
public class AclDataProvider {
    @DataProvider(name = "randomSidsAndPermissions")
    public static Object[][] provideRandomSidsAndPermissionsAndAcl() {
        List<Permission> permissions = Lists.<Permission>newArrayList(
                BranchPermission.VIEW_TOPICS, BranchPermission.CREATE_TOPICS, BranchPermission.DELETE_POSTS
        );
        List sids = (List) provideRandomSids()[0][0];
        return new Object[][]{{sids, permissions}};
    }


    @DataProvider(name = "randomSidsAndPermissionsAndEntity")
    public static Object[][] provideRandomSidsAndPermissionsAndEntity() {
        List<Permission> permissions = Lists.<Permission>newArrayList(
                BranchPermission.VIEW_TOPICS, BranchPermission.CREATE_TOPICS
        );
        List sids = (List) provideRandomSids()[0][0];
        return new Object[][]{{sids, permissions, new EntityImpl(3L)}};
    }

    @DataProvider(name = "randomSids")
    public static Object[][] provideRandomSids() {
        List<Sid> sids = Lists.<Sid>newArrayList(
                new UserGroupSid(1L),
                new UserGroupSid(2L),
                new UserGroupSid(3L)
        );
        return new Object[][]{{sids}};
    }

    @DataProvider(name = "randomEntity")
    public static Object[][] provideRandomEntity() {
        return new Object[][]{{new EntityImpl(2L)}};
    }

    private static class EntityImpl extends Entity {
        private EntityImpl(Long id) {
            setId(id);
        }
    }
}
