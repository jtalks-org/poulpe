/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
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

    @DataProvider(name = "notSavedEntity")
    public static Object[][] provideNotSavedEntity() {
        return new Object[][]{{new EntityImpl(0L)}};
    }

    private static class EntityImpl extends Entity {
        private EntityImpl(Long id) {
            setId(id);
        }
    }
}
