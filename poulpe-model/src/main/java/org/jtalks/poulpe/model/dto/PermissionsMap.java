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
package org.jtalks.poulpe.model.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.permissions.JtalksPermission;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeGroup;

import com.google.common.collect.Maps;

/**
 * Container for permissions and related to it access lists. Contains several methods to simple management for access
 * lists.
 * 
 * @author Vyacheslav Zhivaev
 */
public class PermissionsMap<T extends JtalksPermission> {

    protected final ConcurrentMap<T, GroupAccessList> accessListMap = Maps.newConcurrentMap();

    /**
     * Default constructor, sets nothing.
     */
    public PermissionsMap() {
    }

    /**
     * Constructs {@link PermissionsMap} with predefined values to be added to the access list.
     * 
     * @param addToAccessList
     */
    public PermissionsMap(Map<T, GroupAccessList> addToAccessList) {
        accessListMap.putAll(addToAccessList);
    }

    /**
     * Adds new permission to the access list.
     * 
     * @param permission to be added
     * @param toAllow group to allow
     * @param toRestrict group to restrict
     * @return this instance for providing fluent interface
     */
    public PermissionsMap<T> put(T permission, PoulpeGroup toAllow, PoulpeGroup toRestrict) {
        this.accessListMap.putIfAbsent(permission, new GroupAccessList());
        GroupAccessList accessList = this.accessListMap.get(permission);
        accessList.addAllowed(toAllow).addRestricted(toRestrict);
        return this;
    }

    /**
     * Adds new 'allowed' permission.
     * 
     * @param permission the permission
     * @param group group to allow
     * @return this instance for providing fluent interface
     */
    public PermissionsMap<T> addAllowed(T permission, PoulpeGroup group) {
        return put(permission, group, null);
    }

    /**
     * Adds new 'restricted' permission.
     * 
     * @param permission the permission
     * @param group group to restrict
     * @return this instance for providing fluent interface
     */
    public PermissionsMap<T> addRestricted(T permission, PoulpeGroup group) {
        return put(permission, null, group);
    }

    /**
     * Based on 'allow' flag, put 'allow' permission on the branch (if it's {@code true}), or puts 'restrict' permission
     * on it (otherwise).
     * 
     * @param permission the permission
     * @param group permission holder
     * @param allow {@code true} if allowance is needed, {@code false} otherwise
     * @return this instance for providing fluent interface
     */
    public PermissionsMap<T> put(T permission, PoulpeGroup group, boolean allow) {
        return allow ? addAllowed(permission, group) : addRestricted(permission, group);
    }

    /**
     * For given permission, retrieves list of {@link Component} object that are allowed.
     * 
     * @param permission the permission
     * @return list of {@link PoulpeBranch}
     */
    public List<PoulpeGroup> getAllowed(T permission) {
        return accessListMap.get(permission).getAllowed();
    }

    /**
     * For given permission, retrieves list of {@link Component} object that are restricted.
     * 
     * @param permission the permission
     * @return list of {@link PoulpeBranch}
     */
    public List<PoulpeGroup> getRestricted(T permission) {
        return accessListMap.get(permission).getRestricted();
    }

    /**
     * Gets set of permissions.
     * 
     * @return all permissions
     */
    public Set<T> getPermissions() {
        return accessListMap.keySet();
    }

    /**
     * Static factory for creating {@link PermissionsMap} with given list of permissions.
     * 
     * @param permissions to be added to the access list
     * @return new {@link PermissionsMap} object
     */
    public static <T extends JtalksPermission> PermissionsMap<T> create(List<T> permissions) {
        return new PermissionsMap<T>(withEmptyAccessList(permissions));
    }

    /**
     * For each permission created an empty {@link GroupAccessList}.
     * 
     * @param permissions to traverse
     * @return map of permissions mapped to empty {@link GroupAccessList} objects
     */
    protected static <T extends JtalksPermission> Map<T, GroupAccessList> withEmptyAccessList(List<T> permissions) {
        Map<T, GroupAccessList> newAccessListMap = Maps.newHashMap();
        for (T permission : permissions) {
            newAccessListMap.put(permission, new GroupAccessList());
        }
        return newAccessListMap;
    }
}
