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

import java.util.ArrayList;
import java.util.List;


/**
 * Stores information about the user.
 *
 * @author Vyacheslav Zhivaev
 *
 */
public class User extends org.jtalks.common.model.entity.User {

    private static final long serialVersionUID = -6429539956660665057L;

    private List<PoulpeGroup> groups = new ArrayList<PoulpeGroup>();

    /**
     * Only for hibernate usage.
     */
    public User() {
        super();
    }

    /**
     * Create instance with requiered fields.
     *
     * @param username username
     * @param email    email
     * @param password password
     * @deprecated
     */
    @Deprecated
    public User(String username, String email, String password) {
        super(username, email, password);
    }

    /**
     * Create instance with requiered fields.
     *
     * @param username username
     * @param email    email
     * @param password password
     * @param salt     salt
     */
    public User(String username, String email, String password, String salt) {
        super(username, email, password, salt);
    }

    /**
     * Gets list of groups assigned to user.
     *
     * @return the list of groups assigned to user
     */
    public List<PoulpeGroup> getGroups() {
        return groups;
    }

    /**
     * Sets list of groups assigned to user.
     *
     * @param groups the new list of groups to set
     */
    public void setGroups(List<PoulpeGroup> groups) {
        this.groups = groups;
    }

}
