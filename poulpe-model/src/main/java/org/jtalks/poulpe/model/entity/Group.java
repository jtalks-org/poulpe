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

import org.jtalks.common.model.entity.Entity;

/**
 * User Groups is the class that can join users into groups. After that
 * permissions can be assigned to the groups and all users in this group will
 * have that permission while browsing components.
 * 
 * @author Akimov Knostantin
 */
public class Group extends Entity {
    private String name;
    private String description;

    public Group() {
    }

    /**
     * @param name the title of the groups, when saving to DB, can't be empty or
     * {@code null}, it also should be unique
     * @param description an optional description of the group
     */
    public Group(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Gets the title of the group, if it's already in DB, it's unique and not
     * empty or {@code null}.
     * 
     * @return the title of the group
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the title of the group, when saving to DB, can't be empty or
     * {@code null}, it also should be unique.
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
