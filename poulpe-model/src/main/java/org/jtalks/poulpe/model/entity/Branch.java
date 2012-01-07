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

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.validation.annotations.UniqueConstraint;
import org.jtalks.poulpe.validation.annotations.UniqueField;

/**
 * Forum branch that contains topics related to branch theme.
 *
 * @author Pavel Vervenko
 */
@UniqueConstraint
public class Branch extends Entity {

    public static final String BRANCH_ALREADY_EXISTS = "branches.error.branch_name_already_exists";
    public static final String BRANCH_CANT_BE_VOID = "branches.error.branch_name_cant_be_void";
    public static final String ERROR_LABEL_SECTION_NAME_WRONG = "sections.editsection.name.err";

    @UniqueField(message = BRANCH_ALREADY_EXISTS)
    @NotNull(message = BRANCH_CANT_BE_VOID)
    @NotEmpty(message = BRANCH_CANT_BE_VOID)
    @Length(min = 1, max = 254, message = ERROR_LABEL_SECTION_NAME_WRONG)
    private String name;
    private String description;
    private boolean deleted;
    private Integer position;
    private Section section;

    private List<User> moderators = new ArrayList<User>();

    /**
     * Default constructor
     */
    public Branch() {
    }

    /**
     * Create Branch with name.
     *
     * @param name - name for branch
     */
    public Branch(String name) {
        this.name = name;
    }

    /**
     * Create Branch with name and description
     *
     * @param name        - name for new Branch
     * @param description - description for new Branch
     */
    public Branch(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Get branch name which briefly describes the topics contained in it.
     *
     * @return Branch name as String
     */
    public String getName() {
        return name;
    }

    /**
     * Set branch name.
     *
     * @param name - Branch name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get branch description.
     *
     * @return Branch description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set branch description which contains additional information about the
     * branch.
     *
     * @param description - Branch description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Check if branch marked is deleted.
     *
     * @return true if is deleted, false otherwise
     */
    public boolean getDeleted() {
        return deleted;
    }

    /**
     * Mark branch as deleted. True means that branch is deleted.
     *
     * @param deleted - boolean flag.
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Returns the section in which this branch is.
     *
     * @return the parent section
     */
    public Section getSection() {
        return section;
    }

    /**
     * Sets the section in which this branch is.
     *
     * @param section the parent section
     */
    public void setSection(Section section) {
        this.section = section;
    }

    /**
     * @return an unmodifiable list of {@link User} which are signed to moderate
     *         this branch
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
     */
    public void addModerator(User user) {
        this.moderators.add(user);
    }

    /**
     * Assigns a list of {@link User} to moderate this branch
     *
     * @param users - list of users
     */
    public void addModerators(List<User> users) {
        this.moderators.addAll(users);
    }

    /**
     * Assigns a list of {@link User} to moderate this branch. This method
     * mainly for using in test.
     *
     * @param users - list of users
     */
    public void addModerators(User... users) {
        this.addModerators(Arrays.asList(users));
    }

    /**
     * Removes an assignment for {@link User} to moderate this branch
     */
    public void removeModerator(User user) {
        this.moderators.remove(user);
    }

    /**
     * Checks if {@link User} is assigned to moderate this branch
     *
     * @param user - user for check
     * @return {@code true} if assigned, {@code false} otherwise
     */
    public boolean isModeratedBy(User user) {
        return this.moderators.contains(user);
    }

    @Override
    public String toString() {
        return "Branch [id=" + getId() + ", name=" + name + ", description=" + description + ", position=" + position + "]";
    }

    /**
     * Gets the position.
     * 
     * @return the position
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * Sets the position.
     * 
     * @param position
     *            the position to set
     */
    public void setPosition(Integer position) {
        this.position = position;
    }
}
