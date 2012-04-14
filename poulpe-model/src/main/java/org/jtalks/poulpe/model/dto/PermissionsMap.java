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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.jtalks.common.model.permissions.JtalksPermission;
import org.jtalks.poulpe.model.entity.PoulpeGroup;

import com.google.common.collect.Maps;

/**
 * Container for permissions and related to it access lists. Contains several methods to simple management of access
 * lists.
 * 
 * @author Vyacheslav Zhivaev
 */
public class PermissionsMap<T extends JtalksPermission> {

    private final ConcurrentMap<T, GroupAccessList> accessListMap = Maps.newConcurrentMap();

    /**
     * Constructs {@link PermissionsMap} with empty internal state. Use add* methods to fill this map.
     */
    public PermissionsMap() {
        // NOOP
    }

    /**
     * Constructs {@link PermissionsMap} with given list of permissions and empty restrict\allow data.
     * 
     * @param permissions to be added to the access lists
     */
    public PermissionsMap(List<T> permissions) {
        for (T permission : permissions) {
            accessListMap.put(permission, new GroupAccessList());
        }
    }

    /**
     * Constructs {@link PermissionsMap} with predefined values to be added to the access lists.
     * 
     * @param accessLists values to initialize this container
     */
    public PermissionsMap(Map<T, GroupAccessList> accessLists) {
        accessListMap.putAll(accessLists);
    }

    /**
     * Adds new permission to the access list.
     * 
     * @param permission to be added
     * @param toAllow group to allow
     * @param toRestrict group to restrict
     * @return {@code this} instance for providing fluent interface
     */
    public PermissionsMap<T> add(T permission, PoulpeGroup toAllow, PoulpeGroup toRestrict) {
        accessListMap.putIfAbsent(permission, new GroupAccessList());
        accessListMap.get(permission).addAllowed(toAllow).addRestricted(toRestrict);
        return this;
    }

    /**
     * Adds new 'allowed' permission.
     * 
     * @param permission the permission to add
     * @param group the group for which permission added
     * @return {@code this} instance for providing fluent interface
     */
    public PermissionsMap<T> addAllowed(T permission, PoulpeGroup group) {
        return add(permission, group, null);
    }

    /**
     * Adds new 'restricted' permission.
     * 
     * @param permission the permission to add
     * @param group the group for which permission added
     * @return {@code this} instance for providing fluent interface
     */
    public PermissionsMap<T> addRestricted(T permission, PoulpeGroup group) {
        return add(permission, null, group);
    }

    /**
     * Based on 'allow' flag, add 'allow' permission (if it's {@code true}), or 'restrict' permission on it (otherwise).
     * 
     * @param permission the permission to add
     * @param group the group for which permission added
     * @param allow {@code true} if allowance is needed, {@code false} otherwise
     * @return {@code this} instance for providing fluent interface
     */
    public PermissionsMap<T> add(T permission, PoulpeGroup group, boolean allow) {
        return (allow) ? addAllowed(permission, group) : addRestricted(permission, group);
    }

    /**
     * For given permission, retrieves list of {@link PoulpeGroup} that are allowed.
     * 
     * @param permission the permission to get for
     * @return list of {@link PoulpeGroup}, list instance is UNMODIFIABLE
     */
    public List<PoulpeGroup> getAllowed(T permission) {
        return Collections.unmodifiableList(accessListMap.get(permission).getAllowed());
    }

    /**
     * For given permission, retrieves list of {@link PoulpeGroup} that are restricted.
     * 
     * @param permission the permission to get for
     * @return list of {@link PoulpeGroup}, list instance is UNMODIFIABLE
     */
    public List<PoulpeGroup> getRestricted(T permission) {
        return Collections.unmodifiableList(accessListMap.get(permission).getRestricted());
    }

    /**
     * For given permission, retrieves list of {@link PoulpeGroup} that are allowed or restricted relative to parameter
     * {@code allowed}.
     * 
     * @param permission the permission to get for
     * @param allowed the flag indicating which type of groups needed: allowed (if {@code true}) or restricted
     * @return list of {@link PoulpeGroup}, list instance is UNMODIFIABLE
     */
    public List<PoulpeGroup> get(T permission, boolean allowed) {
        return (allowed) ? getAllowed(permission) : getRestricted(permission);
    }

    /**
     * Gets all permissions defined in this map.
     * 
     * @return set of all permissions defined in this map, set instance is UNMODIFIABLE
     */
    public Set<T> getPermissions() {
        return Collections.unmodifiableSet(accessListMap.keySet());
    }
}
