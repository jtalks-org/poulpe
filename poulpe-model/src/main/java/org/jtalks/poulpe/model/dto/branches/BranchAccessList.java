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

import com.google.common.collect.Maps;

import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.poulpe.model.entity.PoulpeGroup;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author stanislav bashkirtsev
 */
public class BranchAccessList {
    private final ConcurrentMap<BranchPermission, GroupAccessList> accessListMap = Maps.newConcurrentMap();

    public BranchAccessList() {
    }

    public BranchAccessList(Map<BranchPermission, GroupAccessList> addToAccessList) {
        accessListMap.putAll(addToAccessList);
    }

    public BranchAccessList put(BranchPermission permission, PoulpeGroup toAllow, PoulpeGroup toRestrict) {
        this.accessListMap.putIfAbsent(permission, new GroupAccessList());
        GroupAccessList accessList = this.accessListMap.get(permission);
        accessList.addAllowed(toAllow).addRestricted(toRestrict);
        return this;
    }

    public BranchAccessList addAllowed(BranchPermission permission, PoulpeGroup group) {
        return put(permission, group, null);
    }

    public BranchAccessList addRestricted(BranchPermission permission, PoulpeGroup group) {
        return put(permission, null, group);
    }

    public BranchAccessList put(BranchPermission permission, PoulpeGroup group, boolean allow) {
        return allow ? addAllowed(permission, group) : addRestricted(permission, group);
    }

    public List<PoulpeGroup> getAllowed(BranchPermission permission) {
        return accessListMap.get(permission).getAllowed();
    }

    public List<PoulpeGroup> getRestricted(BranchPermission permission) {
        return accessListMap.get(permission).getRestricted();
    }

    public Set<BranchPermission> getPermissions() {
        return accessListMap.keySet();
    }

    public static BranchAccessList create(List<BranchPermission> permissions) {
        return new BranchAccessList(withEmptyAccessList(permissions));
    }

    private static Map<BranchPermission, GroupAccessList> withEmptyAccessList(List<BranchPermission> permissions) {
        Map<BranchPermission, GroupAccessList> newAccessListMap = Maps.newHashMap();
        for (BranchPermission permission : permissions) {
            newAccessListMap.put(permission, new GroupAccessList());
        }
        return newAccessListMap;
    }
}
