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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jtalks.common.model.permissions.JtalksPermission;
import org.jtalks.poulpe.model.entity.PoulpeGroup;

import com.google.common.collect.ImmutableList;

/**
 * DTO container represents changes which needs to be provided by ACL level for specified {@link JtalksPermission}.
 * 
 * @author stanislav bashkirtsev
 * @author Vyacheslav Zhivaev
 */
public class AclChangeset {
    private final JtalksPermission permission;
    private final List<PoulpeGroup> newlyAddedGroups = new ArrayList<PoulpeGroup>();
    private final List<PoulpeGroup> removedGroups = new ArrayList<PoulpeGroup>();

    /**
     * Constructs the object with given {@link JtalksPermission} instance.
     * 
     * @param permission type of permission
     */
    public AclChangeset(JtalksPermission permission) {
        this.permission = permission;
    }

    /**
     * Gets newly added permissions.
     * 
     * @return PoulpeGroup[] with newly permissions
     */
    public PoulpeGroup[] getNewlyAddedGroupsAsArray() {
        return newlyAddedGroups.toArray(new PoulpeGroup[newlyAddedGroups.size()]);
    }

    /**
     * Sets newly added permissions.
     * 
     * @param newlyAddedGroups - list of newly added permissions
     */
    public void setNewlyAddedGroups(Collection<PoulpeGroup> newlyAddedGroups) {
        this.newlyAddedGroups.addAll(newlyAddedGroups);
    }

    /**
     * Gets removed permissions.
     * 
     * @return PoulpeGroup[] with removed permissions
     */
    public List<PoulpeGroup> getRemovedGroups() {
        return ImmutableList.copyOf(removedGroups);
    }

    /**
     * Gets removed permissions.
     * 
     * @return PoulpeGroup[] with removed permissions
     */
    public PoulpeGroup[] getRemovedGroupsAsArray() {
        return removedGroups.toArray(new PoulpeGroup[removedGroups.size()]);
    }

    /**
     * Sets removed permissions.
     * 
     * @param removedGroups - list with removed permissions
     */
    public void setRemovedGroups(Collection<PoulpeGroup> removedGroups) {
        this.removedGroups.addAll(removedGroups);
    }

    /**
     * Gets permission.
     * 
     * @return {@link JtalksPermission}
     */
    public JtalksPermission getPermission() {
        return permission;
    }

    /**
     * Checks {@link AclChangeset} is empty.
     * 
     * @return {@code true} if empty, else return {@code false}
     */
    public boolean isEmpty() {
        return removedGroups.isEmpty() && newlyAddedGroups.isEmpty();
    }
}
