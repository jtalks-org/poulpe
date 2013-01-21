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
package org.jtalks.poulpe.web.controller.section.dialogs;

import org.jtalks.common.model.entity.Group;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * A list of groups with useful methods that are absent in usual list.
 *
 * @author stanislav bashkirtsev
 */
public class GroupList {
    private final List<Group> groups = new ArrayList<Group>();

    /**
     * Clear current list and fill anew with received list 
     * @param groups new list of groups
     * @return {@code this}
     */
    public GroupList setGroups(List<Group> groups) {
        this.groups.clear();
        this.groups.addAll(groups);
        return this;
    }

    /** @return current groups */
    public List<Group> getGroups() {
        return groups;
    }

    /**
     * Finds and returns a group that is equal or the same in the list.
     *
     * @param group a group to find its equal counterpart
     * @return a group in the list that is equal to the specified one or {@code null} if such group wasn't found
     */
    public Group getEqual(@Nullable Group group) {
        if (group == null) {
            return null;
        }
        for (Group next : groups) {
            if (next.getUuid().equals(group.getUuid())) {
                return next;
            }
        }
        return null;
    }
}
