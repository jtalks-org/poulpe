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
package org.jtalks.poulpe.model.logic;

import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.entity.PoulpeUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A wrapper to work with list of users with convenient methods.
 *
 * @author alexander afanasiev
 */
public class UserList {
    private final List<PoulpeUser> users = new LinkedList<PoulpeUser>();

    /** Constructor for initialization variables with array*/
    public UserList(PoulpeUser... users) {
        this.users.addAll(Arrays.asList(users));
    }

    /** Constructor for initialization variables with {@link List}*/
    public UserList(List<PoulpeUser> users) {
        this.users.addAll(users);
    }

    /**
     * Creates and fills the list of {@link PoulpeUser}s from the list of Users. Note, that this constructor actually
     * accepts a list of {@link PoulpeUser}s and then casts them, the list of {@link User}s will cause an exception.
     *
     * @param users the list of {@link PoulpeUser}s to be casted
     * @throws ClassCastException if the specified users are not of type {@link PoulpeUser}
     * @return list of {@link PoulpeUser}s
     */
    public static UserList ofCommonUsers(List<User> users) {
        List<PoulpeUser> poulpeUsers = new ArrayList<PoulpeUser>();
        for (User user : users) {
            poulpeUsers.add((PoulpeUser) user);
        }
        return new UserList(poulpeUsers);
    }

    /**
     * Returns <tt>true</tt> if this list contains no elements.
     *
     * @return <tt>true</tt> if this list contains no elements
     */
    public boolean isEmpty() {
        return users.isEmpty();
    }

    /**
     * Gets an unmodifiable list of underlying users.
     *
     * @return an unmodifiable list of underlying users
     */
    public List<PoulpeUser> getUsers() {
        return Collections.unmodifiableList(users);
    }
}