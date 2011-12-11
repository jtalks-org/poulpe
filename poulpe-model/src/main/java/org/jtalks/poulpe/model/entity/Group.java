package org.jtalks.poulpe.model.entity;

import org.jtalks.common.model.entity.Entity;

/**
 * User Groups is the class that can join users into groups. After that permissions can be assigned to the groups and
 * all users in this group will have that permission while browsing components.
 *
 * @author Akimov Knostantin
 */
public class Group extends Entity {
    private String name;
    private String description;

    public Group() {
    }

    /**
     * @param name        the title of the groups, when saving to DB, can't be empty or {@code null}, it also should be
     *                    unique
     * @param description an optional description of the group
     */
    public Group(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Gets the title of the group, if it's already in DB, it's unique and not empty or {@code null}.
     *
     * @return the title of the group
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the title of the group, when saving to DB, can't be empty or {@code null}, it also should be unique.
     *
     * @param name the title of the group
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the textual description of the group.
     *
     * @return the optional description of the group
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the optional textual description of the group.
     *
     * @param description the description of the group; optional
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
