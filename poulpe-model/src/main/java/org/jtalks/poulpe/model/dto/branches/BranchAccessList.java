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
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeGroup;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * Access list for {@link PoulpeBranch}
 * 
 * @author stanislav bashkirtsev
 */
public class BranchAccessList {

    private final ConcurrentMap<BranchPermission, GroupAccessList> accessListMap = Maps.newConcurrentMap();

    /**
     * Default constructor, sets nothing
     */
    public BranchAccessList() {
    }

    /**
     * Constructs {@link BranchAccessList} with predefined values to be added to
     * the access list
     * 
     * @param addToAccessList
     */
    public BranchAccessList(Map<BranchPermission, GroupAccessList> addToAccessList) {
        accessListMap.putAll(addToAccessList);
    }

    /**
     * Adds new permission to the access list
     * 
     * @param permission to be added
     * @param toAllow group to allow
     * @param toRestrict group to restrict
     * @return this instance for providing fluent interface
     */
    public BranchAccessList put(BranchPermission permission, PoulpeGroup toAllow, PoulpeGroup toRestrict) {
        this.accessListMap.putIfAbsent(permission, new GroupAccessList());
        GroupAccessList accessList = this.accessListMap.get(permission);
        accessList.addAllowed(toAllow).addRestricted(toRestrict);
        return this;
    }

    /**
     * Adds new 'allowed' permission to the branch
     * 
     * @param permission branch permission
     * @param group group to allow
     * @return this instance for providing fluent interface
     */
    public BranchAccessList addAllowed(BranchPermission permission, PoulpeGroup group) {
        return put(permission, group, null);
    }

    /**
     * Adds new 'restricted' permission to the branch
     * 
     * @param permission branch permission
     * @param group group to restrict
     * @return this instance for providing fluent interface
     */
    public BranchAccessList addRestricted(BranchPermission permission, PoulpeGroup group) {
        return put(permission, null, group);
    }

    /**
     * Based on 'allow' flag, put 'allow' permission on the branch (if it's
     * {@code true}), or puts 'restrict' permission on it (otherwise)
     * 
     * @param permission branch permission
     * @param group permission holder
     * @param allow {@code true} if allowance is needed, {@code false} otherwise
     * @return this instance for providing fluent interface
     */
    public BranchAccessList put(BranchPermission permission, PoulpeGroup group, boolean allow) {
        return allow ? addAllowed(permission, group) : addRestricted(permission, group);
    }

    /**
     * For given permission, retrieves list of {@link PoulpeBranch} object that
     * are allowed
     * 
     * @param permission branch permission
     * @return list of {@link PoulpeBranch}
     */
    public List<PoulpeGroup> getAllowed(BranchPermission permission) {
        return accessListMap.get(permission).getAllowed();
    }

    /**
     * For given permission, retrieves list of {@link PoulpeBranch} object that
     * are restricted
     * 
     * @param permission branch permission
     * @return list of {@link PoulpeBranch}
     */
    public List<PoulpeGroup> getRestricted(BranchPermission permission) {
        return accessListMap.get(permission).getRestricted();
    }

    /**
     * @return all permissions
     */
    public Set<BranchPermission> getPermissions() {
        return accessListMap.keySet();
    }

    /**
     * 
     * Static factory for creating {@link BranchAccessList} with given list of
     * permissions
     * 
     * @param permissions to be added to the access list
     * @return new {@link BranchAccessList} object
     */
    public static BranchAccessList create(List<BranchPermission> permissions) {
        return new BranchAccessList(withEmptyAccessList(permissions));
    }

    /**
     * For each permission created an empty {@link GroupAccessList}
     * 
     * @param permissions to traverse
     * @return map of permissions mapped to empty {@link GroupAccessList}
     * objects
     */
    private static Map<BranchPermission, GroupAccessList> withEmptyAccessList(List<BranchPermission> permissions) {
        Map<BranchPermission, GroupAccessList> newAccessListMap = Maps.newHashMap();
        for (BranchPermission permission : permissions) {
            newAccessListMap.put(permission, new GroupAccessList());
        }
        return newAccessListMap;
    }
}
