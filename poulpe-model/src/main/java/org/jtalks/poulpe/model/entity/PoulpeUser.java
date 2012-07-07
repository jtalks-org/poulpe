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
package org.jtalks.poulpe.model.entity;

import org.jtalks.common.model.entity.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores information about the user.
 */
public class PoulpeUser extends org.jtalks.common.model.entity.User {
    private List<Group> groups = new ArrayList<Group>();

    /**
     * Only for hibernate usage.
     */
    public PoulpeUser() {
        super();
    }

    /**
     * Create instance with requiered fields.
     *
     * @param username username
     * @param email    email
     * @param password password
     * @param salt     salt
     */
    public PoulpeUser(String username, String email, String password, String salt) {
        super(username, email, password, salt);
    }

    /**
     * Gets list of groups assigned to user.
     *
     * @return the list of groups assigned to user
     */
    public List<Group> getGroups() {
        return groups;
    }

    /**
     * Defines whether user resides in a group with the specified ID.
     *
     * @param groupId an ID of the group to find from the list of group user is in
     * @return true if user is a member of the group with specified id
     */
    public boolean isInGroupWithId(long groupId) {
        for (Group userGroup : getGroups()) {
            if (userGroup.getId() == groupId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets list of groups assigned to user.
     *
     * @param groups the new list of groups to set
     */

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "PoulpeUser [id=" + getId() + ", email=" + getEmail() + ", username=" + getUsername() + "]";
    }

}
