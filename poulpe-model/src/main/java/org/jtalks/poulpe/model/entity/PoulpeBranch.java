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

import org.jtalks.common.model.entity.Branch;
import org.jtalks.common.model.entity.Group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Forum branch that contains topics.
 *
 * @author Pavel Vervenko
 */
public class PoulpeBranch extends Branch {
    private Group moderatorsGroup;

    /**
     * Creates an empty branch, all fields are set to null,
     */
    public PoulpeBranch() {
        super();
    }

    /**
     * Create PoulpeBranch with name and empty description
     *
     * @param name - name for new PoulpeBranch
     */
    public PoulpeBranch(String name) {
        super(name, "");
    }

    /**
     * Create PoulpeBranch with name and description
     *
     * @param name        - name for new PoulpeBranch
     * @param description - description for new PoulpeBranch
     */
    public PoulpeBranch(String name, String description) {
        super(name, description);
    }

    /**
     * Should be used in preference of {@link #getSection()}
     *
     * @return {@link PoulpeSection}
     */
    public PoulpeSection getPoulpeSection() {
        return (PoulpeSection) getSection();
    }

    /**
     * @return an unmodifiable list of {@link User} which are signed to moderate this branch
     */
    public List<User> getModeratorsList() {
        List<User> moderators = getPoulpeUsersConvertedFromCommonGroupUsers();
        return Collections.unmodifiableList(moderators);
    }

    /**
     * Protected for using only by hibernate.
     *
     * @return the list of moderators.
     */
    protected List<User> getModerators() {
        return getPoulpeUsersConvertedFromCommonGroupUsers();
    }

    /**
     * Sets the list of users which will be signed to moderate this branch. Protected for using only by hibernate.
     *
     * @param moderators a list of {@link User}
     */
    protected void setModerators(List<User> moderators) {
        moderatorsGroup.setUsers(new ArrayList<org.jtalks.common.model.entity.User>(moderators));
    }

    /**
     * Returns a {@link Group} of moderators of this branch. This field is an extra information about moderators, since
     * we already have ACL records that explain what permissions groups have on branches, but this moderators group is
     * just for convenience - in order to easily work with moderators.
     *
     * @return a group of moderators for this branch
     */
    public Group getModeratorsGroup() {
        return moderatorsGroup;
    }

    /**
     * Sets a {@link Group} of moderators for this branch. This field is an extra information about moderators, since we
     * already have ACL records that explain what permissions groups have on branches, but this moderators group is just
     * for convenience - in order to easily work with moderators.
     *
     * @param moderatorsGroup a group of moderators for this branch
     */
    public void setModeratorsGroup(Group moderatorsGroup) {
        this.moderatorsGroup = moderatorsGroup;
    }

    /**
     * Unlike usual situation, in our case this method is used by presentation layer to depict forum structure, so this
     * method should be changed only if this presentation changed the way it shows branches.
     *
     * @return {@link #getName()}
     */
    @Override
    public String toString() {
        return getName();
    }

    private List<User> getPoulpeUsersConvertedFromCommonGroupUsers() {
        List<org.jtalks.common.model.entity.User> commonUsers = getGroupUsers();
        List<User> moderators = new ArrayList<User>(commonUsers.size());
        for (org.jtalks.common.model.entity.User user : commonUsers) {
            moderators.add((User) user);
        }
        return moderators;
    }

    private List<org.jtalks.common.model.entity.User> getGroupUsers() {
        return moderatorsGroup.getUsers();
    }
}
