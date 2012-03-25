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
package org.jtalks.poulpe.model.dto.branches;

import java.util.List;
import java.util.Map;

import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.poulpe.model.dto.GroupAccessList;
import org.jtalks.poulpe.model.dto.PermissionsMap;
import org.jtalks.poulpe.model.entity.PoulpeBranch;

/**
 * Access list for {@link PoulpeBranch}
 * 
 * @see org.jtalks.poulpe.model.dto.PermissionsMap
 * @author stanislav bashkirtsev
 * @author Vyacheslav Zhivaev
 */
public class BranchPermissions extends PermissionsMap<BranchPermission> {

    /**
     * Default constructor, sets nothing
     */
    public BranchPermissions() {
    }

    /**
     * Constructs {@link BranchPermissions} with predefined values to be added to the access list
     * 
     * @param addToAccessList
     */
    public BranchPermissions(Map<BranchPermission, GroupAccessList> addToAccessList) {
        accessListMap.putAll(addToAccessList);
    }

    /**
     * 
     * Static factory for creating {@link BranchPermissions} with given list of permissions
     * 
     * @param permissions to be added to the access list
     * @return new {@link BranchPermissions} object
     */
    public static BranchPermissions create(List<BranchPermission> permissions) {
        return new BranchPermissions(withEmptyAccessList(permissions));
    }
}
