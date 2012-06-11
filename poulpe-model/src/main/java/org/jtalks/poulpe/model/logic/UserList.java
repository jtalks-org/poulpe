package org.jtalks.poulpe.model.logic;

import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.entity.PoulpeUser;

import java.util.*;

/**
 * A wrapper to work with list of users with convenient methods.
 *
 * @author alexander afanasiev
 */
public class UserList {
    private final List<PoulpeUser> users = new LinkedList<PoulpeUser>();

    public UserList(PoulpeUser... users) {
        this.users.addAll(Arrays.asList(users));
    }

    public UserList(List<PoulpeUser> users) {
        this.users.addAll(users);
    }

    /**
     * Creates and fills the list of {@link PoulpeUser}s from the list of Users. Note, that this constructor actually
     * accepts a list of {@link PoulpeUser}s and then casts them, the list of {@link User}s will cause an exception.
     *
     * @param users the list of {@link PoulpeUser}s to be casted
     * @throws ClassCastException if the specified users are not of type {@link PoulpeUser}
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