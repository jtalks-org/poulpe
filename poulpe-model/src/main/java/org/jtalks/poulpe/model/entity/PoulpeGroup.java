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
import org.jtalks.common.model.entity.User;

import java.util.List;

/**
 * This is a group for a poulpe.
 */
public class PoulpeGroup extends Group {

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUsers(List<User> users) {
        super.setUsers(setThisGroupOfUsers(users));
    }

    /**
     * The method adds users to this group. This group is added to the users too.
     *
     * @param users list users
     */
    public void addUsers(List<User> users) {
        super.getUsers().addAll(setThisGroupOfUsers(users));
    }

    /**
     * The method removes users from this group. This group removed from the users too.
     *
     * @param users users for remove
     */
    public void removeUsers(List<User> users) {
        super.getUsers().removeAll(removeThisGroupFromUsers(users));
    }

    /**
     * Set this group of users
     *
     * @param users list users
     * @return users with this group
     */
    private List<User> setThisGroupOfUsers(List<User> users) {
        PoulpeUser poulpeUser = null;
        for (User user : users) {
            poulpeUser = (PoulpeUser) user;
            if (!poulpeUser.getGroups().contains(this)) {
                poulpeUser.getGroups().add(this);
            }
        }
        return users;
    }

    /**
     * The method removes this group from users.
     *
     * @param users list users
     * @return users without this group.
     */
    private List<User> removeThisGroupFromUsers(List<User> users) {
        PoulpeUser poulpeUser = null;
        for (User user : users) {
            poulpeUser = (PoulpeUser) user;
            if (poulpeUser.getGroups().contains(this)) {
                poulpeUser.getGroups().remove(this);
            }
        }
        return users;
    }
}
