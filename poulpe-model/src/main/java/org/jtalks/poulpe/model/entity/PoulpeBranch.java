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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jtalks.common.model.entity.Branch;

/**
 * Forum branch that contains topics
 * 
 * @author Pavel Vervenko
 */
public class PoulpeBranch extends Branch implements BranchSectionVisitable {
    private List<User> moderators = new ArrayList<User>();
    private List<PoulpeGroup> groups = new ArrayList<PoulpeGroup>();

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
     * @param name - name for new PoulpeBranch
     * @param description - description for new PoulpeBranch
     */
    public PoulpeBranch(String name, String description) {
        super(name, description);
    }

    /**
     * Should be used in preference of {@link #getSection()}
     * @return {@link PoulpeSection}
     */
    public PoulpeSection getPoulpeSection() {
        return (PoulpeSection) getSection();
    }

    /**
     * @return an unmodifiable list of {@link User} which are signed to moderate
     * this branch
     */
    public List<User> getModeratorsList() {
        return Collections.unmodifiableList(moderators);
    }

    /**
     * Protected for using only by hibernate.
     * 
     * @return the list of moderators.
     */
    protected List<User> getModerators() {
        return moderators;
    }

    /**
     * Sets the list of users which will be signed to moderate this branch.
     * Protected for using only by hibernate.
     * 
     * @param moderators a list of {@link User}
     */
    protected void setModerators(List<User> moderators) {
        this.moderators = moderators;
    }

    /**
     * Assigns {@link User} to moderate this branch
     * 
     * @param user to be assigned
     */
    public void addModerator(User user) {
        moderators.add(user);
    }

    /**
     * Assigns a list of {@link User} to moderate this branch
     * 
     * @param users - list of moderators
     */
    public void addModerators(List<User> users) {
        moderators.addAll(users);
    }

    /**
     * Assigns a list of {@link User} to moderate this branch. This method
     * mainly for using in tests
     * 
     * @param users - arrays of moderators
     */
    public void addModerators(User... users) {
        addModerators(Arrays.asList(users));
    }

    /**
     * Removes an assignment for {@link User} to moderate this branch
     * 
     * @param user to be removed from moderators
     */
    public void removeModerator(User user) {
        moderators.remove(user);
    }

    /**
     * Checks if {@link User} is assigned to moderate this branch
     * 
     * @param user - user for check
     * @return {@code true} if assigned, {@code false} otherwise
     */
    public boolean isModeratedBy(User user) {
        return moderators.contains(user);
    }

    /**
     * Returns a list of user {@link PoulpeGroup}s of this branch.
     * 
     * @return list of user groups
     */
    public List<PoulpeGroup> getGroups() {
        return groups;
    }

    /**
     * Assigns a list of user {@link PoulpeGroup}s for this branch.
     * 
     * @param groups - list of user groups
     */
    public void setGroups(List<PoulpeGroup> groups) {
        this.groups = groups;
    }

    /**
     * Adds a user {@link PoulpeGroup} for this branch.
     * 
     * @param group - user group to add
     */
    public void addGroup(PoulpeGroup group) {
        this.groups.add(group);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(BranchSectionVisitor visitor) {
        visitor.visitBranch(this);
    }

    /**
     * Adds a new group, or updates, if it already has it
     * 
     * @param group to be added
     */
    public void addOrUpdateGroup(PoulpeGroup group) {
        for (int i = 0; i < groups.size(); i++) {
            if (group.getId() == groups.get(i).getId()) {
                groups.set(i, group);
                return;
            }
        }
        addGroup(group);
//        group.setBranch(this);
    }
}
