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

import com.google.common.collect.ImmutableList;

import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.model.permissions.JtalksPermission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author stanislav bashkirtsev
 */
public class BranchAccessChanges {
    private final JtalksPermission permission;
    private final List<Group> newlyAddedGroups = new ArrayList<Group>();
    private final List<Group> removedGroups = new ArrayList<Group>();

    public BranchAccessChanges(JtalksPermission permission) {
        this.permission = permission;
    }

    /**
     * Get newly added permissions
     *
     * @return Group[] with newly permissions
     */
    public Group[] getNewlyAddedGroupsAsArray() {
        return newlyAddedGroups.toArray(new Group[]{});
    }

    /**
     * Set newly added permissions
     *
     * @param newlyAddedGroups - list of newly added permissions
     */
    public void setNewlyAddedGroups(Collection<Group> newlyAddedGroups) {
        this.newlyAddedGroups.addAll(newlyAddedGroups);
    }

    /**
     * Get removed permissions
     *
     * @return Group[] with removed permissions
     */
    public List<Group> getRemovedGroups() {
        return ImmutableList.copyOf(removedGroups);
    }

    /**
     * Get removed permissions
     *
     * @return Group[] with removed permissions
     */
    public Group[] getRemovedGroupsAsArray() {
        return removedGroups.toArray(new Group[]{});
    }

    /**
     * Set removed permissions
     *
     * @param removedGroups - list with removed permissions
     */
    public void setRemovedGroups(Collection<Group> removedGroups) {
        this.removedGroups.addAll(removedGroups);
    }

    /**
     * Get permission
     *
     * @return {@link JtalksPermission}
     */
    public JtalksPermission getPermission() {
        return permission;
    }

    /**
     * Check {@link BranchAccessChanges} is empty
     *
     * @return {@code true} if empty, else return {@code false}
     */
    public boolean isEmpty() {
        return removedGroups.isEmpty() && newlyAddedGroups.isEmpty();
    }
}
